package com.library.clap.controller;

import com.library.clap.dto.BookDTO;
import com.library.clap.service.BookService;
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
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Books", description = "API for managing books")
public class BookController {
    
    private final BookService bookService;
    
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the library")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    public ResponseEntity<BookDTO> getBookById(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    
    @PostMapping
    @Operation(summary = "Create a new book", description = "Add a new book to the library")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookDTO));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book's information")
    public ResponseEntity<BookDTO> updateBook(
            @Parameter(description = "Book ID") @PathVariable Long id,
            @Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Remove a book from the library")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by title")
    public ResponseEntity<List<BookDTO>> searchBooks(
            @Parameter(description = "Book title") @RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }
    
    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get books by author", description = "Retrieve all books written by a specific author")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(
            @Parameter(description = "Author ID") @PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }
}
