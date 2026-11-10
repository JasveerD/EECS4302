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
variable_declaration: 'var' ID type  #VariableDeclaration
    ;

type: 'int' | 'bool'
    ;


expression: variable_assignment
    | conditional
    | loop
    ;

operation: arithmetic_operation
    | logical_operation
    ;

// visitNegative will be treated as a special case of Subtraction
arithmetic_operation: '(' arithmetic_operation ')'                  #ParenthesizedArithmetic
    | '-' arithmetic_operation                                      #Negative
    | arithmetic_operation mul_div_operator  arithmetic_operation   #MultiplicationDivision
    | arithmetic_operation add_sub_operator arithmetic_operation    #AdditionSubtraction
    | NUM                                                           #IntegerLiteral
    | nested_id                                                     #IntegerVariable
    ;

mul_div_operator: '*' | '/'
    ;

add_sub_operator: '+' | '-'
    ;

logical_operation: '(' logical_operation ')'    #ParenthesizedLogical
    | '!' logical_operation                     #Negation
    | logical_operation '&&' logical_operation  #Conjunction
    | logical_operation '||' logical_operation  #Disjunction
    | logical_operation '->' logical_operation  #Implication
    | logical_operation '<=>' logical_operation #Biconditional
    | relational_operation                      #RelationalOperationDeclaration
    | BOOL                                      #BooleanLiteral
    | nested_id                                 #BooleanVariable
    ;

relational_operation: arithmetic_operation relational_operator arithmetic_operation #RelationalOperation
    ;

relational_operator: '==' | '!=' | '<=' | '>=' | '<' | '>'
    ;

variable_assignment: nested_id '=' operation    #VariableAssignment
    ;

// used to retrieve variables from module variables (like class attributes in Java)
nested_id: ID ('.' ID)?
    ;

conditional: if_block ('else' if_block)* ('else' body)?
    ;

if_block: 'if' '(' logical_operation ')' body   #IfBlock
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

BOOL: 'true' | 'false';
ID:[a-zA-Z][a-zA-Z0-9_]*;
NUM: '0' | [1-9][0-9]*;
COMMENT: '%' ~[\r\n]* ->skip;
WS: [ \t\n\r]+ -> skip;
