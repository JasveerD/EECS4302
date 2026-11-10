package model;

import java.util.ArrayList;
import java.util.List;

import model.arithmetic.ArithmeticOperation;

public class For extends Expression {
    public ArithmeticOperation start;
    public ArithmeticOperation end;
    public ArithmeticOperation step;
    public List<Expression> expressions;

    public For(ArithmeticOperation start, ArithmeticOperation end, ArithmeticOperation step) {
        this.start = start;
        this.end = end;
        this.step = step;
        this.expressions = new ArrayList<>();
    }

    @Override
    public void execute(Program p) {
        int startInt = this.start.evaluate(p);
        int endInt = this.end.evaluate(p);
        int stepInt = this.step.evaluate(p);

        // choose which way to loop based on whether the start is bigger or smaller than the end boundary
        if (startInt < endInt) {
            for (int i = startInt; i <= endInt; i += stepInt) {
                for (Expression expr : this.expressions) {
                    expr.execute(p);
                }
            }
        } else if (startInt > endInt) {
            for (int i = startInt; i >= endInt; i += stepInt) {
                for (Expression expr : this.expressions) {
                    expr.execute(p);
                }
            }
        }
    }
}
