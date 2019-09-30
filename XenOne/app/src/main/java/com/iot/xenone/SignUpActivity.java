package com.iot.xenone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iot.xenone.extraclasses.Decorator;
import com.iot.xenone.extraclasses.User;
import com.iot.xenone.extraclasses.Validation;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailTextInputLayout = findViewById(R.id.email_text_input);
        phoneTextInputLayout = findViewById(R.id.phone_text_input);
        usernameTextInputLayout = findViewById(R.id.username_text_input);
        passwordTextInputLayout = findViewById(R.id.password_text_input);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    private boolean[] searchPhone(final String phone) {
        final boolean[] result = {true};
        firebaseRef.orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Toast.makeText(SignUpActivity.this, "Sorry but such phone number already used",
                            Toast.LENGTH_SHORT).show();
                    result[0] = false;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return result;
    }

    private void signUp(final String email, final String username, final String password, final String phone) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            writeNewUser(user.getUid(), email, username, password, phone);
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    getResources().getString(R.string.already_used_email),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String userId, String email, String username, String password, String phone) {
        User user = new User(email, username, password, phone);
        firebaseRef.child(userId).setValue(user);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void changeSignUpToWelcomeActivity(View view) {
        if (Validation.validateEmail(emailTextInputLayout) &
                Validation.validatePassword(passwordTextInputLayout) &
                Validation.validatePhone(phoneTextInputLayout) &
                Validation.validateUsername(usernameTextInputLayout)) {
            if (searchPhone(Decorator.getInputValue(phoneTextInputLayout))[0]) {
                signUp(Decorator.getInputValue(emailTextInputLayout),
                        Decorator.getInputValue(usernameTextInputLayout),
                        Decorator.getInputValue(passwordTextInputLayout),
                        Decorator.getInputValue(phoneTextInputLayout));
            }
            if (firebaseAuth.getCurrentUser() != null) {
                writeNewUser(firebaseAuth.getCurrentUser().getUid(),
                        Decorator.getInputValue(emailTextInputLayout),
                        Decorator.getInputValue(usernameTextInputLayout),
                        Decorator.getInputValue(passwordTextInputLayout),
                        Decorator.getInputValue(phoneTextInputLayout));
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void changeSignUpToSignInActivity(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
