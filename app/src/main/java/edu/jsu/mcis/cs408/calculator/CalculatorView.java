package edu.jsu.mcis.cs408.calculator;

import android.widget.TextView;

public class CalculatorView {

    private final TextView displayTextView;

    public CalculatorView(TextView displayTextView) {
        this.displayTextView = displayTextView;
    }

    public void updateDisplay(String displayText) {
        displayTextView.setText(displayText);
    }
}
