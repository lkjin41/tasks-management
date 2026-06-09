package com.github.lkjin41.tasksmanagement.tasks.controller.dto.error;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {}
