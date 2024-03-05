package edu.jsu.mcis.cs408.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private CalculatorController calculatorController;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the Model, View, and Controller
        CalculatorModel calculatorModel = new CalculatorModel();
        CalculatorView calculatorView = new CalculatorView(binding.displayTextView);
        calculatorController = new CalculatorController(calculatorModel, calculatorView);

        // Set up the click handler for buttons
        CalculatorClickHandler clickHandler = new CalculatorClickHandler();
        initializeButtonClickListeners(clickHandler);
    }

    private void initializeButtonClickListeners(CalculatorClickHandler clickHandler) {
        for (int i = 0; i < binding.getRoot().getChildCount(); ++i) {
            View child = binding.getRoot().getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(clickHandler);
            }
        }
    }

    // Inner class for handling button clicks
    private class CalculatorClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String tag = view.getTag().toString();
            Toast.makeText(MainActivity.this, tag, Toast.LENGTH_SHORT).show();

            // Delegate the button click to the Controller
            calculatorController.handleButtonClick(tag);
        }
    }
}



