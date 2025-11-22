package com.library.clap.service;

import com.library.clap.dto.AuthorDTO;
import com.library.clap.entity.Author;
import com.library.clap.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Hugo", "Victor", null);
        authorDTO = new AuthorDTO(1L, "Hugo", "Victor");
    }

    @Test
    void testGetAllAuthors() {
        // Given
        List<Author> authors = Arrays.asList(author);
        when(authorRepository.findAll()).thenReturn(authors);

        // When
        List<AuthorDTO> result = authorService.getAllAuthors();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hugo", result.get(0).lastName());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void testGetAuthorById() {
        // Given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        // When
        AuthorDTO result = authorService.getAuthorById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Hugo", result.lastName());
        assertEquals("Victor", result.firstName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAuthorById_NotFound() {
        // Given
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authorService.getAuthorById(999L));
        verify(authorRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateAuthor() {
        // Given
        AuthorDTO newAuthorDTO = new AuthorDTO(null, "Dumas", "Alexandre");
        Author savedAuthor = new Author(2L, "Dumas", "Alexandre", null);
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        // When
        AuthorDTO result = authorService.createAuthor(newAuthorDTO);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("Dumas", result.lastName());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor() {
        // Given
        AuthorDTO updatedDTO = new AuthorDTO(1L, "Hugo", "Victor Marie");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // When
        AuthorDTO result = authorService.updateAuthor(1L, updatedDTO);

        // Then
        assertNotNull(result);
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testDeleteAuthor() {
        // Given
        when(authorRepository.existsById(1L)).thenReturn(true);

        // When
        authorService.deleteAuthor(1L);

        // Then
        verify(authorRepository, times(1)).existsById(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAuthor_NotFound() {
        // Given
        when(authorRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> authorService.deleteAuthor(999L));
        verify(authorRepository, times(1)).existsById(999L);
        verify(authorRepository, never()).deleteById(999L);
    }

    @Test
    void testSearchAuthorsByLastName() {
        // Given
        List<Author> authors = Arrays.asList(author);
        when(authorRepository.findByLastNameContainingIgnoreCase("Hugo")).thenReturn(authors);

        // When
        List<AuthorDTO> result = authorService.searchAuthorsByLastName("Hugo");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hugo", result.get(0).lastName());
        verify(authorRepository, times(1)).findByLastNameContainingIgnoreCase("Hugo");
    }
}
