package com.github.lkjin41.tasksmanagement;

import com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskSearchFilter;
import com.github.lkjin41.tasksmanagement.tasks.repository.TaskRepository;
import com.github.lkjin41.tasksmanagement.tasks.service.TaskService;
import com.github.lkjin41.tasksmanagement.tasks.task.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
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
    void getTaskById_ThrowsException_WhenTaskNotFound() {

        Long taskId = 99L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> taskService.getTaskById(taskId)
        );

    }

    @Test
    void getAllTasks_ReturnsMappedTasks() {
        TaskSearchFilter taskSearchFilter = new TaskSearchFilter(
                null,
                null,
                null,
                null,
                10,
                0
        );

        var pageable = Pageable
                .ofSize(10)
                .withPage(0);

        Task task = mock(Task.class);
        TaskEntity taskEntity = new TaskEntity();

        when(taskRepository.searchAllByFilter(
                taskSearchFilter.creatorId(),
                taskSearchFilter.assignedUserId(),
                taskSearchFilter.status(),
                taskSearchFilter.priority(),
                pageable
        )).thenReturn(List.of(taskEntity));

        when(taskMapper.toTask(taskEntity)).thenReturn(task);

        List<Task> result = taskService.getAllTasks(taskSearchFilter);

        Assertions.assertEquals(1, result.size(), "Should return list with 1 element");
        Assertions.assertEquals(task, result.get(0), "Task should match the mapped result");
    }

    @Test
    void deleteTask_DeletesTask_WhenTaskExists() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTask(taskId);

        org.mockito.Mockito.verify(taskRepository, org.mockito.Mockito.times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_ThrowsException_WhenTaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, () -> taskService.deleteTask(taskId));
        org.mockito.Mockito.verify(taskRepository, org.mockito.Mockito.never()).deleteById(taskId);
    }

    @Test
    void createTask_ReturnsCreatedTask() {
        com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskCreateDto createDto = new com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskCreateDto();
        createDto.setCreatorId(1L);
        createDto.setAssignedUserId(2L);
        createDto.setPriority(TaskPriority.High);

        TaskEntity savedEntity = new TaskEntity();
        Task expectedTask = mock(Task.class);

        when(taskRepository.save(org.mockito.ArgumentMatchers.any(TaskEntity.class))).thenReturn(savedEntity);
        when(taskMapper.toTask(savedEntity)).thenReturn(expectedTask);

        Task result = taskService.createTask(createDto);

        Assertions.assertEquals(expectedTask, result);
    }

    @Test
    void updateTask_ReturnsUpdatedTask_WhenValidationsPass() {
        com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto updateDto = new com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto();
        updateDto.setId(1L);
        updateDto.setCreatorId(2L);
        updateDto.setAssignedUserId(3L);
        updateDto.setPriority(TaskPriority.Medium);
        updateDto.setDeadlineTime(LocalDateTime.now().plusDays(10));

        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(1L);
        existingEntity.setStatus(TaskStatus.IN_PROGRESS);
        existingEntity.setCreateDateTime(LocalDateTime.now().minusDays(1));

        Task expectedTask = mock(Task.class);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(taskMapper.toTask(existingEntity)).thenReturn(expectedTask);

        Task result = taskService.updateTask(updateDto);

        Assertions.assertEquals(expectedTask, result);
        Assertions.assertEquals(2L, existingEntity.getCreatorId());
        Assertions.assertEquals(TaskPriority.Medium, existingEntity.getPriority());
    }

    @Test
    void updateTask_ThrowsTaskAlreadyCompletedException_WhenTaskIsDone() {
        com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto updateDto = new com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto();
        updateDto.setId(1L);

        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(1L);
        existingEntity.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

        Assertions.assertThrows(com.github.lkjin41.tasksmanagement.tasks.exception.TaskAlreadyCompletedException.class,
                () -> taskService.updateTask(updateDto));
    }

    @Test
    void updateTask_ThrowsIllegalStateException_WhenDeadlineBeforeCreation() {
        com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto updateDto = new com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto();
        updateDto.setId(1L);
        updateDto.setDeadlineTime(LocalDateTime.now().minusDays(5));

        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(1L);
        existingEntity.setStatus(TaskStatus.IN_PROGRESS);
        existingEntity.setCreateDateTime(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingEntity));

        Assertions.assertThrows(IllegalStateException.class,
                () -> taskService.updateTask(updateDto));
    }

    @Test
    void transferTaskStatus_TransfersStatus_WhenValidationsPass() {
        Long taskId = 1L;
        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(taskId);
        existingEntity.setAssignedUserId(2L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));
        when(taskRepository.countByAssignedUserIdAndStatus(2L, TaskStatus.IN_PROGRESS)).thenReturn(4L);

        taskService.transferTaskStatus(taskId);

        org.mockito.Mockito.verify(taskRepository, org.mockito.Mockito.times(1)).transferStatus(taskId, TaskStatus.IN_PROGRESS);
    }

    @Test
    void transferTaskStatus_ThrowsException_WhenAssignedUserIdIsNull() {
        Long taskId = 1L;
        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(taskId);
        existingEntity.setAssignedUserId(null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));

        Assertions.assertThrows(IllegalStateException.class, () -> taskService.transferTaskStatus(taskId));
    }

    @Test
    void transferTaskStatus_ThrowsException_WhenMaxTasksReached() {
        Long taskId = 1L;
        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(taskId);
        existingEntity.setAssignedUserId(2L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));
        when(taskRepository.countByAssignedUserIdAndStatus(2L, TaskStatus.IN_PROGRESS)).thenReturn(5L);

        Assertions.assertThrows(IllegalStateException.class, () -> taskService.transferTaskStatus(taskId));
    }

    @Test
    void transferTaskStatusDone_SetsStatusDone() {
        Long taskId = 1L;
        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(taskId);
        existingEntity.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));

        taskService.transferTaskStatusDone(taskId);

        org.mockito.Mockito.verify(taskRepository, org.mockito.Mockito.times(1))
                .setStatusDoneAndUpdateDoneDateTime(org.mockito.ArgumentMatchers.eq(taskId),
                                                    org.mockito.ArgumentMatchers.eq(TaskStatus.DONE),
                                                    org.mockito.ArgumentMatchers.any(LocalDateTime.class));
    }

    @Test
    void transferTaskStatusDone_ThrowsException_WhenStatusCreated() {
        Long taskId = 1L;
        TaskEntity existingEntity = new TaskEntity();
        existingEntity.setId(taskId);
        existingEntity.setStatus(TaskStatus.CREATED);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));

        Assertions.assertThrows(IllegalStateException.class, () -> taskService.transferTaskStatusDone(taskId));
    }
}
