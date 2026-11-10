package model.logical;

import model.Program;

public class BooleanVariable extends LogicalOperation {
    public String id;

    public BooleanVariable(String id) {
        this.id = id;
    }

    public Boolean evaluate(Program p) {
        return (Boolean) p.variables.get(this.id);
    }
}
