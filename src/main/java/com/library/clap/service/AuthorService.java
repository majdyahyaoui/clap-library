package com.library.clap.service;

import com.library.clap.dto.AuthorDTO;
import com.library.clap.entity.Author;
import com.library.clap.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return convertToDTO(author);
    }
    
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setLastName(authorDTO.lastName());
        author.setFirstName(authorDTO.firstName());
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }
    
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        author.setLastName(authorDTO.lastName());
        author.setFirstName(authorDTO.firstName());
        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }
    
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
    
    public List<AuthorDTO> searchAuthorsByLastName(String lastName) {
        return authorRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private AuthorDTO convertToDTO(Author author) {
        return new AuthorDTO(
            author.getId(),
            author.getLastName(),
            author.getFirstName()
        );
    }
}
