package com.github.lkjin41.tasksmanagement;

import java.time.LocalDateTime;

public class Task {
    private long id;
    private long creatorId;
    private long assignedUserId;
    private TaskStatus status;
    private LocalDateTime createDateTime;
    private LocalDateTime deadlineTime;
    private TaskPriority priority;

    public Task(long id,
                TaskPriority priority,
                LocalDateTime deadlineTime,
                LocalDateTime createDateTime,
                long assignedUserId,
                long creatorId,
                TaskStatus status
    ) {
        this.id = id;
        this.priority = priority;
        this.deadlineTime = deadlineTime;
        this.createDateTime = createDateTime;
        this.assignedUserId = assignedUserId;
        this.creatorId = creatorId;
        this.status = status;
    }
}
