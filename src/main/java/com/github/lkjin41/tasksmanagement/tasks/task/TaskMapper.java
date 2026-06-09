package com.github.lkjin41.tasksmanagement.tasks.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toTask(TaskEntity taskEntity) {
        return new Task(
                taskEntity.getId(),
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                taskEntity.getStatus(),
                taskEntity.getCreateDateTime(),
                taskEntity.getDeadlineTime(),
                taskEntity.getDoneDateTime(),
                taskEntity.getPriority()
        );
    }

    public TaskEntity toTaskEntity(Task task) {
        return new TaskEntity (
                task.getPriority(),
                task.getDeadlineTime(),
                task.getCreateDateTime(),
                task.getStatus(),
                task.getAssignedUserId(),
                task.getCreatorId(),
                task.getId()
        );
    }
}
