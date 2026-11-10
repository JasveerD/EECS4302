package model.arithmetic;

import model.Program;

public class IntegerVariable extends ArithmeticOperation {

    public String id;

    public IntegerVariable(String id) {
        this.id = id;
    }

    public Integer evaluate(Program p) {
        return (Integer) p.variables.get(this.id);
    }
}
