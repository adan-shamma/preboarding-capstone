package com.example.tasks;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(
        @NotBlank
        String title,
        boolean done
) {
}