package com.library.clap.controller;

import com.library.clap.dto.AuthorDTO;
import com.library.clap.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void testGetAllAuthors() throws Exception {
        // Given
        List<AuthorDTO> authors = Arrays.asList(
            new AuthorDTO(1L, "Hugo", "Victor"),
            new AuthorDTO(2L, "Dumas", "Alexandre")
        );
        when(authorService.getAllAuthors()).thenReturn(authors);

        // When & Then
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Hugo"))
                .andExpect(jsonPath("$[1].lastName").value("Dumas"));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void testGetAuthorById() throws Exception {
        // Given
        AuthorDTO author = new AuthorDTO(1L, "Hugo", "Victor");
        when(authorService.getAuthorById(1L)).thenReturn(author);

        // When & Then
        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Hugo"))
                .andExpect(jsonPath("$.firstName").value("Victor"));

        verify(authorService, times(1)).getAuthorById(1L);
    }

    @Test
    void testCreateAuthor() throws Exception {
        // Given
        AuthorDTO author = new AuthorDTO(null, "Verne", "Jules");
        AuthorDTO savedAuthor = new AuthorDTO(3L, "Verne", "Jules");
        when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(savedAuthor);

        // When & Then
        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"lastName\":\"Verne\",\"firstName\":\"Jules\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.lastName").value("Verne"));

        verify(authorService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        // Given
        AuthorDTO updatedAuthor = new AuthorDTO(1L, "Hugo", "Victor Marie");
        when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class))).thenReturn(updatedAuthor);

        // When & Then
        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"lastName\":\"Hugo\",\"firstName\":\"Victor Marie\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Victor Marie"));

        verify(authorService, times(1)).updateAuthor(eq(1L), any(AuthorDTO.class));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        // Given
        doNothing().when(authorService).deleteAuthor(1L);

        // When & Then
        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNoContent());

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @Test
    void testSearchAuthors() throws Exception {
        // Given
        List<AuthorDTO> authors = Arrays.asList(new AuthorDTO(1L, "Hugo", "Victor"));
        when(authorService.searchAuthorsByLastName("Hugo")).thenReturn(authors);

        // When & Then
        mockMvc.perform(get("/api/authors/search?lastName=Hugo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Hugo"));

        verify(authorService, times(1)).searchAuthorsByLastName("Hugo");
    }
}
