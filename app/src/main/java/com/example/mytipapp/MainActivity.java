package com.example.mytipapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CustomTipPercentDialog.Communicator {
    private EditText totalBillAmount;
    private TextView splitNumOfPeople;
    private TextView currentTipView;
    private RadioButton tip15Percent;
    private RadioButton tip18Percent;
    private RadioButton tip20Percent;
    private RadioButton tipOtherPercent;
    private TextView totalBillToBePaid;
    private TextView splitTipsToBePaid;
    private TextView totalTipsToBePaid;
    private float totalBill = 0;
    private float tipPercentValue = 0;
    private int numPeopleSplit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find all the views on app startup
        totalBillAmount = (EditText) findViewById(R.id.bill_total);

        currentTipView = (TextView) findViewById(R.id.currentTip);
        tip15Percent = (RadioButton) findViewById(R.id.radioButton1);
        tip18Percent = (RadioButton) findViewById(R.id.radioButton2);
        tip20Percent = (RadioButton) findViewById(R.id.radioButton3);
        tipOtherPercent = (RadioButton) findViewById(R.id.radioButton4);

        splitNumOfPeople = (TextView) findViewById(R.id.people_split_number);

        totalBillToBePaid = (TextView) findViewById(R.id.total_amount_number);
        splitTipsToBePaid = (TextView) findViewById(R.id.tip_split_amount_number);
        totalTipsToBePaid = (TextView) findViewById(R.id.tip_total_amount_number);
    }

    // Method to update the chosen Tip Percentage
    public void updateTipPercent (View view) {
        String tipString;
        String newTip;
        if (tip15Percent.isChecked()) {
            tipPercentValue = 15;
            tipString = Float.toString(tipPercentValue);
            newTip = tipString + "%";
            Log.v("radioButtons", "15% Tip choice was detected");
            currentTipView.setText(newTip);
        } else if (tip18Percent.isChecked()) {
            tipPercentValue = 18;
            tipString = Float.toString(tipPercentValue);
            newTip = tipString + "%";
            Log.v("radioButtons", "18% Tip choice was detected");
            currentTipView.setText(newTip);
        } else if (tip20Percent.isChecked()) {
            tipPercentValue = 20;
            tipString = Float.toString(tipPercentValue);
            newTip = tipString + "%";
            Log.v("radioButtons", "20% Tip choice was detected");
            currentTipView.setText(newTip);
        } else if (tipOtherPercent.isChecked()) {
            showCustomTipDialog(view);
        }
    }

    // Method to update the number of people splitting the bill
    public void updateBillSplit (View view) {
        String splitNumString = splitNumOfPeople.getText().toString();
        numPeopleSplit = Integer.parseInt(splitNumString);

        if (view.getId() == R.id.minus_split_button & numPeopleSplit >= 2) {
            numPeopleSplit -= 1;
            splitNumString = Integer.toString(numPeopleSplit);
            splitNumOfPeople.setText(splitNumString);
        }

        if (view.getId() == R.id.plus_split_button & numPeopleSplit < 20) {
            numPeopleSplit += 1;
            splitNumString = Integer.toString(numPeopleSplit);
            splitNumOfPeople.setText(splitNumString);
        }
    }

    // Method to bring up the Custom Tip Percentage Dialog Fragment
    public void showCustomTipDialog (View view) {
        FragmentManager manager = getSupportFragmentManager();
        CustomTipPercentDialog tipDialog = new CustomTipPercentDialog();
        tipDialog.show(manager, "CustomTipDialog");
    }

    // Method to calculate the Split and Total Tip Amount, and the Total Bill Amount
    public void calculateSplitTipTotal (View view) {
        // set up the currency formatter
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault(Locale.Category.FORMAT));

        // get the total bill entered
        String totalBillString = totalBillAmount.getText().toString();
        totalBill = Float.parseFloat(totalBillString);
        Log.v("Total Bill Entered", totalBillString);

        // get the number of splits entered
        String numSplits = splitNumOfPeople.getText().toString();
        numPeopleSplit = Integer.parseInt(numSplits);
        Log.v("Number of Splits Entered", numSplits);

        // get the tip percentage
        String tipString = Float.toString(tipPercentValue);
        Log.v("Tip Percent Entered", tipString);
        tipPercentValue = Float.parseFloat(tipString);

        // calculate total tip amount from bill total and tip percentage
        double tipAmount = totalBill*(tipPercentValue/100);
        String output1 = formatter.format(tipAmount);
        Log.v("Tip Amount Calculated", output1);

        // calculate split tip amount
        double splitTipAmount = tipAmount/numPeopleSplit;
        String output2 = formatter.format(splitTipAmount);
        Log.v("Split Tip Amount Calculated", output2);

        // calculate final total bill amount
        String output3;
        if (numPeopleSplit != 1) {
            double finalTotalBill = (totalBill/numPeopleSplit) + splitTipAmount;
            output3 = formatter.format(finalTotalBill);
            Log.v("Final Total Bill Calculated", output3);
        } else {
            double finalTotalBill = totalBill + tipAmount;
            output3 = formatter.format(finalTotalBill);
            Log.v("Final Total Bill Calculated", output3);
        }

        // assign calculated numbers to text views
        splitTipsToBePaid.setText(output2);
        totalTipsToBePaid.setText(output1);
        totalBillToBePaid.setText(output3);
    }

    @Override
    public void onDialogMessage(String message, String tip) {
        if (message.equals("Ok clicked")) {
            tipPercentValue = Integer.parseInt(tip);
            String newTip = tip + "%";
            currentTipView.setText(newTip);
        }
    }
}
