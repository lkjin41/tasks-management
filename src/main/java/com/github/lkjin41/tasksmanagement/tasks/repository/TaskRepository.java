package com.github.lkjin41.tasksmanagement.tasks.repository;

import com.github.lkjin41.tasksmanagement.tasks.task.TaskPriority;
import com.github.lkjin41.tasksmanagement.tasks.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.tasks.task.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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
    void transferStatus(
            @Param("id") Long id,
            @Param("status") TaskStatus status
    );

    @Transactional
    @Modifying
    @Query(
    """
         update TaskEntity t
         set t.status = :status,
         t.doneDateTime = :doneDateTime
         where t.id = :id
    """)
    void setStatusDoneAndUpdateDoneDateTime(
            @Param("id") Long id,
            @Param("status") TaskStatus status,
            @Param("doneDateTime") LocalDateTime doneDateTime
    );

    @Query(
        """
        select t from TaskEntity t
        where (:creatorId IS NULL OR t.creatorId = :creatorId)
        AND (:assignedUserId IS NULL OR t.assignedUserId = :assignedUserId)
        AND (:status IS NULL OR t.status = :status)
        AND (:priority IS NULL OR t.priority = :priority)
        """
    )
    List<TaskEntity> searchAllByFilter(
            @Param("creatorId") Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("pageable") Pageable pageable
    );

}
