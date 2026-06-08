package com.github.lkjin41.tasksmanagement.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Task {
    private Long id;
    private Long creatorId;
    private Long assignedUserId;
    private TaskStatus status;
    private LocalDateTime createDateTime;
    private LocalDateTime deadlineTime;
    private LocalDateTime doneDateTime;
    private TaskPriority priority;
}
