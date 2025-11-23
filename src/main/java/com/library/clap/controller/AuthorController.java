package com.library.clap.controller;

import com.library.clap.dto.AuthorDTO;
import com.library.clap.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authors", description = "API for managing authors")
public class AuthorController {
    
    private final AuthorService authorService;
    
    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors in the library")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve a specific author by their ID")
    public ResponseEntity<AuthorDTO> getAuthorById(
            @Parameter(description = "Author ID") @PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }
    
    @PostMapping
    @Operation(summary = "Create a new author", description = "Add a new author to the library")
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createAuthor(authorDTO));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Update an existing author's information")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @Parameter(description = "Author ID") @PathVariable Long id,
            @Valid @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Remove an author from the library")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "Author ID") @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search authors", description = "Search authors by last name")
    public ResponseEntity<List<AuthorDTO>> searchAuthors(
            @Parameter(description = "Author's last name") @RequestParam String lastName) {
        return ResponseEntity.ok(authorService.searchAuthorsByLastName(lastName));
    }
}
