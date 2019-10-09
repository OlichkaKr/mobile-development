package com.iot.xenone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iot.xenone.extraclasses.User;

public class WelcomeActivity extends AppCompatActivity {

    private TextView emailTextView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        emailTextView = findViewById(R.id.email_text_input);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            TextView textView = findViewById(R.id.welcome_user);
            textView.setText(currentUser.getDisplayName());
        } else {
            TextView textView = findViewById(R.id.welcome_user);
            textView.setText("Anonymus");
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void signOut(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
