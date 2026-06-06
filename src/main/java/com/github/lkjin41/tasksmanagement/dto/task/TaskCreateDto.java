package com.github.lkjin41.tasksmanagement.dto.task;

import com.github.lkjin41.tasksmanagement.entity.task.TaskPriority;
import com.github.lkjin41.tasksmanagement.entity.task.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateDto {
    private Long creatorId;
    private Long assignedUserId;
    private LocalDateTime createDateTime;
    private LocalDateTime deadlineTime;
    private TaskPriority priority;
}
