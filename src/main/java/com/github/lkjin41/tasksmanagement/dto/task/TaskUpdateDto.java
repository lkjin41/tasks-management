package com.github.lkjin41.tasksmanagement.dto.task;

import com.github.lkjin41.tasksmanagement.entity.task.TaskPriority;
import com.github.lkjin41.tasksmanagement.entity.task.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateDto {
    private Long id;
    private Long creatorId;
    private Long assignedUserId;
    private TaskStatus status;
    private LocalDateTime createDateTime;
    private LocalDateTime deadlineTime;
    private TaskPriority priority;
}
