package model.logical;

import model.Program;
import model.arithmetic.ArithmeticOperation;

public class BinaryRelational extends LogicalOperation {
    public ArithmeticOperation left;
    public ArithmeticOperation right;
    public String operator;

    public BinaryRelational(ArithmeticOperation left, ArithmeticOperation right) {
        this.left = left;
        this.right = right;
    }

    public Boolean evaluate(Program p) {
        if (operator.equals("==")) {
            return this.left.evaluate(p).equals(this.right.evaluate(p));
        } else if (operator.equals("!=")) {
            return !this.left.evaluate(p).equals(this.right.evaluate(p));
        } else if (operator.equals("<=")) {
            return this.left.evaluate(p) <= this.right.evaluate(p);
        } else if (operator.equals(">=")){
            return this.left.evaluate(p) >= this.right.evaluate(p);
        } else if (operator.equals("<")) {
            return this.left.evaluate(p) < this.right.evaluate(p);
        } else if (operator.equals(">")) {
            return this.left.evaluate(p) > this.right.evaluate(p);
        }
        return false;
    }
}
