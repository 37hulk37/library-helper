package com.hulk.library;

import com.hulk.library.dao.BookRepository;
import com.hulk.library.dao.EventRepository;
import com.hulk.library.dao.ReaderRepository;
import com.hulk.library.entity.Book;
import com.hulk.library.entity.Event;
import com.hulk.library.entity.Reader;
import com.hulk.library.exception.NotFoundException;
import com.hulk.library.utils.request.NewBookRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Order(1)
    @Test
    void createBook() throws Exception {
        var newBook = new NewBookRequest(
                "C++",
                "Bjarne Stroustrup",
                "IT",
                "..."
        );

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newBook))
        )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").isNotEmpty(),
                        jsonPath("name").value(newBook.getName()),
                        jsonPath("author").value(newBook.getAuthor()),
                        jsonPath("topic").value(newBook.getTopic()),
                        jsonPath("description").value(newBook.getDescription())
                );
    }

    @Order(2)
    @Test
    void createBookWithNameAlreadyExist() throws Exception {
        var name = bookRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Book not found"))
                .getName();

        var newBook = new NewBookRequest(
                name,
                "Bjarne Stroustrup",
                "IT",
                "..."
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(newBook))
                )
                .andExpect(status().isBadRequest());
    }

    @Order(3)
    @Test
    void updateBook() throws Exception {
        var book = bookRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        book.setAuthor("Graydon Hoare");

        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(book))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").value(book.getId()),
                        jsonPath("name").value(book.getName()),
                        jsonPath("author").value(book.getAuthor()),
                        jsonPath("topic").value(book.getTopic()),
                        jsonPath("description").value(book.getDescription())
                );
    }

    @Order(4)
    @Test
    void updateBookWithNameAlreadyExist() throws Exception {
        var book = bookRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        var anotherBook = bookRepository.save(new Book(
                "Kotlin",
                "Jetbrains",
                "IT",
                "..."
        ));

        book.setName(anotherBook.getName());

        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(book))
                )
                .andExpect(status().isBadRequest());
    }

    @Order(5)
    @Test
    void findBookById() throws Exception {
        var book = bookRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("id").value(book.getId()),
                        jsonPath("name").value(book.getName()),
                        jsonPath("author").value(book.getAuthor()),
                        jsonPath("topic").value(book.getTopic()),
                        jsonPath("description").value(book.getDescription())
                );
    }

    @Order(6)
    @Test
    void findBookById_whenDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/books/12345"))
                .andExpect(status().isNotFound());
    }

    @Order(7)
    @Test
    void findMostPopularBook() throws Exception {
        var book = bookRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        var reader = readerRepository.save(new Reader("Google"));

        var event = borrowAndReturnBook(book, reader);
        MultiValueMap<String, String> params = createQueryParams(event.getFrom(), event.getUntil());

        mockMvc.perform(get("/api/books")
                        .params(params)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("id").value(book.getId()),
                        jsonPath("name").value(book.getName()),
                        jsonPath("author").value(book.getAuthor()),
                        jsonPath("topic").value(book.getTopic()),
                        jsonPath("description").value(book.getDescription())
                );
    }

    private String formatDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private Event borrowAndReturnBook(Book book, Reader reader) {
        int days = 10;
        var from = Date.from(Instant.now());
        var until = DateUtils.addDays(from, days);

        var event = new Event(
                reader,
                book,
                from,
                until
        );
        event.setReturned(true);

        return eventRepository.save(event);
    }

    private MultiValueMap<String, String> createQueryParams(Date from, Date until) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", formatDate(from));
        params.add("until", formatDate(until));

        return params;
    }
}
