grammar Expr;

@header {
    package antlr;
}

prog: (module_declaration)* EOF #Program
    ;

module_declaration: 'module' ID '{' (variable_declaration|expression)* '}' #ModuleDeclaration
    ;

variable_declaration: 'var' ID type #VariableDeclaration
    ;

expression: operation       #OperationDeclaration
    | variable_assignment   #VariableAssignment
    | conditional           #ConditionalDeclaration
    ;

operation: arithmetic_operation #ArithmeticOperation
    | relational_operation      #RelationalOperation
    | logical_operation         #LogicalOperation
    ;

arithmetic_operation: '(' arithmetic_operation ')'  #ParenthesizedArithmetic
    | '-' arithmetic_operation                      #Negative
    | arithmetic_operation '*' arithmetic_operation #Multiplication
    | arithmetic_operation '/' arithmetic_operation #Division
    | arithmetic_operation '+' arithmetic_operation #Addition
    | arithmetic_operation '-' arithmetic_operation #Subtraction
    | NUM                                           #Integer
    | ID                                            #IntegerVariable
    ;

relational_operation: '(' relational_operation ')'      #ParenthesizedRelational
    | arithmetic_operation '==' arithmetic_operation    #Equality
    | arithmetic_operation '!=' arithmetic_operation    #Inequality
    | arithmetic_operation '<=' arithmetic_operation    #LessOrEqual
    | arithmetic_operation '>=' arithmetic_operation    #GreaterOrEqual
    | arithmetic_operation '<' arithmetic_operation     #LessThan
    | arithmetic_operation '>' arithmetic_operation     #GreaterThan
    ;

logical_operation: '(' logical_operation ')'    #ParenthesizedLogical
    | '!' logical_operation                     #Negation
    | logical_operation '&&' logical_operation  #Conjunction
    | logical_operation '||' logical_operation  #Disjunction
    | logical_operation '->' logical_operation  #Implication
    | logical_operation '<=>' logical_operation #Biconditional
    | relational_operation                      #RelationalOperationDeclaration
    | BOOL                                      #Boolean
    | ID                                        #BooleanVariable
    ;

variable_assignment: ID '=' operation
    ;

conditional: 'if' '(' logical_operation ')' body ('else' 'if' '(' logical_operation ')' body)* ('else' body)?
    ;

body: '{' (variable_declaration|expression)* '}'
    ;

type: 'int'
    | 'bool'
    ;

BOOL: 'true' | 'false';
ID:[a-zA-Z][a-zA-Z0-9_]*;
NUM: '0' | [1-9][0-9]*;
COMMENT: '%' ~[\r\n]* ->skip;
WS: [ \t\n\r]+ -> skip;
