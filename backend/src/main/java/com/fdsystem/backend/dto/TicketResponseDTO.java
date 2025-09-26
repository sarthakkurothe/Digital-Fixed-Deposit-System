package com.fdsystem.backend.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class TicketResponseDTO {
    private long id;
    private long fdId;
    private String subject;
    private String description;
    private String status;
    private String response;
    private Date createdDate;
}
