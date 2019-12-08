package com.iot.xenone.extraclasses;

import com.google.android.material.textfield.TextInputLayout;

public class Decorator {

    public static String getInputValue(TextInputLayout inputLayout){
        return inputLayout.getEditText().getText().toString();
    }
}
