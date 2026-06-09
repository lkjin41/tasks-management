package com.github.lkjin41.tasksmanagement.tasks.controller.dto.task;

import com.github.lkjin41.tasksmanagement.tasks.task.TaskPriority;
import com.github.lkjin41.tasksmanagement.tasks.task.TaskStatus;

public record TaskSearchFilter(
        Long creatorId,
        Long assignedUserId,
        TaskStatus status,
        TaskPriority priority,
        int pageSize,
        int pageNum
) {
}
