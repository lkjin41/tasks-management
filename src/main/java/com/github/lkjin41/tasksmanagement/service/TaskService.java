package com.github.lkjin41.tasksmanagement.service;

import com.github.lkjin41.tasksmanagement.dto.task.TaskCreateDto;
import com.github.lkjin41.tasksmanagement.dto.task.TaskUpdateDto;
import com.github.lkjin41.tasksmanagement.domain.task.Task;
import com.github.lkjin41.tasksmanagement.domain.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.entity.task.TaskEntity;
import com.github.lkjin41.tasksmanagement.exception.TaskAlreadyCompletedException;
import com.github.lkjin41.tasksmanagement.repository.TaskRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(Long id) throws NoSuchElementException {

        TaskEntity taskFromDb = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("couldn't find task by id = " + id));

        return toDomainTask(taskFromDb);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream().map(this::toDomainTask).toList();
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("couldn't find task by id = " + id);
        }
        taskRepository.deleteById(id);
    }

    public Task createTask(TaskCreateDto taskToCreate) throws IllegalArgumentException {
        TaskEntity saved = taskRepository.save(
                new TaskEntity(
                        taskToCreate.getPriority(),
                        LocalDateTime.now().plusDays(5),
                        LocalDateTime.now(),
                        TaskStatus.CREATED,
                        taskToCreate.getAssignedUserId(),
                        taskToCreate.getCreatorId(),
                        null
                ));
        return toDomainTask(saved);
    }

    public Task updateTask(TaskUpdateDto taskToUpdate) throws IllegalStateException {
        Long taskId = taskToUpdate.getId();

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException(
                "Couldn't find task by id = " + taskId
        ));

        if (task.getStatus() == TaskStatus.DONE) {
            throw new TaskAlreadyCompletedException();
        }

        if (taskToUpdate.getDeadlineTime().isBefore(task.getCreateDateTime())) {
            throw new IllegalStateException("deadline cant be before create date");
        }

        task.setCreatorId(taskToUpdate.getCreatorId());
        task.setAssignedUserId(taskToUpdate.getAssignedUserId());
        task.setStatus(taskToUpdate.getStatus());
        task.setPriority(taskToUpdate.getPriority());
        task.setDeadlineTime(taskToUpdate.getDeadlineTime());

        return toDomainTask(task);
    }

    private Task toDomainTask(
            TaskEntity taskFromDb
    ) {
        return new Task(
                taskFromDb.getId(),
                taskFromDb.getCreatorId(),
                taskFromDb.getAssignedUserId(),
                taskFromDb.getStatus(),
                taskFromDb.getCreateDateTime(),
                taskFromDb.getDeadlineTime(),
                taskFromDb.getDoneDateTime(),
                taskFromDb.getPriority()

        );
    }

    public void transferTaskStatus(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "Couldn't find task by id = " + id
        ));

        Long AssignedUserId = task.getAssignedUserId();

        if (AssignedUserId == null) {
            throw new IllegalStateException("assignedUserId is not set for task id = " + id);
        }

        long countOfAssignedTasks = taskRepository
                .countByAssignedUserIdAndStatus(AssignedUserId, TaskStatus.IN_PROGRESS);

        if (countOfAssignedTasks >= 5) {
            throw new IllegalStateException("User already has maximum number of active tasks (IN_PROGRESS)");
        }

        taskRepository.transferStatus(id, TaskStatus.IN_PROGRESS);


    }

    public void transferTaskStatusDone(Long id) {
        taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("couldn't find task by id = " +  id)
        );

        taskRepository.setStatusDoneAndUpdateDoneDateTime(id, TaskStatus.DONE, LocalDateTime.now());
    }
}

