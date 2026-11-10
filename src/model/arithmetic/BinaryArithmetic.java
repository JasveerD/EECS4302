package model.arithmetic;

import model.Program;

public class BinaryArithmetic extends ArithmeticOperation {
    public String operator;

    public BinaryArithmetic(ArithmeticOperation left, ArithmeticOperation right) {
        super(left, right);
    }

    public Integer evaluate(Program p) {
        if (operator.equals("*")) {
            return this.left.evaluate(p) * this.right.evaluate(p);
        } else if (operator.equals("/")) {
            return this.left.evaluate(p) / this.right.evaluate(p);
        } else if (operator.equals("+")) {
            return this.left.evaluate(p) + this.right.evaluate(p);
        } else if (operator.equals("-")){
            return this.left.evaluate(p) - this.right.evaluate(p);
        }
        return 0;
    }
}
