package com.library.clap.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorDTO(
    Long id,
    @NotBlank(message = "Last name is required") String lastName,
    @NotBlank(message = "First name is required") String firstName
) {}
