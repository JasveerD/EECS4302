package model;

import antlr.ExprBaseVisitor;
import java.util.ArrayList;
import java.util.List;

public class AntlrToExpression extends ExprBaseVisitor<Expression> {
    private List<String> vars;
    private List<String> semanticErrors;

    public AntlrToExpression(List<String> semanticErrors) {
        this.vars = new ArrayList<>();
        this.semanticErrors = semanticErrors;
    }
}
