package com.github.lkjin41.tasksmanagement.tasks.controller;

import com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskCreateDto;
import com.github.lkjin41.tasksmanagement.tasks.controller.dto.task.TaskUpdateDto;
import com.github.lkjin41.tasksmanagement.tasks.task.Task;
import com.github.lkjin41.tasksmanagement.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> transferTaskStatusInProgress(
            @PathVariable Long id
    ) {
        log.info("transferTaskStatus method was called with id={}", id);

        taskService.transferTaskStatus(id);
        log.info("successfully set in_progress status to task with id={}", id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> transferTaskStatusDone(
            @PathVariable Long id
    ) {
        log.info("transferTaskStatusDone method was called with task id={}", id);
        taskService.transferTaskStatusDone(id);
        log.info("successfully set done status to task with id={}", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id
    ) {
        log.info("getTaskById method was called with id={}", id);
        return ResponseEntity.ok().body(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("getAllTasks method was called");
        return ResponseEntity.ok().body(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid TaskCreateDto taskToCreate
    ) {
        log.info("createTask method was called");

        Task savedTask = taskService.createTask(taskToCreate);
        log.info("successfully created task {}", savedTask);
        return ResponseEntity.ok().body(savedTask);
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(
            @RequestBody @Valid TaskUpdateDto taskToUpdate
    ) {
        log.info("updateTask method was called with id={}", taskToUpdate.getId());

        return ResponseEntity.ok().body(taskService.updateTask(taskToUpdate));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {
        log.info("deleteTask method was called with id={}", id);

        taskService.deleteTask(id);
        log.info("successfully delete a task with id={}", id);
        return ResponseEntity.ok().build();


    }
}



