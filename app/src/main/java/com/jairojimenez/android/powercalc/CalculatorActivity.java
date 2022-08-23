package com.jairojimenez.android.powercalc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;


public class CalculatorActivity extends AppCompatActivity implements
        NumberPickerDialogFragment.NumberPickerDialogHandler {

    /*
       - TARGET_WEIGHT_BUTTON_REFERENCE, BAR_WEIGHT_REFERENCE, PERCENTAGE_REFERENCE
            are references for the NumberPicker OnDialogSet().
       - barWeight, targetWeight, perSide are private values that the user can change when clicking on
            their respectable buttons/area
     */
    private static final int TARGET_WEIGHT_BUTTON_REFERENCE = 0;
    private final int BAR_WEIGHT_REFERENCE = 1;
    private final int PERCENTAGE_REFERENCE = 2;
    private int barWeight;
    private int targetWeight;
    private double percentage = 0;

    /*
        - Layouts to be used in CalculatorActivity
        - All layouts are clickable
        - When a Layout is clicked NumberPicker appears and user enters a value
     */
    LinearLayout mTotalWeightLayout;
    LinearLayout mBarWeightLayout;
    LinearLayout mPercentageLayout;

    //TEXT VIEWS that get updates after users enter values from NumberPicker
    TextView mTotalWeightTextView;
    TextView mBarWeightTextView;
    TextView mPerSideTextView;
    TextView mPercentageValueTextView;
    TextView mPercentageLabelTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_main);

        //Initializing all TextViews and getting their respective xml elements

        mTotalWeightTextView = (TextView) findViewById(R.id.total_weight_value_text_view);
        mBarWeightTextView = (TextView) findViewById(R.id.bar_weight_value_text_view);
        mPerSideTextView = (TextView) findViewById(R.id.per_side_value_text_view);
        mPercentageValueTextView = (TextView) findViewById(R.id.percentage_value_text_view);
        mPercentageLabelTextView = (TextView) findViewById(R.id.percentage_text_view);

        /*
            - Initializing TotalWeightLayout and setting an OnClickListener to the layout
            - When clicked NumberPicker activates and user enter their desired value
         */

        mTotalWeightLayout = (LinearLayout) findViewById(R.id.total_weight_layout);
        mTotalWeightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setReference(TARGET_WEIGHT_BUTTON_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                npb.show();

            }
        });

        /*
            - Initializing mBarWeightLayout and setting an OnClickListener to the layout
            - When clicked NumberPicker activates and user enter their desired value
         */

        mBarWeightLayout = (LinearLayout) findViewById(R.id.bar_weight_layout);
        mBarWeightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setReference(BAR_WEIGHT_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                npb.show();
                updatePercentageWeight(percentage, targetWeight);
            }
        });

        /*
            - Initializing mPercentageWeightLayout and setting an OnClickListener to the layout
            - When clicked NumberPicker activates and user enter their desired value
         */

        mPercentageLayout = (LinearLayout) findViewById(R.id.percentage_weight_layout);
        mPercentageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setReference(PERCENTAGE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                npb.show();
            }
        });

    }

    /*
        - When user enters any data in a NumberPicker from any layout OnDialogNumberSet uses the
            REFERENCE from the layout to activate the case
     */

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        switch (reference) {
            case BAR_WEIGHT_REFERENCE:
                updateBarWeight(number);
                barWeight = number;
                break;
            case PERCENTAGE_REFERENCE:
                percentage = (double) number;
                updatePercentageWeight(percentage, targetWeight);
                break;
            case TARGET_WEIGHT_BUTTON_REFERENCE:
                updateTargetWeight(number);
                targetWeight = number;
                updatePercentageWeight(percentage, targetWeight);
                platesNeededCalculator(number);
                break;
        }
        updatePerSide(targetWeight, barWeight);
    }

    // - Sets the integer a to a String and Updates the mTotalWeightTextView

    public void updateTargetWeight(int a) {

        int BAR_WEIGHT = 45;

        if ( a < BAR_WEIGHT) {
            Context context = getApplicationContext();
            CharSequence text = "Weight must be more than the weight of the bar";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else {

            String totalWeightString = Integer.toString(a);
            mTotalWeightTextView.setText(totalWeightString);
        }
    }

    // - Sets the integer a to a String and Updates the mUpdateBarWeight

    public void updateBarWeight(int a) {

            String barWeightString = Integer.toString(a) + "lbs";
            mBarWeightTextView.setText(barWeightString);

    }

    /*
        - Takes int a and int b and divides it by 2 and it is assigned to perside to get
            the weight on each side
        - perSide then gets parsed to a String perSideString and updates the mPerSideTextView
        - int a is targetWeight and int b is barWeight
        - if a is equal to b mPerSideTextView is set to 0

     */

    public void updatePerSide(int a, int b) {
        int perSide = (a - b) / 2;

        if (a > 45) {
            String perSideString = Integer.toString(perSide);
            mPerSideTextView.setText(perSideString);
        }

        else if(a == b) {
            mPerSideTextView.setText("0");
        }
    }

    /*
        - Takes double a divides it by 100.00 then times it by b and sets it to percentWeight
        - Sets percentWeight to percentWeightString and updates the mPercentValue TextView
     */

    public void updatePercentageWeight(double a, int b) {
        double percentWeight = ((a / 100.00) * b);
        String percentWeightString = Double.toString(percentWeight) + "lbs";
        String st = "% of Weight";
        mPercentageLabelTextView.setText(a + st);
        mPercentageValueTextView.setText(percentWeightString);
    }

    public void platesNeededCalculator(int targetWeight) {

        /*
            - placeholder Text Views that are assigned to ZERO at first then platesNeededcalcultor
                assigns what is needed for each weight
         */
        TextView mforty_fiveLbsPlates_needed = (TextView) findViewById(R.id.forty_fiveLbsPlates_needed);
        TextView mtwentyFive_fiveLbsPlates_needed = (TextView) findViewById(R.id.twentyFiveLbsPlates_needed);
        TextView mtenLbsPlates_needed = (TextView) findViewById(R.id.tenLbsPlates_needed);
        TextView mfiveLbsPlates_needed = (TextView) findViewById(R.id.fiveLbsPlates_needed);
        TextView mtwohalf_fiveLbsPlates_needed = (TextView) findViewById(R.id.two_halfLbsPlates_needed);

        //Array to hold all the TextViews initialized
        TextView textViewArray [] = {mforty_fiveLbsPlates_needed, mtwentyFive_fiveLbsPlates_needed,
                mtenLbsPlates_needed, mfiveLbsPlates_needed, mtwohalf_fiveLbsPlates_needed};

        //For now BAR_WEIGHT will be 45 because it is the basic bar weight for now
        //tmp will
        final int BAR_WEIGHT = 45;
        double tmp;

        //platesArray is an array that has the basic plate weights
        double platesArray [] = {45, 25, 10, 5, 2.5};

        //platesCount will hold the plates needed calculated with the loop below
        int platesCount [] = new int[4];

        //If the targetWeight is less thn the BAR_WEIGHT a toast message will popup and warn the user
        if(targetWeight < BAR_WEIGHT) {
            Toast.makeText(CalculatorActivity.this, "Weight must be >= the bar weight", Toast.LENGTH_SHORT).show();
        }

        //tmp will hold the value of the targetWeight minus BAR_WEIGHT
        //If targetWeight were not subtracted by BAR_WEIGHT you will get false calculations
        tmp = targetWeight - BAR_WEIGHT;

        for(int i = 0; i < platesArray.length; i++) {
            while(tmp / platesArray[i] >= 2) {
                tmp -= (platesArray[i] * 2);
                platesCount[i] += 2;
                int num = platesCount[i]/2;
                textViewArray[i].setText(Integer.toString(num));
            }
        }
    }
}

