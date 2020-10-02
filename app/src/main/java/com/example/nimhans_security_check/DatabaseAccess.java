package com.example.nimhans_security_check;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;

    Cursor c = null;

    private DatabaseAccess (Context context)
    {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open()
    {
        this.db = openHelper.getWritableDatabase();
    }

    public void close()
    {
        if(db != null)
        {
            this.db.close();
        }
    }

    public String getPatientDetails(String ID)
    {
        c = db.rawQuery(
                "select Patient_Data.Age, Patient_Data.Department_ID, Department.Department_Name, Patient_Data.Batch_ID, Batch.From_Time, Batch.To_Time\n" +
                        "from Patient_Data \n" +
                        "inner join Department, Batch on Patient_Data.Department_ID = Department.Department_ID and Patient_Data.Batch_ID = Batch.Batch_ID where Patient_Data.Patient_ID = '" + ID + "';\n",
                new String[]{}
        );
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext())
        {
            String age = c.getString(0);
            String department = c.getString(2);
            String fromTime = c.getString(4);
            String toTime = c.getString(5);
            buffer.append("").append(age).append(" ").append(department).append(" ").append(fromTime).append(" ").append(toTime);
        }
        return buffer.toString();
    }

    public String getDepartments()
    {
        c = db.rawQuery(
                "select Department_Name from Department",
                new String[]{}
        );
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext())
        {
            String department = c.getString(0);
            buffer.append(department).append(" ");
        }
        return buffer.toString();
    }
}
