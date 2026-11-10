package model.logical;

import model.Operation;

public abstract class LogicalOperation extends Operation<LogicalOperation, Boolean> {

    public LogicalOperation() {
    }

    public LogicalOperation(LogicalOperation left, LogicalOperation right) {
        this.left = left;
        this.right = right;
    }
}
