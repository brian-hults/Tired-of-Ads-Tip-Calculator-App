package com.example.mytipapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

public class CustomTipPercentDialog extends DialogFragment implements View.OnClickListener {
    Button plusPercent, minusPercent, ok, cancel;
    Communicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (Communicator) context;
    }

    TextView customPercentTip;
    int customPercent;
    String customPercentString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_tip_dialog, null);

        minusPercent = (Button) view.findViewById(R.id.minusPercentButton);
        plusPercent = (Button) view.findViewById(R.id.plusPercentButton);

        ok = (Button) view.findViewById(R.id.dialogOk);
        cancel = (Button) view.findViewById(R.id.dialogCancel);

        customPercentTip = (TextView) view.findViewById(R.id.customPercentText);
        customPercentString = customPercentTip.getText().toString();
        customPercent = Integer.parseInt(customPercentString);

        minusPercent.setOnClickListener(this);
        plusPercent.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        setCancelable(true);
        return view;
    }

    @Override
    public void onClick (View view) {
        if (view.getId() == R.id.minusPercentButton) {
            customPercent -= 1;
            customPercentString = Integer.toString(customPercent);
            Log.v("minus button", customPercentString);
            customPercentTip.setText(customPercentString);
        }

        if (view.getId() == R.id.plusPercentButton) {
            customPercent += 1;
            customPercentString = Integer.toString(customPercent);
            Log.v("plus button", customPercentString);
            customPercentTip.setText(customPercentString);
        }

        if (view.getId() == R.id.dialogOk) {
            communicator.onDialogMessage("Ok clicked", customPercentString);
            dismiss();
        }

        if (view.getId() == R.id.dialogCancel) {
            communicator.onDialogMessage("Cancel clicked", "0");
            dismiss();
        }
    }

    interface Communicator {
        public void onDialogMessage (String message, String tip);
    }
}
