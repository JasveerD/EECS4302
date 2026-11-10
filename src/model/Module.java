package model;

import java.util.ArrayList;
import java.util.List;

public class Module extends Expression {
    public String id;
    public List<VariableDeclaration> variables;

    public Module(String id) {
        this.id = id;
        this.variables = new ArrayList<>();
    }

    @Override
    public void execute(Program p) {
        for (VariableDeclaration decl : variables) {
            decl.execute(p);
        }
    }
}
