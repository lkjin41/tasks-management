package com.github.lkjin41.tasksmanagement;

import com.github.lkjin41.tasksmanagement.tasks.repository.TaskRepository;
import com.github.lkjin41.tasksmanagement.tasks.service.TaskService;
import com.github.lkjin41.tasksmanagement.tasks.task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TasksManagementApplicationTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_ReturnsTask_WhenTaskExists() {

        Long taskId = 1L;
        Task task = new Task(
                taskId,
                2L,
                3L,
                TaskStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                null,
                TaskPriority.Low
                );
        TaskEntity taskEntity = new TaskEntity(
                TaskPriority.Low,
                task.getDeadlineTime(),
                task.getCreateDateTime(),
                TaskStatus.CREATED,
                3L,
                2L,
                taskId
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toTask(taskEntity)).thenReturn(task);

        Task result = taskService.getTaskById(taskId);

        Assertions.assertNotNull(result, "Task should be not empty");
        Assertions.assertEquals(task, result, "Returned task does not match the expected task");

    }

    @Test
    void getTaskById_ThrowsException_WhenTaskNotFound () {

        Long taskId = 99L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> taskService.getTaskById(taskId)
        );

    }

}
