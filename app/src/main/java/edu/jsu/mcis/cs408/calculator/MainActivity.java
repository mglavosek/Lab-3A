package edu.jsu.mcis.cs408.calculator;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int ROWS = 4;
    private static final int COLS = 5;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
    }

    private void initLayout() {
        ConstraintLayout constraintLayout = binding.layout;

        // Create and set up display TextView
        TextView displayTextView = new TextView(this);
        displayTextView.setId(View.generateViewId());
        displayTextView.setTextSize(48);
        displayTextView.setText(R.string.zero_placeholder); // Placeholder value
        displayTextView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        // Set layout parameters for the display TextView
        ConstraintLayout.LayoutParams displayLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        displayTextView.setLayoutParams(displayLayoutParams);

        // Add display TextView to the layout and set constraints
        constraintLayout.addView(displayTextView);

        // Set constraints for the display TextView
        ConstraintSet displayConstraintSet = new ConstraintSet();
        displayConstraintSet.clone(constraintLayout);
        displayConstraintSet.connect(displayTextView.getId(), ConstraintSet.TOP, R.id.guideNorth, ConstraintSet.BOTTOM);
        displayConstraintSet.connect(displayTextView.getId(), ConstraintSet.START, R.id.guideWest, ConstraintSet.END);
        displayConstraintSet.connect(displayTextView.getId(), ConstraintSet.END, R.id.guideEast, ConstraintSet.START);

        // Apply constraints to the layout for the display TextView
        displayConstraintSet.applyTo(constraintLayout);

        // Arrays for button text, tag names, and button IDs
        String[] buttonTexts = getResources().getStringArray(R.array.button_texts);
        String[] buttonTags = getResources().getStringArray(R.array.button_tags);
        int[] buttonIds = new int[ROWS * COLS];

        // Arrays for horizontal and vertical chains
        int[][] horizontals = new int[ROWS][COLS];
        int[][] verticals = new int[COLS][ROWS];

        // Initialize buttons dynamically
        for (int i = 0; i < ROWS * COLS; i++) {
            Button button = new Button(this);
            button.setId(View.generateViewId());
            button.setText(buttonTexts[i]);
            button.setTag(buttonTags[i]);
            button.setTextSize(24);

            // Set layout parameters for the button
            ConstraintLayout.LayoutParams buttonLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            button.setLayoutParams(buttonLayoutParams);

            // Add button to the layout and set constraints
            constraintLayout.addView(button);
            buttonIds[i] = button.getId();

            //Set constraints
            ConstraintSet buttonConstraintSet = new ConstraintSet();
            buttonConstraintSet.clone(constraintLayout);

            int row = i / COLS;
            int col = i % COLS;

            // Set top constraint for the first row
            if (row == 0) {
                buttonConstraintSet.connect(button.getId(), ConstraintSet.TOP, displayTextView.getId(), ConstraintSet.BOTTOM);
            } else {
                buttonConstraintSet.connect(button.getId(), ConstraintSet.TOP, buttonIds[(row - 1) * COLS], ConstraintSet.BOTTOM);
            }

            // Set left constraint for the first column
            if (col == 0) {
                buttonConstraintSet.connect(button.getId(), ConstraintSet.START, R.id.guideWest, ConstraintSet.END);
            } else {
                buttonConstraintSet.connect(button.getId(), ConstraintSet.START, buttonIds[i - 1], ConstraintSet.END);
            }

            // Populate horizontal and vertical chain arrays
            horizontals[row][col] = button.getId();
            verticals[col][row] = button.getId();

            // Apply constraints to the layout for buttons
            buttonConstraintSet.applyTo(constraintLayout);
        }

        // Apply horizontal chains for buttons
        for (int i = 0; i < ROWS; i++) {
            ConstraintSet buttonConstraintSet = new ConstraintSet();
            buttonConstraintSet.clone(constraintLayout);
            buttonConstraintSet.createHorizontalChain(
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT,
                    horizontals[i],
                    null,
                    ConstraintSet.CHAIN_PACKED
            );
            buttonConstraintSet.applyTo(constraintLayout);
        }

        // Apply vertical chains for buttons
        for (int i = 0; i < COLS; i++) {
            ConstraintSet buttonConstraintSet = new ConstraintSet();
            buttonConstraintSet.clone(constraintLayout);
            buttonConstraintSet.createVerticalChain(
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    verticals[i],
                    null,
                    ConstraintSet.CHAIN_PACKED
            );
            buttonConstraintSet.applyTo(constraintLayout);
        }
    }
}



