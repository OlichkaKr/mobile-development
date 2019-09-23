package com.iot.xenone;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.email_text_input);
        passwordEditText = findViewById(R.id.password_text_input);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void signIn(String email, String password) {

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Incorrect email or password",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean validateEmail(){
        String emailInput = emailEditText.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()){
            emailEditText.setError("Field can't be empty");
            return false;
        }
        else if (!emailInput.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            emailEditText.setError("Email is invalid");
            return false;
        }
        else {
            emailEditText.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = passwordEditText.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()){
            passwordEditText.setError("Field can't be empty");
            return false;
        } else if(passwordEditText.getEditText().getText().toString().length() < 8){
            passwordEditText.setError("Minimum 8 characters");
            return false;
        }
        else{
            passwordEditText.setError(null);
            return true;
        }
    }

    public void changeSignInToWelcomeActivity(View view){
        if (validateEmail() && validatePassword()){
            signIn(emailEditText.getEditText().getText().toString(), passwordEditText.getEditText().getText().toString());
            if (firebaseAuth.getCurrentUser() != null){
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }

        }
    }

    public void changeSignInToSignUpActivity(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
