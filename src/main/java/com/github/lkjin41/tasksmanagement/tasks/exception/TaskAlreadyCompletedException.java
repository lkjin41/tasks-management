package com.github.lkjin41.tasksmanagement.tasks.exception;


public class TaskAlreadyCompletedException extends RuntimeException {

    public TaskAlreadyCompletedException() {
        super("task is already done");
    }

}
