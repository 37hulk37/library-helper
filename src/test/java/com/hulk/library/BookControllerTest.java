//package com.hulk.library;
//
//import com.hulk.library.dao.BookRepository;
//import com.hulk.library.entity.Book;
//import com.hulk.library.service.BookService;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jooq.AutoConfigureJooq;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Objects;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureJooq
//@AutoConfigureMockMvc
//class BookControllerTest {
//    @Autowired
//    private BookRepository repository;
//    @Autowired
//    private MockMvc mockMvc;
//
////    @Container
////    static PostgreSQLContainer<?> postgreSQLContainer =
////            new PostgreSQLContainer<>("postgres:15-alpine")
////                    .withDatabaseName("spring")
////                    .withUsername("postgres")
////                    .withPassword("postgres");
////
////    @DynamicPropertySource
////    static void setPostgreSQLContainer(DynamicPropertyRegistry registry) {
////        registry.add("postgresql.driver", postgreSQLContainer::getDriverClassName);
////    }
//
//    @Order(1)
//    @Test
//    void createBook() throws Exception {
//        var newBook = new Book(
//                "C++",
//                "Bjarne Stroustrup",
//                "IT",
//                "..."
//        );
//
//        mockMvc.perform(post("/api/books")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(String.valueOf(newBook))
//        )
//                .andExpectAll(
//                        status().isOk(),
//                        content().contentType(MediaType.APPLICATION_JSON),
//                        content().string(String.valueOf(newBook))
//                );
//    }
//
//    @Order(2)
//    @Test
//    void createBookWithNameAlreadyExist() throws Exception {
//        var name = repository.findById(1L)
//                .get()
//                .getName();
//
//        var newBook = new Book(
//                name,
//                "Bjarne Stroustrup",
//                "IT",
//                "..."
//        );
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(newBook))
//                )
//                .andExpect(status().isNotFound());
//    }
//
//    @Order(3)
//    @Test
//    void updateBook() throws Exception {
//        var bookName = repository.findById(1L)
//                .get()
//                .getName();
//
//        var updatedBook = repository.save(new Book(
//                bookName,
//                "C++",
//                "Bjarne Stroustrup",
//                "IT",
//                "..."
//        ));
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(updatedBook))
//                )
//                .andExpectAll(
//                        status().isOk(),
//                        content().contentType(MediaType.APPLICATION_JSON),
//                        content().string(String.valueOf(updatedBook))
//                );
//    }
//
//    @Order(4)
//    @Test
//    void updateBookWithNameAlreadyExist() throws Exception {
//        var book = repository.findById(1L).get();
//        var anotherBook = repository.findById(2L).get();
//
////        var updatedBook = new Book(
////                book.getId(),
////                anotherBook.getName(),
////                book.getAuthor(),
////                book.getTopic(),
////                book.getDescription()
////        );
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(updatedBook))
//                )
//                .andExpect(status().isNotFound());
//    }
//
//
//    @Order(5)
//    @Test
//    void findByIdInfo() throws Exception {
//        var book = booksDao.findById(1);
//
//        mockMvc.perform(get("/api/books/{id}", book.getId()))
//                .andExpectAll(
//                        status().isOk(),
//                        content().contentType(MediaType.APPLICATION_JSON),
//                        content().string(String.valueOf(book))
//                );
//    }
//
//    @Order(6)
//    @Test
//    void findByIdInfo_whenBookDoesNotExist() throws Exception {
//        mockMvc.perform(post("/api/books/12345"))
//                .andExpect(status().isMethodNotAllowed());
//    }
//}
