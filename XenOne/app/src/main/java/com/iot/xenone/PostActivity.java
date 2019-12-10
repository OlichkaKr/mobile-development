package com.iot.xenone;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iot.xenone.extraclasses.Validation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;


public class PostActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;
    private FloatingActionButton uploadImgButton;
    private ImageView enterImage;
    private Button sendData;
    private EditText enterCords;
    private EditText enterFilling;
    private EditText enterExport;
    private RequestParams requestParams;
    private String URL_POST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setupViews();
        uploadImgButton.setOnClickListener(view -> openImage());
        sendData.setOnClickListener(view -> checkData());
    }

    private void setupViews() {
        uploadImgButton = findViewById(R.id.new_image_button);
        sendData = findViewById(R.id.send_data);
        enterCords = findViewById(R.id.enter_cords);
        enterFilling = findViewById(R.id.enter_filling);
        enterExport = findViewById(R.id.enter_export);
        enterImage = findViewById(R.id.new_image);
        initOnRefresh(findViewById(R.id.pullToRefresh));
        URL_POST = getString(R.string.api_url_post);
        requestParams = new RequestParams();
    }

    private void initOnRefresh(SwipeRefreshLayout pullToRefresh) {
        pullToRefresh.setOnRefreshListener(() -> pullToRefresh.setRefreshing(false));
    }

    private void openImage() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            if (imageUri != null) {
                bitmap = convertToBitmap(imageUri);
            }
            requestParams.put("image", convertToBase64(bitmap));
            showProfileImage(bitmap);
        }
    }

    private Bitmap convertToBitmap(Uri imageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void showProfileImage(final Bitmap bitmap) {
        enterImage.setImageBitmap(bitmap);
    }

    private boolean updateCords() {
        if (Validation.validateCords(enterCords)) {
            requestParams.add("cords", enterCords.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean updateFilling() {
        if (Validation.validateFilling(enterFilling)) {
            requestParams.add("filling", enterFilling.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean updateExport() {
        if (Validation.validateExport(enterExport)) {
            requestParams.add("export", enterExport.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    public void checkData() {
        if (updateCords() & updateExport() & updateFilling()) {
            enterCords.getText().clear();
            enterFilling.getText().clear();
            enterExport.getText().clear();
            requestData(URL_POST, requestParams);
        }
    }

    private void requestData(String url, RequestParams requestParams) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                Log.d("Akerbr", "JSON: " + response.toString());
                returnToList();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,
                                  JSONObject response) {
                Toast.makeText(PostActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void returnToList() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
