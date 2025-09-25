package com.fdsystem.backend.dto;

import lombok.Data;

@Data
public class TicketResponseDTO {
    private String subject;
    private String description;
    private String status;
    private String response;
}
