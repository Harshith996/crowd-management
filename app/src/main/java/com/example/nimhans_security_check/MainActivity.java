package com.example.nimhans_security_check;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseAccess databaseAccess;
    private String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        department = "Screening";

        databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        getSupportActionBar().setTitle("NIMHANS " + department + " Department");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);
            }
        }
    }

    public void btn_start_scan(View view) {

        Intent intent = new Intent(MainActivity.this, ScanCodeActivity.class);
        intent.putExtra("Department", department);
        startActivity(intent);

    }

    public void btn_select_department(View view) {

        databaseAccess.open();
        final String[] departments = databaseAccess.getDepartments().split("\\s+");
        for (int i=0; i<departments.length; i++) {
            if (departments[i].equals(department)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Department");
                builder.setSingleChoiceItems(departments, i, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        department = departments[i];
                        getSupportActionBar().setTitle("NIMHANS " + department + " Department");
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, department, Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                databaseAccess.close();
                break;
            }
        }
    }
}
