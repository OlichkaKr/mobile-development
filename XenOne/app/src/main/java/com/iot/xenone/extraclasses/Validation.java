package com.iot.xenone.extraclasses;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Validation {

    private static String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static String PHONE_NUMBER_PATTERN = "[+]380[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";

    public static boolean validateEmail(TextInputLayout emailTextInputLayout) {
        String emailInput = emailTextInputLayout.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            emailTextInputLayout.setError("Field can not be empty");
            return false;
        } else if (!emailInput.matches(EMAIL_PATTERN)) {
            emailTextInputLayout.setError("Email is invalid");
            return false;
        } else {
            emailTextInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validateEmail(EditText emailEditText) {
        String emailInput = emailEditText.getText().toString().trim();
        if (emailInput.isEmpty()) {
            emailEditText.setError("Field can not be empty");
            return false;
        } else if (!emailInput.matches(EMAIL_PATTERN)) {
            emailEditText.setError("Email is invalid");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }

    public static boolean validateUsername(TextInputLayout usernameTextInputLayout) {
        String usernameInput = usernameTextInputLayout.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            usernameTextInputLayout.setError("Field can not be empty");
            return false;
        } else {
            usernameTextInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validateUsername(EditText usernameEditText) {
        String usernameInput = usernameEditText.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            usernameEditText.setError("Field can not be empty");
            return false;
        } else {
            usernameEditText.setError(null);
            return true;
        }
    }

    public static boolean validatePhone(TextInputLayout phoneTextInputLayout) {
        String phoneInput = phoneTextInputLayout.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phoneTextInputLayout.setError("Field can not be empty");
            return false;
        } else if (!phoneInput.matches(PHONE_NUMBER_PATTERN)) {
            phoneTextInputLayout.setError("Phone number is invalid");
            return false;
        } else {
            phoneTextInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validatePassword(TextInputLayout passwordTextInputLayout) {
        String passwordInput = passwordTextInputLayout.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            passwordTextInputLayout.setError("Field can not be empty");
            return false;
        } else if (passwordTextInputLayout.getEditText().getText().toString().length() < 8) {
            passwordTextInputLayout.setError("Minimum 8 characters");
            return false;
        } else {
            passwordTextInputLayout.setError(null);
            return true;
        }
    }
}
