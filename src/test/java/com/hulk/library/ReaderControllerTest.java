package com.hulk.library;

import com.hulk.library.dao.BookRepository;
import com.hulk.library.dao.EventRepository;
import com.hulk.library.dao.ReaderRepository;
import com.hulk.library.entity.Book;
import com.hulk.library.entity.Event;
import com.hulk.library.entity.Reader;
import com.hulk.library.exception.NotFoundException;
import com.hulk.library.utils.request.NewReaderRequest;
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
public class ReaderControllerTest {
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
    void createReader() throws Exception {
        var newReader = new NewReaderRequest("Bjarne Stroustrup");

        mockMvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newReader))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").isNotEmpty(),
                        jsonPath("name").value(newReader.getName())
                );
    }

    @Order(2)
    @Test
    void createReaderWithNameAlreadyExist() throws Exception {
        var name = readerRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(""))
                .getName();

        var newReader = new NewReaderRequest(name);

        mockMvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(newReader))
                )
                .andExpect(status().isBadRequest());
    }

    @Order(3)
    @Test
    void updateReader() throws Exception {
        var reader = readerRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        reader.setName("Graydon Hoare");

        mockMvc.perform(put("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reader))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").value(reader.getId()),
                        jsonPath("name").value(reader.getName())
                );
    }

    @Order(4)
    @Test
    void updateReaderWithNameAlreadyExist() throws Exception {
        var reader = readerRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Reader not found"));
        var anotherReader = readerRepository.save(new Reader("Robert Griesemer"));

        reader.setName(anotherReader.getName());

        mockMvc.perform(put("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(reader))
                )
                .andExpect(status().isBadRequest());
    }

    @Order(5)
    @Test
    void findReaderById() throws Exception {
        var reader = readerRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        mockMvc.perform(get("/api/readers/{id}", reader.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("id").value(reader.getId()),
                        jsonPath("name").value(reader.getName())
                );
    }

    @Order(6)
    @Test
    void findReaderById_whenDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/readers/12345"))
                .andExpect(status().isNotFound());
    }

    @Order(7)
    @Test
    void findBestReader() throws Exception {
        var book = bookRepository.save(new Book(
                "Kotlin",
                "Jetbrains",
                "IT",
                "..."
        ));

        var reader = readerRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        var event = borrowAndReturnBook(book, reader);
        MultiValueMap<String, String> params = createQueryParams(event.getFrom(), event.getUntil());

        mockMvc.perform(get("/api/readers")
                        .params(params)
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("id").value(reader.getId()),
                        jsonPath("name").value(reader.getName())
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


