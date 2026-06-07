package com.github.lkjin41.tasksmanagement.controller;

import com.github.lkjin41.tasksmanagement.dto.task.TaskCreateDto;
import com.github.lkjin41.tasksmanagement.dto.task.TaskUpdateDto;
import com.github.lkjin41.tasksmanagement.domain.task.Task;
import com.github.lkjin41.tasksmanagement.service.TaskService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> transferTaskStatus(
            @PathVariable Long id
    ) {
        log.info("transferTaskStatus method was called with id={}", id);
        try {
            taskService.transferTaskStatus(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            log.warn("transferTaskStatus: couldn't find task with provided id={}", id);
            return ResponseEntity.status(404).build();
        } catch (IllegalStateException e) {
            log.warn("transferTaskStatus: {}", e.getMessage());
            return ResponseEntity.status(409).build();
        }

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
            @RequestBody TaskCreateDto taskToCreate
    ) {
        log.info("createTask method was called");
        try {
            Task savedTask = taskService.createTask(taskToCreate);
            return ResponseEntity.ok().body(savedTask);
        } catch (IllegalArgumentException e) {
            log.warn("create: couldn't create a task with illegal arg");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(
            @RequestBody TaskUpdateDto taskToUpdate
            ) {
        log.info("updateTask method was called with id={}", taskToUpdate.getId());
        try {
            return ResponseEntity.ok().body(taskService.updateTask(taskToUpdate));
        } catch (NoSuchElementException e) {
            log.warn("update: couldn't find a task with id={}", taskToUpdate.getId());
            return ResponseEntity.status(404).build();
        } catch (BadRequestException e) {
            log.warn("update: deadline cant be before create date");
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {
        log.info("deleteTask method was called with id={}", id);
        try {
            taskService.deleteTask(id);
            log.info("successfully delete a task with id={}", id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            log.info("delete: couldn't find a task with id={}", id);
            return ResponseEntity.status(404).build();
        }

    }
}



