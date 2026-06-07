package com.github.lkjin41.tasksmanagement.dto.task;

import com.github.lkjin41.tasksmanagement.domain.task.TaskPriority;
import lombok.Data;

@Data
public class TaskCreateDto {
    private Long creatorId;
    private Long assignedUserId;
    private TaskPriority priority;
}
