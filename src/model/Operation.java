package model;

public abstract class Operation<T, R> extends Expression {
    public T left;
    public T right;

    public abstract R evaluate(Program p);

    @Override
    public void execute(Program p) {    // Subclasses of operations will just use built in evaluate
        evaluate(p);
    }
}
