package com.library.clap.repository;

import com.library.clap.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByLastNameContainingIgnoreCase(String lastName);
}
