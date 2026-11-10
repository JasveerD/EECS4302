package model;

public class Assignment extends Expression {
    public String id;
    public Expression value;

    public Assignment(String id) {
        this.id = id;
    }

    @Override
    public void execute(Program p) {
        p.variables.put(this.id, ((Operation<?, ?>) value).evaluate(p));
    }
}
