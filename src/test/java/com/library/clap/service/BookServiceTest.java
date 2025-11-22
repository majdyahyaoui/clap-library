package com.library.clap.service;

import com.library.clap.dto.BookDTO;
import com.library.clap.entity.Author;
import com.library.clap.entity.Book;
import com.library.clap.repository.AuthorRepository;
import com.library.clap.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Hugo", "Victor", null);
        book = new Book(1L, "Les Misérables", 12.50, LocalDate.of(1862, 4, 3), author);
    }

    @Test
    void testGetAllBooks() {
        // Given
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<BookDTO> result = bookService.getAllBooks();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Les Misérables", result.get(0).title());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When
        BookDTO result = bookService.getBookById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Les Misérables", result.title());
        assertEquals(12.50, result.price());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> bookService.getBookById(999L));
        verify(bookRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateBook() {
        // Given
        BookDTO newBookDTO = new BookDTO(null, "Notre-Dame de Paris", 10.99, 
                                            LocalDate.of(1831, 3, 16), 1L, null, null);
        Book savedBook = new Book(2L, "Notre-Dame de Paris", 10.99, 
                                     LocalDate.of(1831, 3, 16), author);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // When
        BookDTO result = bookService.createBook(newBookDTO);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("Notre-Dame de Paris", result.title());
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testCreateBook_AuthorNotFound() {
        // Given
        BookDTO newBookDTO = new BookDTO(null, "Test", 10.0, LocalDate.now(), 999L, null, null);
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> bookService.createBook(newBookDTO));
        verify(authorRepository, times(1)).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        // Given
        BookDTO updatedDTO = new BookDTO(1L, "Les Misérables (Special Edition)", 15.00, 
                                           LocalDate.of(1862, 4, 3), 1L, null, null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookDTO result = bookService.updateBook(1L, updatedDTO);

        // Then
        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(true);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchBooksByTitle() {
        // Given
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findByTitleContainingIgnoreCase("Misérables")).thenReturn(books);

        // When
        List<BookDTO> result = bookService.searchBooksByTitle("Misérables");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Les Misérables", result.get(0).title());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Misérables");
    }

    @Test
    void testGetBooksByAuthor() {
        // Given
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findByAuthorId(1L)).thenReturn(books);

        // When
        List<BookDTO> result = bookService.getBooksByAuthor(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).authorId());
        verify(bookRepository, times(1)).findByAuthorId(1L);
    }
}
