package com.example.customview2;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ValueSelector extends RelativeLayout {

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    private boolean plusButtonIsPressed = false;
    private boolean minusButtonIsPressed = false;
    private final long REPEAT_INTERVAL_MS = 200l;

    View rootView;
    TextView valueTextView;
    View minusButton;
    View plusButton;


    public ValueSelector(Context context) {
        super(context);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValueSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * Get the current minimum value that is allowed
     *
     * @return
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Set the minimum value that will be allowed
     *
     * @param minValue
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * Get the current maximum value that is allowed
     *
     * @return
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Set the maximum value that will be allowed
     *
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Get the current value
     *
     * @return the current value
     */
    public int getValue() {
        return Integer.valueOf(valueTextView.getText().toString());
    }

    /**
     * Set the current value.  If the passed in value exceeds the current min or max, the value
     * will be set to the respective min/max.
     *
     * @param newValue new value
     */
    public void setValue(int newValue) {
        int value = newValue;
        if(newValue < minValue) {
            value = minValue;
        } else if (newValue > maxValue) {
            value = maxValue;
        }

        valueTextView.setText(String.valueOf(value));
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.value_selector, this);
        valueTextView = (TextView) rootView.findViewById(R.id.valueTextView);

        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue();
            }
        });


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue();
            }
        });

    }

    private void incrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if(currentVal < maxValue) {
            valueTextView.setText(String.valueOf(currentVal + 1));
        }
    }

    private void decrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if(currentVal > minValue) {
            valueTextView.setText(String.valueOf(currentVal - 1));
        }
    }



}