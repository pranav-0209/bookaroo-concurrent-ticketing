package com.pranav.bookaroo_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String venue;
    @Min(1)
    private int totalTickets;
    @NotNull
    private LocalDateTime eventDateTime;
}
