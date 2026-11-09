grammar Expr;

@header {
    package antlr;
}

prog: (module_declaration)* 'main' '(' (variable_declaration|expression)* ')' EOF #Program
    ;

// you can only declare variables in a module, all assignments occur in the main method
module_declaration: 'module' ID '{' (variable_declaration)* '}' #ModuleDeclaration
    ;

// variables can be assigned a 'module' type as well
variable_declaration: 'var' ID type #VariableDeclaration
    ;

expression: operation
    | variable_assignment
    | conditional
    | loop
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
    | nested_id                                #IntegerVariable
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
    | nested_id                            #BooleanVariable
    ;

variable_assignment: nested_id '=' operation
    ;

// used to retrieve variables from module variables (like class attributes in Java)
nested_id: ID ('.' ID)*
    ;

conditional: if_block ('else' if_block)* ('else' body)?
    ;

if_block: 'if' '(' logical_operation ')' body
    ;

body: '{' (variable_declaration|expression)* '}'
    ;

loop: for | while
    ;

// takes a starting number, an ending number, and an increment/decrement value
for: 'for' '(' arithmetic_operation ',' arithmetic_operation ',' arithmetic_operation ')' body
    ;

while: 'while' '(' logical_operation ')'  body
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
