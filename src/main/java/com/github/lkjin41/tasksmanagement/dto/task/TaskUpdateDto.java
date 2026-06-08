package com.github.lkjin41.tasksmanagement.dto.task;

import com.github.lkjin41.tasksmanagement.domain.task.TaskPriority;
import com.github.lkjin41.tasksmanagement.domain.task.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateDto {
    @NotNull
    private Long id;
    @NotNull
    private Long creatorId;
    @NotNull
    private Long assignedUserId;
    @NotNull
    @Future
    private LocalDateTime deadlineTime;
    @NotNull
    private TaskPriority priority;
}
