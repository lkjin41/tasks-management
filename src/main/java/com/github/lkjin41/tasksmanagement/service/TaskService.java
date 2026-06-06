package com.github.lkjin41.tasksmanagement.service;

import com.github.lkjin41.tasksmanagement.dto.mapping.TaskMapping;
import com.github.lkjin41.tasksmanagement.dto.task.TaskCreateDto;
import com.github.lkjin41.tasksmanagement.dto.task.TaskUpdateDto;
import com.github.lkjin41.tasksmanagement.entity.task.Task;
import com.github.lkjin41.tasksmanagement.entity.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.exception.TaskAlreadyCompletedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final Map<Long, Task> tasksStorage;
    private final TaskMapping taskMapping;
    private final AtomicLong counterId;

    public TaskService(TaskMapping taskMapping) {
        this.tasksStorage = new HashMap<>();
        this.taskMapping = taskMapping;
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

    public void createTask(TaskCreateDto taskToCreate) throws IllegalArgumentException{
        tasksStorage.put(counterId.get(), new Task(
                counterId.get(),
                taskToCreate.getCreatorId(),
                taskToCreate.getAssignedUserId(),
                TaskStatus.CREATED,
                taskToCreate.getCreateDateTime(),
                taskToCreate.getDeadlineTime(),
                taskToCreate.getPriority()
        ));
        counterId.incrementAndGet();
    }

    public Task updateTask(TaskUpdateDto taskToUpdate) {
        Long taskId = taskToUpdate.getId();
        if (!tasksStorage.containsKey(taskId)) {
            throw new NoSuchElementException("couldn't find task by id = " + taskId);
        }
        Task prevTask = tasksStorage.get(taskId);
        if (prevTask.getStatus() == TaskStatus.DONE) {
            throw new TaskAlreadyCompletedException();
        }
        return null;
    }
}

