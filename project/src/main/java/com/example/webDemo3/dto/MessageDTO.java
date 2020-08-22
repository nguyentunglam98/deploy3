package com.example.webDemo3.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Integer messageCode;
    private String message;

    public MessageDTO() {
        this.messageCode = null;
        this.message = null;
    }

    public MessageDTO(Integer messageCode, String message) {
        this.messageCode = messageCode;
        this.message = message;
    }
}
