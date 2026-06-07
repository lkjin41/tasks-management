package com.github.lkjin41.tasksmanagement.repository;

import com.github.lkjin41.tasksmanagement.domain.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.entity.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    long countByAssignedUserIdAndStatus(Long id, TaskStatus status);

}
