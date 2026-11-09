grammar Expr;

@header {
    package antlr;
}

prog: (module_declaration)* 'main' '(' (variable_declaration|expression)* ')' EOF #Program
    ;

module_declaration: 'module' ID '{' (variable_declaration)* '}' #ModuleDeclaration
    ;

variable_declaration: 'var' ID type #VariableDeclaration
    ;

expression: operation
    | variable_assignment
    | conditional
    ;

operation: arithmetic_operation
    | relational_operation
    | logical_operation
    ;

arithmetic_operation: '(' arithmetic_operation ')'  #ParenthesizedArithmetic
    | '-' arithmetic_operation                      #Negative
    | arithmetic_operation '*' arithmetic_operation #Multiplication
    | arithmetic_operation '/' arithmetic_operation #Division
    | arithmetic_operation '+' arithmetic_operation #Addition
    | arithmetic_operation '-' arithmetic_operation #Subtraction
    | NUM                                           #Integer
    | generalized_id                                #IntegerVariable
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
    | generalized_id                            #BooleanVariable
    ;

variable_assignment: generalized_id '=' operation
    ;

generalized_id: ID ('.' ID)*
    ;

conditional: if_block ('else' if_block)* ('else' body)?
    ;

if_block: 'if' '(' logical_operation ')' body
    ;

body: '{' (variable_declaration|expression)* '}'
    ;

type: 'int'
    | 'bool'
    | 'module'
    ;

BOOL: 'true' | 'false';
ID:[a-zA-Z][a-zA-Z0-9_]*;
NUM: '0' | [1-9][0-9]*;
COMMENT: '%' ~[\r\n]* ->skip;
WS: [ \t\n\r]+ -> skip;
