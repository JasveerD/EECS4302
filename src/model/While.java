package model;

import java.util.ArrayList;
import java.util.List;

import model.logical.LogicalOperation;

public class While extends Expression {
    public LogicalOperation guard;
    public List<Expression> expressions;

    public While(LogicalOperation guard) {
        this.guard = guard;
        this.expressions = new ArrayList<>();
    }

    @Override
    public void execute(Program p) {
        while (this.guard.evaluate(p)) {
            for (Expression expr : expressions) {
                expr.execute(p);
            }
        }
    }

}
