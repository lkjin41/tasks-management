package com.github.lkjin41.tasksmanagement.service;
import com.github.lkjin41.tasksmanagement.dto.task.TaskCreateDto;
import com.github.lkjin41.tasksmanagement.dto.task.TaskUpdateDto;
import com.github.lkjin41.tasksmanagement.entity.task.Task;
import com.github.lkjin41.tasksmanagement.entity.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.exception.TaskAlreadyCompletedException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final Map<Long, Task> tasksStorage;
    private final AtomicLong counterId;

    public TaskService() {
        this.tasksStorage = new HashMap<>();
        this.counterId = new AtomicLong();
    }

    public Task getTaskById(Long id) throws NoSuchElementException {
        if (!tasksStorage.containsKey(id)) {
            throw new NoSuchElementException("couldn't find task by id = " + id);
        }
        return tasksStorage.get(id);
    }

    public List<Task> getAllTasks() {
        return tasksStorage.values().stream().toList();
    }

    public void deleteTask(Long id) {
        if (!tasksStorage.containsKey(id)) {
            throw new NoSuchElementException("couldn't find task by id = " + id);
        }
        tasksStorage.remove(id);
    }

    public void createTask(TaskCreateDto taskToCreate) throws IllegalArgumentException {
        tasksStorage.put(counterId.get(), new Task(
                counterId.get(),
                taskToCreate.getCreatorId(),
                taskToCreate.getAssignedUserId(),
                TaskStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                taskToCreate.getPriority()
        ));
        counterId.incrementAndGet();
    }

    public Task updateTask(TaskUpdateDto taskToUpdate) throws BadRequestException{
        Long taskId = taskToUpdate.getId();

        Task task = tasksStorage.get(taskId);

        if (task == null) {
            throw new NoSuchElementException(
                    "Couldn't find task by id = " + taskId
            );
        }

        if (task.getStatus() == TaskStatus.DONE) {
            throw new TaskAlreadyCompletedException();
        }

        if (taskToUpdate.getDeadlineTime().isBefore(task.getCreateDateTime())) {
            throw new BadRequestException("deadline cant be before create date");
        }

        task.setCreatorId(taskToUpdate.getCreatorId());
        task.setAssignedUserId(taskToUpdate.getAssignedUserId());
        task.setStatus(taskToUpdate.getStatus());
        task.setPriority(taskToUpdate.getPriority());
        task.setDeadlineTime(taskToUpdate.getDeadlineTime());

        return task;
    }
}

