package model;

import antlr.ExprBaseVisitor;
import antlr.ExprParser.VariableDeclarationContext;
import antlr.ExprParser.Variable_declarationContext;
import antlr.ExprParser.WhileContext;
import antlr.ExprParser.AdditionSubtractionContext;
import antlr.ExprParser.Arithmetic_operationContext;
import antlr.ExprParser.BiconditionalContext;
import antlr.ExprParser.BooleanLiteralContext;
import antlr.ExprParser.BooleanVariableContext;
import antlr.ExprParser.ConditionalContext;
import antlr.ExprParser.ConjunctionContext;
import antlr.ExprParser.DisjunctionContext;
import antlr.ExprParser.ExpressionContext;
import antlr.ExprParser.ForContext;
import antlr.ExprParser.IfBlockContext;
import antlr.ExprParser.If_blockContext;
import antlr.ExprParser.ImplicationContext;
import antlr.ExprParser.IntegerLiteralContext;
import antlr.ExprParser.IntegerVariableContext;
import antlr.ExprParser.ModuleDeclarationContext;
import antlr.ExprParser.MultiplicationDivisionContext;
import antlr.ExprParser.NegationContext;
import antlr.ExprParser.NegativeContext;
import antlr.ExprParser.ParenthesizedArithmeticContext;
import antlr.ExprParser.ParenthesizedLogicalContext;
import antlr.ExprParser.RelationalOperationContext;
import antlr.ExprParser.VariableAssignmentContext;
import model.arithmetic.ArithmeticOperation;
import model.arithmetic.BinaryArithmetic;
import model.arithmetic.IntegerLiteral;
import model.arithmetic.IntegerVariable;
import model.arithmetic.Negative;
import model.logical.BooleanLiteral;
import model.logical.BooleanVariable;
import model.logical.BinaryLogical;
import model.logical.BinaryRelational;
import model.logical.LogicalOperation;
import model.logical.Negation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

public class AntlrToExpression extends ExprBaseVisitor<Expression> {
    public List<String> vars;
    public Map<String, String> varTypes;   // Map var name to type
    private List<String> semanticErrors;
    private String currModule;

    public AntlrToExpression(List<String> semanticErrors) {
        this.vars = new ArrayList<>();
        this.varTypes = new HashMap<>();
        this.semanticErrors = semanticErrors;
        this.currModule = null;
    }

    @Override
    public Expression visitModuleDeclaration(ModuleDeclarationContext ctx) {
        String id = ctx.ID().getText();
        Module newModule = new Module(id);
        currModule = id;
        for (Variable_declarationContext varDecl : ctx.variable_declaration()) {
            Expression expr = this.visit(varDecl);
            newModule.variables.add((VariableDeclaration) expr);
        }

        currModule = null;
        return newModule;
    }

    @Override
    public Expression visitVariableDeclaration(VariableDeclarationContext ctx) {
        String id = ctx.ID().getText();
        String type = ctx.type().getText();
        if (currModule != null) {
            id = currModule + "." + id;
        }
        vars.add(id);
        varTypes.put(id, type);

        return new VariableDeclaration(id, type);
    }

