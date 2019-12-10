package com.iot.xenone.extraclasses;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Decorator {

    public static String getInputValue(TextInputLayout inputLayout){
        return inputLayout.getEditText().getText().toString();
    }

    public static String getInputValue(EditText editText){
        return editText.getText().toString();
    }
}
