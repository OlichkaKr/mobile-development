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
import com.iot.xenone.extraclasses.User;

public class SignUpActivity extends AppCompatActivity{

    private TextInputLayout emailEditText;
    private TextInputLayout usernameEditText;
    private TextInputLayout phoneEditText;
    private TextInputLayout passwordEditText;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.email_text_input);
        phoneEditText = findViewById(R.id.phone_text_input);
        usernameEditText = findViewById(R.id.username_text_input);
        passwordEditText = findViewById(R.id.password_text_input);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

    }

    private boolean[] searchPhone(final String phone){
        final boolean[] result = {true};
        mDatabase.orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Toast.makeText(SignUpActivity.this, "Sorry but such phone number already used",
                        Toast.LENGTH_SHORT).show();
                    result[0] = false;
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        return result;
    }
    private void signUp(final String email, final String username, final String password, final String phone){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            writeNewUser(user.getUid(), email, username, password, phone);
                            Toast.makeText(SignUpActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Sorry but such email already used",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void writeNewUser(String userId, String email, String username, String password, String phone) {
        User user = new User(email, username, password, phone);

        mDatabase.child(userId).setValue(user);
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

    private boolean validateUsername(){
        String usernameInput = usernameEditText.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()){
            usernameEditText.setError("Field can't be empty");
            return false;
        }
        else {
            usernameEditText.setError(null);
            return true;
        }
    }

    private boolean validatePhone(){
        String phoneInput = phoneEditText.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()){
            phoneEditText.setError("Field can't be empty");
            return false;
        }
        else if (!phoneInput.matches("[+]380[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")){
            phoneEditText.setError("Phone number is invalid");
            return false;
        }
        else {
            phoneEditText.setError(null);
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

    public void changeSignUpToWelcomeActivity(View view){
        if (validateEmail() & validatePassword() & validatePhone() & validateUsername()){
            if (searchPhone(phoneEditText.getEditText().getText().toString())[0]){
                signUp(emailEditText.getEditText().getText().toString(),
                        usernameEditText.getEditText().getText().toString(),
                        passwordEditText.getEditText().getText().toString(),
                        phoneEditText.getEditText().getText().toString());
            }
            if (firebaseAuth.getCurrentUser() != null) {
                writeNewUser(firebaseAuth.getCurrentUser().getUid(),
                        emailEditText.getEditText().getText().toString(),
                        usernameEditText.getEditText().getText().toString(),
                        passwordEditText.getEditText().getText().toString(),
                        phoneEditText.getEditText().getText().toString());
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void changeSignUpToSignInActivity(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
