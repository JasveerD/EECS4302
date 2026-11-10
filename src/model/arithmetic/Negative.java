package model.arithmetic;

import model.Program;

public class Negative extends ArithmeticOperation {
    public ArithmeticOperation value;

    public Negative(ArithmeticOperation value) {
        this.value = value;
    }

    public Integer evaluate(Program p) {
        return 0 - this.value.evaluate(p);
    }
}
