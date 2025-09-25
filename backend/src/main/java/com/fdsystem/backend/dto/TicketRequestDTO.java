package com.fdsystem.backend.dto;

import lombok.Data;

@Data
public class TicketRequestDTO {
    private long userId;
    private long fdId;
    private String subject;
    private String description;
}
