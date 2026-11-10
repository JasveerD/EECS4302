package model.logical;

import model.Program;

public class Negation extends LogicalOperation {

    public LogicalOperation value;

    public Negation(LogicalOperation value) {
        this.value = value;
    }

    @Override
    public Boolean evaluate(Program p) {
        return !this.value.evaluate(p);
    }
}
