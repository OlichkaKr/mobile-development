package com.iot.xenone.extraclasses;

import com.google.android.material.textfield.TextInputLayout;

public class Validation {

    private static String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static String PHONE_NUMBER_PATTERN = "[+]380[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";

    public static boolean validateEmail(TextInputLayout emailTextInputLayout) {
        String emailInput = emailTextInputLayout.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            emailTextInputLayout.setError("Field can't be empty");
            return false;
        } else if (!emailInput.matches(EMAIL_PATTERN)) {
            emailTextInputLayout.setError("Email is invalid");
            return false;
        } else {
            emailTextInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validateUsername(TextInputLayout usernameTextInputLayout) {
        String usernameInput = usernameTextInputLayout.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            usernameTextInputLayout.setError("Field can't be empty");
            return false;
        } else {
            usernameTextInputLayout.setError(null);
            return true;
        }
    }

    public static boolean validatePhone(TextInputLayout phoneTextInputLayout) {
        String phoneInput = phoneTextInputLayout.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phoneTextInputLayout.setError("Field can't be empty");
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
            passwordTextInputLayout.setError("Field can't be empty");
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
