package edu.jsu.mcis.cs408.calculator;

public class CalculatorController {

    private final CalculatorModel calculatorModel;
    private final CalculatorView calculatorView;

    public CalculatorController(CalculatorModel calculatorModel, CalculatorView calculatorView) {
        this.calculatorModel = calculatorModel;
        this.calculatorView = calculatorView;
    }

    public void handleButtonClick(String buttonTag) {
        String digit = buttonTag.substring(3);

        if (digit.equals(".")) {
            handleDotButtonClick();
        } else {
            switch (digit) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    handleDigitButtonClick(digit);
                    break;
                case "Plus":
                case "Minus":
                case "Multiply":
                case "Divide":
                    handleOperatorButtonClick(buttonTag);
                    break;
                case "Sqrt":
                    handleSqrtButtonClick();
                    break;
                case "Percent":
                    handlePercentButtonClick();
                    break;
                case "PlusMinus":
                    handleNegationButtonClick();
                    break;
                case "Clear":
                    handleClearButtonClick();
                    break;
                case "Equals":
                    handleEqualsButtonClick();
                    break;
            }
        }
    }

    private void handleDigitButtonClick(String digit) {
        calculatorModel.inputDigit(digit);
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handleDotButtonClick() {
        calculatorModel.inputDigit(".");
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handleOperatorButtonClick(String operator) {
        calculatorModel.inputOperator(operator);
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handleSqrtButtonClick() {
        calculatorModel.squareRoot();
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handlePercentButtonClick() {
        calculatorModel.percent();
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handleNegationButtonClick() {
        calculatorModel.negation();
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handleClearButtonClick() {
        calculatorModel.clear();
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }

    private void handleEqualsButtonClick() {
        calculatorModel.calculateResult();
        calculatorView.updateDisplay(calculatorModel.getDisplayText());
    }
}
