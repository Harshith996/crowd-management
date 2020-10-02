package com.example.nimhans_security_check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setTitle("NIMHANS " + getIntent().getStringExtra("Department") + " Department");
    }

    @Override
    public void handleResult(Result result) {
        String patientID = result.getText();
        Intent intent = new Intent(ScanCodeActivity.this, EvaluateScanActivity.class);
        intent.putExtra("Patient ID", patientID);
        intent.putExtra("Department", getIntent().getStringExtra("Department"));
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
