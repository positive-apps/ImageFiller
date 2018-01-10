package com.positive_apps.imagefiller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.positive.image_filler.ImageFiller;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI:
    private ImageButton verticalBatteryBtn;
    private ImageButton horizontalBatteryBtn;
    private ImageButton mapBatteryBtn;
    private ImageButton verticalIndicatorBtn;
    private ImageButton horizontalIndicatorBtn;
    private EditText percentageEdtTxt;
    private EditText gradientEdtTxt;
    private CheckBox animateCheckBox;
    private RadioGroup directionRadioGroup;
    private RadioGroup durationRadioGroup;
    private ImageView previewImgVw;
    private Button goBtn;

    // Global:
    int currentEmptyResId;
    int currentFullResId;
    int currentGradientResId;

    // -------------------------------------Life Cycle Methods--------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all views:
        getViews();

        // Set listeners:
        setListeners();
    }

    // ------------------------------------View.OnClickListener-------------------------------------

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.verticalBatteryBtn:
                previewImgVw.setImageResource(R.drawable.battery_large);
                currentEmptyResId = R.drawable.battery_large;
                currentFullResId = R.drawable.battery_large_full;
                currentGradientResId = R.drawable.battery_large_red;
                break;

            case R.id.horizontalBatteryBtn:
                previewImgVw.setImageResource(R.drawable.battery_horiz);
                currentEmptyResId = R.drawable.battery_horiz;
                currentFullResId = R.drawable.battery_horiz_full;
                currentGradientResId = R.drawable.battery_horiz_red;
                break;

            case R.id.mapBatteryBtn:
                previewImgVw.setImageResource(R.drawable.battery_map);
                currentEmptyResId = R.drawable.battery_map;
                currentFullResId = R.drawable.battery_map_full;
                currentGradientResId = R.drawable.battery_map_red;
                break;

            case R.id.verticalIndicatorBtn:
                previewImgVw.setImageResource(R.drawable.indicator_large);
                currentEmptyResId = R.drawable.indicator_large;
                currentFullResId = R.drawable.indicator_large_full;
                currentGradientResId = R.drawable.indicator_large_red;
                break;

            case R.id.horizontalIndicatorBtn:
                previewImgVw.setImageResource(R.drawable.indicator_map);
                currentEmptyResId = R.drawable.indicator_map;
                currentFullResId = R.drawable.indicator_map_full;
                currentGradientResId = R.drawable.indicator_map_red;
                break;

            case R.id.goBtn:
                doGo();
                break;
        }

    }

    // ---------------------------------------Helper Methods----------------------------------------

    private void getViews() {
        verticalBatteryBtn = (ImageButton) findViewById(R.id.verticalBatteryBtn);
        horizontalBatteryBtn = (ImageButton) findViewById(R.id.horizontalBatteryBtn);
        mapBatteryBtn = (ImageButton) findViewById(R.id.mapBatteryBtn);
        verticalIndicatorBtn = (ImageButton) findViewById(R.id.verticalIndicatorBtn);
        horizontalIndicatorBtn = (ImageButton) findViewById(R.id.horizontalIndicatorBtn);
        percentageEdtTxt = (EditText) findViewById(R.id.percentageEdtTxt);
        gradientEdtTxt = (EditText) findViewById(R.id.gradientEdtTxt);
        animateCheckBox = (CheckBox) findViewById(R.id.animateCheckBox);
        directionRadioGroup = (RadioGroup) findViewById(R.id.directionRadioGroup);
        durationRadioGroup = (RadioGroup) findViewById(R.id.durationRadioGroup);
        previewImgVw = (ImageView) findViewById(R.id.previewImgVw);
        goBtn = (Button) findViewById(R.id.goBtn);
    }

    private void setListeners() {
        verticalBatteryBtn.setOnClickListener(this);
        horizontalBatteryBtn.setOnClickListener(this);
        mapBatteryBtn.setOnClickListener(this);
        verticalIndicatorBtn.setOnClickListener(this);
        horizontalIndicatorBtn.setOnClickListener(this);

        goBtn.setOnClickListener(this);
    }

    private void doGo() {

        // Get the percentage:
        int percentage;
        if (percentageEdtTxt.getText().toString().length() > 0) {
            percentage = Integer.parseInt(percentageEdtTxt.getText().toString());
            if (percentage <= 0 || percentage > 100) {
                percentage = 100;
            }
        } else {
            percentage = 100;
        }

        // Get the gradient percentage:
        int gradientPercentage;
        if (gradientEdtTxt.getText().toString().length() > 0) {
            gradientPercentage = Integer.parseInt(gradientEdtTxt.getText().toString());
            if (gradientPercentage <= 0 || gradientPercentage > 100) {
                gradientPercentage = 0;
            }
        } else {
            gradientPercentage = 0;
        }

        // Get the direction:
        int direction = 0;
        int checkedDirectionRadioBtnId = directionRadioGroup.getCheckedRadioButtonId();
        switch (checkedDirectionRadioBtnId) {
            case R.id.t2bRB:
                direction = ImageFiller.FILL_TOP_TO_BOTTOM;
                break;
            case R.id.b2tRB:
                direction = ImageFiller.FILL_BOTTOM_TO_TOP;
                break;
            case R.id.r2lRB:
                direction = ImageFiller.FILL_RIGHT_TO_LEFT;
                break;
            case R.id.l2rRB:
                direction = ImageFiller.FILL_LEFT_TO_RIGHT;
                break;
        }

        // Duration:
        int duration = 0;
        int checkedDurationRadioBtnId = durationRadioGroup.getCheckedRadioButtonId();
        switch (checkedDurationRadioBtnId) {
            case R.id.vsRB:
                duration = ImageFiller.DURATION_VERY_SLOW;
                break;
            case R.id.sRB:
                duration = ImageFiller.DURATION_SLOW;
                break;
            case R.id.mRB:
                duration = ImageFiller.DURATION_MEDIUM;
                break;
            case R.id.fRB:
                duration = ImageFiller.DURATION_FAST;
                break;
            case R.id.vfRB:
                duration = ImageFiller.DURATION_VERY_FAST;
                break;
        }

        // Create a new ImageFiller:
        ImageFiller filler = new ImageFiller();

        // Continue Only if an image was selected in the first place:
        if (currentEmptyResId != 0 || currentFullResId != 0 || currentGradientResId != 0) {

            filler.with(this)
                    .onHolder(previewImgVw)
                    .toPercentage(percentage)
                    .emptyVersionResId(currentEmptyResId)
                    .fullVersionResId(currentFullResId)
                    .gradientVersionResId(currentGradientResId)
                    .gradientPercentage(gradientPercentage)
                    .direction(direction)
                    .animate(animateCheckBox.isChecked())
                    .duration(duration)
                    .go();

        } else {
            Toast.makeText(this, "You need to select an image", Toast.LENGTH_SHORT).show();
        }
    }
}
