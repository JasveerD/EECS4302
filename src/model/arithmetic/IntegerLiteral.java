package model.arithmetic;

import model.Program;

public class IntegerLiteral extends ArithmeticOperation {

    public int value;

    public IntegerLiteral(int value) {
        this.value = value;
    }

    public Integer evaluate(Program p) {
        return this.value;
    }
}
