package model.arithmetic;

import model.Operation;

public abstract class ArithmeticOperation extends Operation<ArithmeticOperation, Integer> {

    public Integer value;
    public String id;

    public ArithmeticOperation() {
    }

    public ArithmeticOperation(ArithmeticOperation left, ArithmeticOperation right) {
        this.left = left;
        this.right = right;
    }
}
