package com.library.clap.controller;

import com.library.clap.dto.BookDTO;
import com.library.clap.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGetAllBooks() throws Exception {
        // Given
        List<BookDTO> books = Arrays.asList(
            new BookDTO(1L, "Les Misérables", 12.50, LocalDate.of(1862, 4, 3), 1L, "Hugo", "Victor"),
            new BookDTO(2L, "Notre-Dame de Paris", 10.99, LocalDate.of(1831, 3, 16), 1L, "Hugo", "Victor")
        );
        when(bookService.getAllBooks()).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Les Misérables"))
                .andExpect(jsonPath("$[1].title").value("Notre-Dame de Paris"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        // Given
        BookDTO book = new BookDTO(1L, "Les Misérables", 12.50, 
                                     LocalDate.of(1862, 4, 3), 1L, "Hugo", "Victor");
        when(bookService.getBookById(1L)).thenReturn(book);

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Les Misérables"))
                .andExpect(jsonPath("$.price").value(12.50));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testCreateBook() throws Exception {
        // Given
        BookDTO savedBook = new BookDTO(3L, "Germinal", 14.00, 
                                          LocalDate.of(1885, 3, 1), 2L, "Zola", "Émile");
        when(bookService.createBook(any(BookDTO.class))).thenReturn(savedBook);

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Germinal\",\"price\":14.00,\"publicationDate\":\"1885-03-01\",\"authorId\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Germinal"));

        verify(bookService, times(1)).createBook(any(BookDTO.class));
    }

    @Test
    void testUpdateBook() throws Exception {
        // Given
        BookDTO updatedBook = new BookDTO(1L, "Les Misérables (Special Edition)", 15.00,
                                            LocalDate.of(1862, 4, 3), 1L, "Hugo", "Victor");
        when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(updatedBook);

        // When & Then
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Les Misérables (Special Edition)\",\"price\":15.00,\"publicationDate\":\"1862-04-03\",\"authorId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(15.00));

        verify(bookService, times(1)).updateBook(eq(1L), any(BookDTO.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        // Given
        doNothing().when(bookService).deleteBook(1L);

        // When & Then
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void testSearchBooks() throws Exception {
        // Given
        List<BookDTO> books = Arrays.asList(
            new BookDTO(1L, "Les Misérables", 12.50, LocalDate.of(1862, 4, 3), 1L, "Hugo", "Victor")
        );
        when(bookService.searchBooksByTitle("Misérables")).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books/search?title=Misérables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Les Misérables"));

        verify(bookService, times(1)).searchBooksByTitle("Misérables");
    }

    @Test
    void testGetBooksByAuthor() throws Exception {
        // Given
        List<BookDTO> books = Arrays.asList(
            new BookDTO(1L, "Les Misérables", 12.50, LocalDate.of(1862, 4, 3), 1L, "Hugo", "Victor")
        );
        when(bookService.getBooksByAuthor(1L)).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorId").value(1));

        verify(bookService, times(1)).getBooksByAuthor(1L);
    }
}
