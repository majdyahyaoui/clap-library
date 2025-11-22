package com.library.clap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookDTO(
    Long id,
    @NotBlank(message = "Title is required") String title,
    @NotNull(message = "Price is required") @Positive(message = "Price must be positive") Double price,
    @NotNull(message = "Publication date is required") LocalDate publicationDate,
    @NotNull(message = "Author is required") Long authorId,
    String authorLastName,
    String authorFirstName
) {}
