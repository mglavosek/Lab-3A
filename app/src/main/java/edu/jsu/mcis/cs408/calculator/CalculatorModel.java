package edu.jsu.mcis.cs408.calculator;

import java.math.BigDecimal;
import java.math.MathContext;

public class CalculatorModel {

    // Enum for calculator states
    public enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }

    // Enum for operators
    public enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, SQUARE_ROOT, PERCENT
    }

    // variables
    private BigDecimal lhsOperand;
    private BigDecimal rhsOperand;
    private BigDecimal result;
    private CalculatorState currentState;
    private Operator selectedOperator;
    private StringBuilder lhsOperandBuilder;  // StringBuilder for left-hand operand
    private StringBuilder rhsOperandBuilder;  // StringBuilder for right-hand operand

    // Constructor
    public CalculatorModel() {
        currentState = CalculatorState.CLEAR;
        lhsOperand = BigDecimal.ZERO;
        rhsOperand = BigDecimal.ZERO;
        result = BigDecimal.ZERO;
        selectedOperator = null;
        lhsOperandBuilder = new StringBuilder();
        rhsOperandBuilder = new StringBuilder();
    }

    // Public methods

    // Input handling methods
    public void inputDigit(String digit) {
        if (digit.equals(".")) {
            handleDotInput();
        } else {
            handleDigitInput(digit);
        }
    }

    public void inputOperator(String operator) {
        if (currentState == CalculatorState.LHS || currentState == CalculatorState.RESULT) {
            currentState = CalculatorState.OP_SELECTED;
            selectedOperator = getOperatorFromString(operator);
        } else if (currentState == CalculatorState.RHS) {
            calculateResult();
            currentState = CalculatorState.OP_SELECTED;
            selectedOperator = getOperatorFromString(operator);
        }
    }

    public void squareRoot() {
        switch (currentState) {
            case LHS:
                lhsOperandBuilder = new StringBuilder(calculateSquareRoot(parseOperand(lhsOperandBuilder)).toPlainString());
                currentState = CalculatorState.LHS;
                break;
            case RESULT:
                lhsOperandBuilder = new StringBuilder(calculateSquareRoot(result).toPlainString());
                currentState = CalculatorState.LHS;
                break;
            case OP_SELECTED:
                break;
            case RHS:
                rhsOperandBuilder = new StringBuilder(calculateSquareRoot(parseOperand(rhsOperandBuilder)).toPlainString());
                currentState = CalculatorState.RHS;
                break;
            case ERROR:
                setError();
                currentState = CalculatorState.ERROR;
                break;
        }
    }

    public void percent() {
        switch (currentState) {
            case LHS:
                lhsOperandBuilder = new StringBuilder(parseOperand(lhsOperandBuilder).multiply(BigDecimal.valueOf(0.01)).toPlainString());
                currentState = CalculatorState.LHS;
                break;
            case RESULT:
                lhsOperandBuilder = new StringBuilder(result.multiply(BigDecimal.valueOf(0.01)).toPlainString());
                currentState = CalculatorState.LHS;
                break;
            case OP_SELECTED:
                break;
            case RHS:
                rhsOperandBuilder = new StringBuilder(parseOperand(rhsOperandBuilder).multiply(BigDecimal.valueOf(0.01)).toPlainString());
                currentState = CalculatorState.RHS;
                break;
            case ERROR:
                getDisplayText();
                break;
        }
    }

    public void negation() {
        switch (currentState) {
            case LHS:
            case RESULT:
                lhsOperandBuilder = new StringBuilder(parseOperand(lhsOperandBuilder).negate().toPlainString());
                break;
            case OP_SELECTED:
                break;
            case RHS:
                rhsOperandBuilder = new StringBuilder(parseOperand(rhsOperandBuilder).negate().toPlainString());
                break;
            case ERROR:
                setError();
                break;
        }
    }

    public void clear() {
        lhsOperandBuilder = new StringBuilder();
        rhsOperandBuilder = new StringBuilder();
        result = BigDecimal.ZERO;
        selectedOperator = null;
        currentState = CalculatorState.CLEAR;
    }

    public void calculateResult() {
        switch (currentState) {
            case LHS:
            case RESULT:
                break;
            case OP_SELECTED:
                calculateResultWithOperator();
                break;
            case RHS:
                calculateResultWithOperator();
                lhsOperandBuilder = new StringBuilder(result.toPlainString());
                rhsOperandBuilder = new StringBuilder();
                currentState = CalculatorState.RESULT;
                break;
            case ERROR:
                setError();
                getDisplayText();
                break;
        }
    }

    // Helper methods

    private void handleDigitInput(String digit) {
        StringBuilder currentOperandBuilder = (currentState == CalculatorState.LHS || currentState == CalculatorState.CLEAR) ?
                lhsOperandBuilder : rhsOperandBuilder;

        if (currentOperandBuilder.length() < 8) {  // Limit the length of the operand
            currentOperandBuilder.append(digit);
            updateOperand(currentOperandBuilder);
        }
    }

    private void handleDotInput() {
        StringBuilder currentOperandBuilder = (currentState == CalculatorState.LHS || currentState == CalculatorState.CLEAR) ?
                lhsOperandBuilder : rhsOperandBuilder;

        if (!currentOperandBuilder.toString().contains(".")) {
            currentOperandBuilder.append(".");
            updateOperand(currentOperandBuilder);
        }
    }

    private void updateOperand(StringBuilder operandBuilder) {
        if (operandBuilder.length() > 0) {
            try {
                if (currentState == CalculatorState.LHS || currentState == CalculatorState.CLEAR) {
                    lhsOperand = new BigDecimal(operandBuilder.toString());
                    currentState = CalculatorState.LHS;
                } else if (currentState == CalculatorState.RHS || currentState == CalculatorState.OP_SELECTED) {
                    rhsOperand = new BigDecimal(operandBuilder.toString());
                    currentState = CalculatorState.RHS;
                }
            } catch (NumberFormatException e) {
                setError();
                currentState = CalculatorState.ERROR;
            }
        }
    }

    private void calculateResultWithOperator() {
        if (selectedOperator != null) {
            BigDecimal lhs = parseOperand(lhsOperandBuilder);
            BigDecimal rhs = parseOperand(rhsOperandBuilder);
            switch (selectedOperator) {
                case ADD:
                    result = lhs.add(rhs);
                    break;
                case SUBTRACT:
                    result = lhs.subtract(rhs);
                    break;
                case MULTIPLY:
                    result = lhs.multiply(rhs);
                    break;
                case DIVIDE:
                    if (rhs.compareTo(BigDecimal.ZERO) != 0) {
                        result = lhs.divide(rhs, MathContext.DECIMAL128);
                    } else {
                        setError();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Operator getOperatorFromString(String operator) {
        switch (operator) {
            case "btnPlus":
                return Operator.ADD;
            case "btnMinus":
                return Operator.SUBTRACT;
            case "btnMultiply":
                return Operator.MULTIPLY;
            case "btnDivide":
                return Operator.DIVIDE;
            case "btnSqrt":
                return Operator.SQUARE_ROOT;
            case "btnPercent":
                return Operator.PERCENT;
            default:
                return null;
        }
    }

    private BigDecimal calculateSquareRoot(BigDecimal operand) {
        if (operand.compareTo(BigDecimal.ZERO) >= 0) {
            return BigDecimal.valueOf(Math.sqrt(operand.doubleValue()));
        } else {
            setError();
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal parseOperand(StringBuilder operandBuilder) {
        try {
            return new BigDecimal(operandBuilder.toString());
        } catch (NumberFormatException e) {
            setError();
            return BigDecimal.ZERO;
        }
    }

    private void setError() {
        currentState = CalculatorState.ERROR;
    }

    public String getDisplayText() {
        switch (currentState) {
            case LHS:
                return lhsOperandBuilder.toString();
            case OP_SELECTED:
                return lhsOperandBuilder.toString() + " " + operatorToString(selectedOperator);
            case RHS:
                return rhsOperandBuilder.toString();
            case RESULT:
                String resultString = result.stripTrailingZeros().toPlainString();
                // Makes sure that the result will not be too long for the display
                return resultString.length() > 20 ? resultString.substring(0, 20) : resultString;
            case ERROR:
                return "Error";
            default:
                return "";
        }
    }

    private String operatorToString(Operator operator) {
        switch (operator) {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case SQUARE_ROOT:
                return "sqrt";
            case PERCENT:
                return "%";
            default:
                return "";
        }
    }
}

