package model;

import java.util.List;

import model.logical.LogicalOperation;

public class IfBlock extends Expression {
    public LogicalOperation guard;
    public List<Expression> expressions;

    public IfBlock(LogicalOperation guard, List<Expression> expressions) {
        this.guard = guard;
        this.expressions = expressions;
    }

    @Override
    public void execute(Program p) {
        for (Expression expr : this.expressions) {
            expr.execute(p);
        }
    }
}
