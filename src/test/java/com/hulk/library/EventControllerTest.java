package com.hulk.library;

import com.hulk.library.dao.BookRepository;
import com.hulk.library.dao.EventRepository;
import com.hulk.library.dao.ReaderRepository;
import com.hulk.library.entity.Book;
import com.hulk.library.entity.Reader;
import com.hulk.library.exception.NotFoundException;
import com.hulk.library.utils.request.BorrowBookRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventControllerTest {
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

    @BeforeEach
    public void createBookAndReader() {
        if (!bookRepository.existsById(1L)) {
            bookRepository.save(new Book(
                    "C++",
                    "Bjarne Stroustrup",
                    "IT",
                    "..."
            ));
        }

        if (!readerRepository.existsById(1L)) {
            readerRepository.save(new Reader("Robert Griesemer"));
        }
    }

    @Order(1)
    @Test
    void borrowBook() throws Exception {
        var book = bookRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        var reader = readerRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        var newEvent = new BorrowBookRequest(
                book.getId(),
                reader.getId(),
                10
        );

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newEvent))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").isNotEmpty(),
                        jsonPath("bookId").value(newEvent.getBookId()),
                        jsonPath("readerId").value(newEvent.getReaderId()),
                        jsonPath("from").isNotEmpty(),
                        jsonPath("until").isNotEmpty(),
                        jsonPath("returned").value(false)
                );
    }

    @Order(2)
    @Test
    void handInBook() throws Exception {
        var event = eventRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        mockMvc.perform(put("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(event.getId()))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").value(event.getId()),
                        jsonPath("bookId").value(event.getBook().getId()),
                        jsonPath("readerId").value(event.getReader().getId()),
                        jsonPath("from").isNotEmpty(),
                        jsonPath("until").isNotEmpty(),
                        jsonPath("returned").value(true)
                );
    }

    @Order(3)
    @Test
    void getAllEvents() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("[0].id").isNotEmpty(),
                        jsonPath("[0].bookId").isNotEmpty(),
                        jsonPath("[0].readerId").isNotEmpty(),
                        jsonPath("[0].from").isNotEmpty(),
                        jsonPath("[0].until").isNotEmpty(),
                        jsonPath("[0].returned").isNotEmpty()
                );
    }

    @Order(4)
    @Test
    void getEventById() throws Exception {
        var event = eventRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("id").value(event.getId()),
                        jsonPath("book").isNotEmpty(),
                        jsonPath("reader").isNotEmpty(),
                        jsonPath("from").isNotEmpty(),
                        jsonPath("until").isNotEmpty(),
                        jsonPath("returned").value(true)
                );
    }

    @Order(5)
    @Test
    void findEventById_whenDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/events/12345"))
                .andExpect(status().isNotFound());
    }
}
