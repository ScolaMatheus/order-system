package com.microservico.restaurant.dto.response;

import java.time.LocalDateTime;

public record ExceptionDTO(String message, int statusCode, LocalDateTime timesTamp) {
}
