package com.iot.xenone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ListItemDetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_detailed);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getItemsFromIntent();
    }

    private void getItemsFromIntent() {
        Intent intent = getIntent();
        String itemTitle = intent.getStringExtra("cords");
        String itemDate = intent.getStringExtra("filling");
        String itemPlace = intent.getStringExtra("export");
        String imageName = intent.getStringExtra("image");

        displayItem(itemTitle, itemDate, itemPlace, imageName);
    }

    private void displayItem(String cords, String filling, String export,  String image) {
        final TextView cordsText = findViewById(R.id.cords);
        final TextView fillingText = findViewById(R.id.filling);
        final TextView exportText = findViewById(R.id.export);
        final ImageView img = findViewById(R.id.img);
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);

        cordsText.append(cords);
        fillingText.append(filling);
        exportText.append(export);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        final Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }
}