    @Override
    public Expression visitVariableAssignment(VariableAssignmentContext ctx) {
        String id = ctx.nested_id().getText();
        Assignment assignVar = new Assignment(id);

        // a lone variable assignment (x = y) would default to evaluating y as arithmetic based on the grammar
        // explicitly check whether it is the case before visiting it normally
        String rhs = ctx.operation().getText();
        Expression value;
        if ("bool".equals(varTypes.get(rhs))) {
            value = new BooleanVariable(rhs);
        } else {
            value = this.visit(ctx.operation());
        }

        Token idToken = ctx.nested_id().getStart();
        if (!varTypes.containsKey(id)) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": variable `" + id + "` has not been declared");
        } else if (value instanceof ArithmeticOperation && !varTypes.get(id).equals("int")) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": type mismatch, trying to assign an int to a boolean");
        } else if ((value instanceof LogicalOperation) && !varTypes.get(id).equals("bool")) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": type mismatch, trying to assign a boolean to an int");
        }
        assignVar.value = value;

        return assignVar;
    }

    @Override
    public Expression visitParenthesizedArithmetic(ParenthesizedArithmeticContext ctx) {
        return this.visit(ctx.getChild(1));
    }

    @Override
    public Expression visitNegative(NegativeContext ctx) {
        return new Negative((ArithmeticOperation) this.visit(ctx.getChild(1)));
    }

    @Override
    public Expression visitMultiplicationDivision(MultiplicationDivisionContext ctx) {
        ArithmeticOperation left = (ArithmeticOperation) this.visit(ctx.getChild(0));
        ArithmeticOperation right = (ArithmeticOperation) this.visit(ctx.getChild(2));
        BinaryArithmetic muldiv = new BinaryArithmetic(left, right);
        muldiv.operator = ctx.mul_div_operator().getText();
        return muldiv;
    }

    @Override
    public Expression visitAdditionSubtraction(AdditionSubtractionContext ctx) {
        ArithmeticOperation left = (ArithmeticOperation) this.visit(ctx.getChild(0));
        ArithmeticOperation right = (ArithmeticOperation) this.visit(ctx.getChild(2));
        BinaryArithmetic addsub = new BinaryArithmetic(left, right);
        addsub.operator = ctx.add_sub_operator().getText();
        return addsub;
    }

    @Override
    public Expression visitIntegerLiteral(IntegerLiteralContext ctx) {
        return new IntegerLiteral(Integer.parseInt(ctx.NUM().getText()));
    }

    @Override
    public Expression visitIntegerVariable(IntegerVariableContext ctx) {
        String id = ctx.getText();
        Token idToken = ctx.getStart();
        if (!varTypes.containsKey(id)) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": variable `" + id + "` has not been declared");
        } else if (!varTypes.get(id).equals("int")) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": type mismatch, trying to assign a boolean to an int");
        }
        return new IntegerVariable(ctx.getText());
    }

    @Override
    public Expression visitParenthesizedLogical(ParenthesizedLogicalContext ctx) {
        return this.visit(ctx.getChild(1));
    }

    @Override
    public Expression visitNegation(NegationContext ctx) {
        return new Negation((LogicalOperation) this.visit(ctx.getChild(1)));
    }

    @Override
    public Expression visitConjunction(ConjunctionContext ctx) {
        LogicalOperation left = (LogicalOperation) this.visit(ctx.getChild(0));
        LogicalOperation right = (LogicalOperation) this.visit(ctx.getChild(2));
        BinaryLogical binlog = new BinaryLogical(left, right);
        binlog.operator = ctx.getChild(1).getText();
        return binlog;
    }

    @Override
    public Expression visitDisjunction(DisjunctionContext ctx) {
        LogicalOperation left = (LogicalOperation) this.visit(ctx.getChild(0));
        LogicalOperation right = (LogicalOperation) this.visit(ctx.getChild(2));
        BinaryLogical binlog = new BinaryLogical(left, right);
        binlog.operator = ctx.getChild(1).getText();
        return binlog;
    }

    @Override
    public Expression visitImplication(ImplicationContext ctx) {
        LogicalOperation left = (LogicalOperation) this.visit(ctx.getChild(0));
        LogicalOperation right = (LogicalOperation) this.visit(ctx.getChild(2));
        BinaryLogical binlog = new BinaryLogical(left, right);
        binlog.operator = ctx.getChild(1).getText();
        return binlog;
    }

    @Override
    public Expression visitBiconditional(BiconditionalContext ctx) {
        LogicalOperation left = (LogicalOperation) this.visit(ctx.getChild(0));
        LogicalOperation right = (LogicalOperation) this.visit(ctx.getChild(2));
        BinaryLogical binlog = new BinaryLogical(left, right);
        binlog.operator = ctx.getChild(1).getText();
        return binlog;
    }

    @Override
    public Expression visitRelationalOperation(RelationalOperationContext ctx) {
        ArithmeticOperation left = (ArithmeticOperation) this.visit(ctx.getChild(0));
        ArithmeticOperation right = (ArithmeticOperation) this.visit(ctx.getChild(2));
        BinaryRelational binrel = new BinaryRelational(left, right);
        binrel.operator = ctx.relational_operator().getText();
        return binrel;
    }

    @Override
    public Expression visitBooleanLiteral(BooleanLiteralContext ctx) {
        return new BooleanLiteral(Boolean.parseBoolean(ctx.BOOL().getText()));
    }

    @Override
    public Expression visitBooleanVariable(BooleanVariableContext ctx) {
        String id = ctx.getText();
        Token idToken = ctx.getStart();
        if (!varTypes.containsKey(id)) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": variable `" + id + "` has not been declared");
        } else if (!varTypes.get(id).equals("bool")) {
            semanticErrors.add("Error at " + idToken.getLine() + "," + idToken.getCharPositionInLine() + ": type mismatch, trying to assign an int to a boolean");
        }
        return new BooleanVariable(ctx.getText());
    }

    @Override
    public Expression visitConditional(ConditionalContext ctx) {
        Conditional cond = new Conditional();
        for (If_blockContext ifblock : ctx.if_block()) {
            cond.ifBlocks.add((IfBlock) this.visit(ifblock));
        }
        if (ctx.body() != null) {   // only if the "else" block exists
            List<Expression> expressions = new ArrayList<>();
            for (ParseTree child : ctx.body().children) {
                if (child instanceof Variable_declarationContext || child instanceof ExpressionContext) {
                    expressions.add(this.visit(child));
                }
            }
            cond.ifBlocks.add(new IfBlock(new BooleanLiteral(true), expressions));
        }

        return cond;
    }

    @Override
    public Expression visitIfBlock(IfBlockContext ctx) {
        LogicalOperation guard = (LogicalOperation) this.visit(ctx.getChild(2));
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < ctx.getChild(4).getChildCount(); i++) {
            ParseTree child = ctx.getChild(4).getChild(i);
            if (child instanceof Variable_declarationContext || child instanceof ExpressionContext) {
                expressions.add(this.visit(child));
            }
        }
        return new IfBlock(guard, expressions);
    }

    @Override
    public Expression visitFor(ForContext ctx) {
        List<ArithmeticOperation> loopParams = new ArrayList<>();
        for (Arithmetic_operationContext op : ctx.arithmetic_operation()) {
            loopParams.add((ArithmeticOperation) this.visit(op));   // instantiates the start, end, and step vars
        }

        For forloop = new For(loopParams.get(0), loopParams.get(1), loopParams.get(2));

        for (ParseTree child : ctx.body().children) {
            if (child instanceof Variable_declarationContext || child instanceof ExpressionContext) {
                forloop.expressions.add(this.visit(child));
            }
        }

        return forloop;
    }

    @Override
    public Expression visitWhile(WhileContext ctx) {
        While whileloop = new While((LogicalOperation) this.visit(ctx.logical_operation()));

        for (ParseTree child : ctx.body().children) {
            if (child instanceof Variable_declarationContext || child instanceof ExpressionContext) {
                whileloop.expressions.add(this.visit(child));
            }
        }

        return whileloop;
    }

}
