package edu.jsu.mcis.cs408.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initLayout();
    }

    private void initLayout(){
        String[] buttonTexts = getResources().getStringArray(R.array.button_texts);
        String[] buttonTags = getResources().getStringArray(R.array.button_tags);

        // Create and configure display TextView
        TextView displayTextView = new TextView(this);
        displayTextView.setId(View.generateViewId());
        displayTextView.setText(getString(R.string.zero_placeholder));
        displayTextView.setTextSize(24); // Increase text size to 24dp
        displayTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END); // Right-justify

        // Add display TextView to layout
        binding.layout.addView(displayTextView);

        // Apply display TextView constraints (anchored to the guidelines)
        ConstraintSet set = new ConstraintSet();
        set.clone(binding.layout);
        set.connect(displayTextView.getId(), ConstraintSet.TOP, binding.guideNorth.getId(), ConstraintSet.BOTTOM, 16);
        set.connect(displayTextView.getId(), ConstraintSet.START, binding.guideWest.getId(), ConstraintSet.END, 16);
        set.connect(displayTextView.getId(), ConstraintSet.END, binding.guideEast.getId(), ConstraintSet.START, 16);
        set.applyTo(binding.layout);

        // Button creation
        int[][] horizontals = new int[ROWS][COLS];
        int[][] verticals = new int[COLS][ROWS];
        int[] buttonIds = new int[ROWS * COLS];

        // Add buttons dynamically
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;

                Button button = new Button(this);
                button.setId(View.generateViewId());
                button.setText(buttonTexts[index]);
                button.setTag(buttonTags[index]);

                // Set constraints
                set.clone(binding.layout);
                set.connect(button.getId(), ConstraintSet.TOP, (i == 0) ? displayTextView.getId() : buttonIds[(i - 1) * COLS], ConstraintSet.BOTTOM, 16);
                set.connect(button.getId(), ConstraintSet.START, (j == 0) ? ConstraintSet.PARENT_ID : buttonIds[index - 1], ConstraintSet.END, 16);
                set.connect(button.getId(), ConstraintSet.END, (j == COLS - 1) ? ConstraintSet.PARENT_ID : 0, ConstraintSet.START, 16);
                set.connect(button.getId(), ConstraintSet.BOTTOM, (i == ROWS - 1) ? ConstraintSet.PARENT_ID : 0, ConstraintSet.TOP, 16);

                set.applyTo(binding.layout);

                // Add to chain arrays
                horizontals[i][j] = button.getId();
                verticals[j][i] = button.getId();
                buttonIds[index] = button.getId();

                // Add button to layout
                binding.layout.addView(button);
            }
        }

        // Create horizontal and vertical chains
        for (int i = 0; i < ROWS; i++) {
            set.clone(binding.layout);
            set.createHorizontalChain(ConstraintSet.PARENT_ID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                    horizontals[i], null, ConstraintSet.CHAIN_PACKED);
            set.applyTo(binding.layout);
        }

        for (int i = 0; i < COLS; i++) {
            set.clone(binding.layout);
            set.createVerticalChain(ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                    verticals[i], null, ConstraintSet.CHAIN_PACKED);
            set.applyTo(binding.layout);
        }
    }
}



