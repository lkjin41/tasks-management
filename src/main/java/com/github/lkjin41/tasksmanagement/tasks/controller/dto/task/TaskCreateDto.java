package com.github.lkjin41.tasksmanagement.tasks.controller.dto.task;

import com.github.lkjin41.tasksmanagement.tasks.task.TaskPriority;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateDto {
    @NotNull
    private Long creatorId;
    @NotNull
    private Long assignedUserId;
    @NotNull
    private TaskPriority priority;
}
