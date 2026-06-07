package com.github.lkjin41.tasksmanagement.entity.task;

import com.github.lkjin41.tasksmanagement.domain.task.TaskPriority;
import com.github.lkjin41.tasksmanagement.domain.task.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "tasks")
@Entity
@Getter
@Setter
public class TaskEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creator_id")
    private Long creatorId;
    @Column(name = "assigned_user_id")
    private Long assignedUserId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;
    @Column(name = "deadline_time")
    private LocalDateTime deadlineTime;
    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    public TaskEntity() {
    }

    public TaskEntity(
            TaskPriority priority,
            LocalDateTime deadlineTime,
            LocalDateTime createDateTime,
            TaskStatus status,
            Long assignedUserId,
            Long creatorId,
            Long id
    ) {
        this.priority = priority;
        this.deadlineTime = deadlineTime;
        this.createDateTime = createDateTime;
        this.status = status;
        this.assignedUserId = assignedUserId;
        this.creatorId = creatorId;
        this.id = id;
    }
}
