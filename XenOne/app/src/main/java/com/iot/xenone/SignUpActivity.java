package com.iot.xenone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iot.xenone.extraclasses.Decorator;
import com.iot.xenone.extraclasses.Validation;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout email;
    private TextInputLayout username;
    private TextInputLayout phone;
    private TextInputLayout password;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email_text_input);
        phone = findViewById(R.id.phone_text_input);
        username = findViewById(R.id.username_text_input);
        password = findViewById(R.id.password_text_input);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    private void signUp(final Map<String, String> values) {
        firebaseAuth.createUserWithEmailAndPassword(values.get("email"), values.get("password"))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(values.get("username")).build();
                            user.updateProfile(profileUpdates);
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    getResources().getString(R.string.already_used_email),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void changeSignUpToWelcomeActivity(View view) {
        Map<String, String> values = new HashMap<>();
        values.put("email", Decorator.getInputValue(email));
        values.put("username", Decorator.getInputValue(username));
        values.put("password", Decorator.getInputValue(password));

        if (Validation.validateEmail(email) &
                Validation.validatePassword(password) &
                Validation.validatePhone(phone) &
                Validation.validateUsername(username)) {
            signUp(values);

            if (firebaseAuth.getCurrentUser() != null) {
                Intent intent = new Intent(this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    public void changeSignUpToSignInActivity(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
