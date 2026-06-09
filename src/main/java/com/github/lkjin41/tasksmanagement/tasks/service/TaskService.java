package com.github.lkjin41.tasksmanagement.tasks.service;

import com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskCreateDto;
import com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskSearchFilter;
import com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto;
import com.github.lkjin41.tasksmanagement.tasks.task.Task;
import com.github.lkjin41.tasksmanagement.tasks.task.TaskMapper;
import com.github.lkjin41.tasksmanagement.tasks.task.TaskStatus;
import com.github.lkjin41.tasksmanagement.tasks.task.TaskEntity;
import com.github.lkjin41.tasksmanagement.tasks.exception.TaskAlreadyCompletedException;
import com.github.lkjin41.tasksmanagement.tasks.repository.TaskRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Task getTaskById(Long id) throws NoSuchElementException {

        TaskEntity taskFromDb = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("couldn't find task by id = " + id));

        return taskMapper.toTask(taskFromDb);
    }

    public List<Task> getAllTasks(TaskSearchFilter taskSearchFilter) {

        var pageable = Pageable
                .ofSize(taskSearchFilter.pageSize())
                .withPage(taskSearchFilter.pageNum());

        return taskRepository
                .searchAllByFilter(
                        taskSearchFilter.creatorId(),
                        taskSearchFilter.assignedUserId(),
                        taskSearchFilter.status(),
                        taskSearchFilter.priority(),
                        pageable
                )
                .stream()
                .map(taskMapper::toTask)
                .toList();
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
        return taskMapper.toTask(saved);
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
        task.setPriority(taskToUpdate.getPriority());
        task.setDeadlineTime(taskToUpdate.getDeadlineTime());

        return taskMapper.toTask(task);
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
        TaskEntity task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("couldn't find task by id = " +  id)
        );

        if (task.getStatus() == TaskStatus.CREATED){
            throw new IllegalStateException("couldn't set done status to task with created status");
        }

        taskRepository.setStatusDoneAndUpdateDoneDateTime(id, TaskStatus.DONE, LocalDateTime.now());
    }
}

