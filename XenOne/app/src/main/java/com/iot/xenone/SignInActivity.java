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
import com.iot.xenone.extraclasses.Decorator;
import com.iot.xenone.extraclasses.Validation;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailTextInputLayout = findViewById(R.id.email_text_input);
        passwordTextInputLayout = findViewById(R.id.password_text_input);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this,
                                    getResources().getString(R.string.email_pass_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void changeSignInToWelcomeActivity(View view) {
        if (Validation.validateEmail(emailTextInputLayout) &&
                Validation.validatePassword(passwordTextInputLayout)) {
            signIn(Decorator.getInputValue(emailTextInputLayout),
                    Decorator.getInputValue(passwordTextInputLayout));
            if (firebaseAuth.getCurrentUser() != null) {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void changeSignInToSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}