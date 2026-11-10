# EECS4302

## Modules
- Declared using the module keyword, at the top of the file before the main method
- All variables declared within a module are local to it (the same name can be reused in other modules, or the main method), and are static (there is only one instance of a module once it has been declared)

## Variable Declarations
- Declared using the var keyword, and defining their type (int or bool). Once declared, they cannot be changed

## Variable Assignments
- Must occur after declarations. If attempting to assign a value to a variable that has not been declared, a compilation error will throw. If the value or expression being assigned does not match the variables type, a compilation error will throw

## Conditionals
- Evaluates a logical expression to determine whether to proceed with that branch

## Loops
- For loops require an arithmetic expression (or anything evaluating to a number) for the start value, end value, and incrementer (or decrementer)
- While loops execute everything in the body until the logical expression evaluates to false
