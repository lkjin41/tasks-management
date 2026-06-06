package com.github.lkjin41.tasksmanagement.dto.mapping;

import com.github.lkjin41.tasksmanagement.dto.task.TaskResponseDto;
import com.github.lkjin41.tasksmanagement.entity.task.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapping {
    TaskResponseDto toDto(Task task);
}
