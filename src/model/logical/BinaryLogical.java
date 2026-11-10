package model.logical;

import model.Program;

public class BinaryLogical extends LogicalOperation {
    public String operator;

    public BinaryLogical(LogicalOperation left, LogicalOperation right) {
        super(left, right);
    }

    public Boolean evaluate(Program p) {
        if (operator.equals("&&")) {
            return this.left.evaluate(p) && this.right.evaluate(p);
        } else if (operator.equals("||")) {
            return this.left.evaluate(p) || this.right.evaluate(p);
        } else if (operator.equals("->")) {
            return !this.left.evaluate(p) || this.right.evaluate(p);
        } else if (operator.equals("<=>")){
            return this.left.evaluate(p).equals(this.right.evaluate(p));
        }
        return false;
    }
}
