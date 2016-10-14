package com.app.todoapp.dao.enums;

public enum TaskStatus {

    OPEN, CLOSED;

    public static TaskStatus valueOf(int ordinal) {
        TaskStatus retVal = null;
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.ordinal() == ordinal) {
                retVal = taskStatus;
                break;
            }
        }
        return retVal;
    }
}
