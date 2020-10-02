package com.example.nimhans_security_check;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EvaluateScanActivity extends AppCompatActivity {

    TextView textView;
    String patientID, currentDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_scan);

        currentDepartment = getIntent().getStringExtra("Department");

        getSupportActionBar().setTitle("NIMHANS " + currentDepartment + " Department");

        textView = (TextView) findViewById(R.id.batch);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        patientID = getIntent().getStringExtra("Patient ID");

        databaseAccess.open();

        String details = databaseAccess.getPatientDetails(patientID);
        Log.d("DEBUG", details);
        String age = details.split("\\s+")[0];
        String department = details.split("\\s+")[1];
        String fromTime = details.split("\\s+")[2];
        String toTime = details.split("\\s+")[3];

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String formattedDate = format.format(c);

        try
        {
            if(!department.equals(currentDepartment))
            {
                View view = this.getWindow().getDecorView();
                view.setBackgroundColor(Color.RED);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
                textView.setText("Patient has to go to: " + department);
            }
            else if(Float.valueOf(age) < 60 && Float.valueOf(age) >= 18 && format.parse(formattedDate).before(format.parse(fromTime)))
            {
                View view = this.getWindow().getDecorView();
                view.setBackgroundColor(Color.RED);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
                textView.setText("Patient is too early");
            }
            else
            {
                View view = this.getWindow().getDecorView();
                view.setBackgroundColor(Color.GREEN);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                textView.setText("The patient may proceed");
            }
        }
        catch (ParseException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        databaseAccess.close();
    }
}
