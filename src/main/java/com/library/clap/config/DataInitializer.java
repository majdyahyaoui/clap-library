package com.library.clap.config;

import com.library.clap.entity.Author;
import com.library.clap.entity.Book;
import com.library.clap.repository.AuthorRepository;
import com.library.clap.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Profile("h2")
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (authorRepository.count() > 0) {
            log.info("Data already initialized, skipping...");
            return;
        }

        log.info("Initializing data...");

        // Create and save authors in batch
        var hugo = new Author(null, "Hugo", "Victor", null);
        var dumas = new Author(null, "Dumas", "Alexandre", null);
        var verne = new Author(null, "Verne", "Jules", null);
        var zola = new Author(null, "Zola", "Émile", null);

        authorRepository.saveAll(List.of(hugo, dumas, verne, zola));

        // Create and save books in batch
        var books = List.of(new Book(null, "Les Misérables", 12.50, LocalDate.of(1862, 4, 3), hugo), new Book(null, "Notre-Dame de Paris", 10.99, LocalDate.of(1831, 3, 16), hugo), new Book(null, "Le Comte de Monte-Cristo", 15.00, LocalDate.of(1844, 8, 28), dumas), new Book(null, "Les Trois Mousquetaires", 13.50, LocalDate.of(1844, 3, 1), dumas), new Book(null, "Vingt Mille Lieues sous les mers", 11.00, LocalDate.of(1870, 1, 1), verne), new Book(null, "Le Tour du monde en quatre-vingts jours", 9.50, LocalDate.of(1873, 1, 1), verne), new Book(null, "Germinal", 14.00, LocalDate.of(1885, 3, 1), zola));

        bookRepository.saveAll(books);

        log.info("Successfully initialized {} authors and {} books", authorRepository.count(), bookRepository.count());
    }
}
