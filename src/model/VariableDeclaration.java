package model;

public class VariableDeclaration extends Expression {
    public String id;
    public String type;

    public VariableDeclaration(String id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public void execute(Program p) {
        if (type.equals("int")) {
            p.variables.put(id, 0);
        } else {
            p.variables.put(id, false);
        }
    }
}
