package com.github.lkjin41.tasksmanagement.exception;


public class TaskAlreadyCompletedException extends RuntimeException {

    public TaskAlreadyCompletedException() {
        super("task is already done");
    }

}
