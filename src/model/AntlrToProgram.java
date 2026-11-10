package model;

import antlr.ExprBaseVisitor;
import antlr.ExprParser;
import antlr.ExprParser.ExpressionContext;
import antlr.ExprParser.Module_declarationContext;
import antlr.ExprParser.Variable_declarationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

public class AntlrToProgram extends ExprBaseVisitor<Program> {

    public List<String> semanticErrors; // to be accessed by the main application program

    @Override
    public Program visitProgram(ExprParser.ProgramContext ctx) {
        Program prog = new Program();

        semanticErrors = new ArrayList<>();
        // a helper visitor for transforming each subtree into an Expression object
        AntlrToExpression exprVisitor = new AntlrToExpression(semanticErrors);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (i == ctx.getChildCount() - 1) {
                // last child the start symbol prog is EOF
                // Do not visit this child and attempt to convert it to an Expression object
            } else {
                ParseTree child = ctx.getChild(i);
                if (child instanceof Module_declarationContext || child instanceof Variable_declarationContext || child instanceof ExpressionContext) {
                    prog.expressions.add(exprVisitor.visit(ctx.getChild(i)));
                }
            }
        }

        // use the AntlrToExpression List and Map to initialize default values for each variable
        prog.varsInOrder = exprVisitor.vars;
        for (String var : prog.varsInOrder) {
            if (exprVisitor.varTypes.get(var).equals("int")) {
                prog.variables.put(var, 0);
            } else if (exprVisitor.varTypes.get(var).equals("bool")) {
                prog.variables.put(var, false);
            }
        }



        return prog;
    }
}
