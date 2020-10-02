<h1 align=center>Evaluator</h1>

Evaluator is a Java library used to evaluate a string of text that represents a mathematical expression. It is written in Java 8, but relies only on simple classes from the standard library, so should the lib should work on almost any Java version.

```Java
String math = "4 + 5 * -4^3";
String result = Evaluator.solveToString(math);

System.out.println(result);
```
Output:
```
-316
```
The library was original developed to aid in another for-fun project.

## Features

### Basic Arithmetic
Evaluator supports all the standard arithmetic operations:
* Addition (`+`)
* Subtraction (`-`)
* Multiplication (`*`)
* Division (`/`)
* Exponentiation (`^`)
* Modulo (`%`)
* Negation (Unary Minus) (`-`)
* Absolute Value (Unary Plus) (`+`)

> *Negation and Force Positive are unary operators.*

> *Absolute Value makes the expression that it is applied to always result in a positive number. e.g. `+(2 - 4)` would result in positive `2`.*

Evaluator solves all expressions with regard to standard order of operations.

```Java
String math = "1 + 2 ^ 3 / 4";
System.out.println(Evaluator.solveToString(math));
```
Output: `3`

### Parentheses
Parentheses are supported, and can be used to group expressions, overriding standard order of operations.

```Java
String math = "1 + 2 * (3 - 4)";
System.out.println(Evaluator.solveToString(math));
```
Output: `-1`

If an operator is missing between a parenthesis and another expression, multiplication is assumed:
```
String math = "(4 - 2)5 + 3(1 + 1)";
System.out.println(Evaluator.solveToString(math));
```
Output: `16`

### Constants & Variables
The constants `Pi` and `e` are supported. Their capitalization is ignored.
```Java
String math = "pi * e";
System.out.println(Evaluator.solveToString(math));
```
Output: `8.539734222673565677848730527685`

> *These two constants' values are taken from the Java Standard Library `Math` class.*

The API keeps track of a `VariableMap` that stores each variable that a single `Evaluator` instance can access. See [State](#state) below for more information on variables.

### Boolean Math
The library has full support for boolean math (math operating on `true` and `false`).
Supported boolean operators:
* AND (`&`, `&&`)
* OR (`|`, `||`)
* XOR (`^`)
* NOT (`!`)

> *NOT is a unary operator.*

Examples:
```Java
String math = "true & false | false & true";
System.out.println(Evaluator.solveToString(math));
```
Output: `false`

```Java
String math = "true & false | !false & true";
System.out.println(Evaluator.solveToString(math));
```
Output: `true`

### State
Each instance of the `Evaluator` class (used to solve expressions) keeps track of a state. This state is comprised of a set of variables known by the evaluator and a set of types that allow the evaluator to read `cast` operations. A cast operation converts an expression of one type to another type (e.g. a `Boolean` expression gets converted to a `Number` value when being solved. The standard procedure for this is to treat `true` as `1` and `false` as 0).

When not using the shortcut functions defined in the `Evaluator` class, the state of your evaluator can be manipulated:

```Java
Evaluator evaluator = new Evaluator();
evaluator.getVariableMap().new Variable<>("test", new NumericData(4));// Register a variable to the evaluator.
System.out.println(evaluator.solve(Spate.spate("test*2")));
```
Output: `8`
