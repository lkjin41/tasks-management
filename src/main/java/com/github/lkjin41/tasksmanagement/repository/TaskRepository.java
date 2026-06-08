package com.github.lkjin41.tasksmanagement.repository;

import com.github.lkjin41.tasksmanagement.domain.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.entity.task.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    long countByAssignedUserIdAndStatus(Long id, TaskStatus status);

    @Transactional
    @Modifying
    @Query(
    """
    update TaskEntity t
    set t.status = :status
    where t.id = :id
    """)
    void transferStatus(Long id, TaskStatus status);

    @Transactional
    @Modifying
    @Query(
    """
         update TaskEntity t
         set t.status = :status,
         t.doneDateTime = :doneDateTime
         where t.id = :id
    """)
    void setStatusDoneAndUpdateDoneDateTime(Long id, TaskStatus status, LocalDateTime doneDateTime);

}
