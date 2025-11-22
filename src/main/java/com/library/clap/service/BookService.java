package com.library.clap.service;

import com.library.clap.dto.BookDTO;
import com.library.clap.entity.Author;
import com.library.clap.entity.Book;
import com.library.clap.repository.AuthorRepository;
import com.library.clap.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return convertToDTO(book);
    }
    
    public BookDTO createBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.authorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + bookDTO.authorId()));
        
        Book book = new Book();
        book.setTitle(bookDTO.title());
        book.setPrice(bookDTO.price());
        book.setPublicationDate(bookDTO.publicationDate());
        book.setAuthor(author);
        
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }
    
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        
        Author author = authorRepository.findById(bookDTO.authorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + bookDTO.authorId()));
        
        book.setTitle(bookDTO.title());
        book.setPrice(bookDTO.price());
        book.setPublicationDate(bookDTO.publicationDate());
        book.setAuthor(author);
        
        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }
    
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    public List<BookDTO> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getPrice(),
            book.getPublicationDate(),
            book.getAuthor().getId(),
            book.getAuthor().getLastName(),
            book.getAuthor().getFirstName()
        );
    }
}
