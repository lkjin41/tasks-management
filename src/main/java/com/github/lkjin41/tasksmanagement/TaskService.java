package com.github.lkjin41.tasksmanagement;

import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    Map<Long, Task> tasksStorage = Map.of(
            1L, new Task(
                    1L,
                    TaskPriority.High,
                    LocalDateTime.now().plusDays(5),
                    LocalDateTime.now(),
                    11L,
                    111L,
                    TaskStatus.CREATED
            ),
            2L, new Task(
                    2L,
                    TaskPriority.Medium,
                    LocalDateTime.now().plusDays(3),
                    LocalDateTime.now(),
                    12L,
                    112L,
                    TaskStatus.CREATED
            ),
            3L, new Task(
                    3L,
                    TaskPriority.Low,
                    LocalDateTime.now().plusDays(4),
                    LocalDateTime.now(),
                    44L,
                    35L,
                    TaskStatus.CREATED
            )
    );

    public Task getTaskById(Long id) throws NoSuchElementException{
        if (!tasksStorage.containsKey(id)){
            throw new NoSuchElementException("Cant find task by id = " + id);
        }
        return tasksStorage.get(id);
    }

    public List<Task> getAllTasks() {
        return tasksStorage.values().stream().toList();
    }
}
