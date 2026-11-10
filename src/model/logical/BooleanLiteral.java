package model.logical;

import model.Program;

public class BooleanLiteral extends LogicalOperation {
    public boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public Boolean evaluate(Program p) {
        return this.value;
    }
}
