package com.codepath.todoli;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.codepath.todoli.R.layout.activity_display_tasks;
/**
 * Created by hkanekal
 */
public class DisplayTasks extends Activity {
    //String TAG = "ToDoLi: "; //need for debugging
    private DBHelper mydb ;
    TextView tName ;
    TextView tNotes;
    TextView tPriority;
    TextView tStatus;
    TextView tDue;
    String Task_To_Update = "";
    private int mYear;
    private int mMonth;
    private int mDay;
    private int cYear;
    DatePicker tDuePicker;
    private String Hi;
    private String Med;
    private String Lo;
    final int[] sPriorint = new int[1];
    final int[] sStatint = new int[1];
    final String[] dbPriority= new String[1];
    final String[] dbStatus= new String[1];
    public Spinner mSpinnerPriority;
    public Spinner mSpinnerStatus;
    private boolean mSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_display_tasks);

        tName = (TextView) findViewById(R.id.editTaskName);
        tNotes = (TextView) findViewById(R.id.editTaskNotes);
        tPriority = (TextView) findViewById(R.id.editTaskPriority);
        tStatus = (TextView) findViewById(R.id.editTaskStatus);
        tDue = (TextView) findViewById(R.id.tvTaskDue);
        mSpinnerPriority = (Spinner)findViewById(R.id.sPriority);
        mSpinnerStatus = (Spinner)findViewById(R.id.sTstatus);
        Hi = this.getString(R.string.pHigh);
        Med = this.getString(R.string.pMedium);
        Lo = this.getString(R.string.pLow);
        mydb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            Task_To_Update = extras.getString("item");
            if(Value>0){ // Task has populated information, get it for display
                Cursor rs = mydb.getTask(Task_To_Update);
                rs.moveToFirst();
                String tvnam = rs.getString(rs.getColumnIndex(DBHelper.TASKLIST_COLUMN_TNAME));
                String tvnote = rs.getString(rs.getColumnIndex(DBHelper.TASKLIST_COLUMN_TNOTES));
                String tvprio = rs.getString(rs.getColumnIndex(DBHelper.TASKLIST_COLUMN_TPRIORITY));
                String tvstat = rs.getString(rs.getColumnIndex(DBHelper.TASKLIST_COLUMN_TSTATUS));
                String tvdue = rs.getString(rs.getColumnIndex(DBHelper.TASKLIST_COLUMN_TDUE));
                if (!rs.isClosed())  {
                    rs.close();
                }
                tName.setText(tvnam);
                tName.setFocusable(true);
                tName.setClickable(true);

                tNotes.setText(tvnote);
                tNotes.setFocusable(true);
                tNotes.setClickable(true);

                tPriority.setFocusable(true);
                tPriority.setClickable(true);
                ConvdBPriority2localvariables(tvprio);
                tPriority.setText(dbPriority[0]); // set from database
                getspinnerpriority();

                tStatus.setFocusable(true);
                tStatus.setClickable(true);
                ConvdBStatus2localvariables(tvstat);
                tStatus.setText(dbStatus[0]);// set from database
                getspinnerstatus();

                tDue.setText(tvdue);
                tDue.setFocusable(true);
                tDue.setClickable(true);
                if(tDue.equals("")) { // Check if any blank Due date set
                    tDue.setText(setCurrentDateOnView("1/1/2017","TBD")); // and fix it
                } else { // check that the due date is up to date and/or set it
                    tDue.setText(setCurrentDateOnView(tvdue,tvstat));
                    Calendar calendar = Calendar.getInstance();
                    cYear = calendar.get(Calendar.YEAR);
                    tDuePicker = (DatePicker) findViewById(R.id.datePicker);
                    MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
                    tDuePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                           calendar.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
                }
            } else if (Value == 0) {
                // Entering task details for first time
                tDue.setText(setCurrentDateOnView("1/1/2017","TBD"));
                Calendar calendar = Calendar.getInstance();
                cYear = calendar.get(Calendar.YEAR);
                tDuePicker = (DatePicker) findViewById(R.id.datePicker);
                MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
                tDuePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
                getspinnerpriority();
                getspinnerstatus();
            }
        }
    }
    // Use local variables within this class scope for
    // Priority ans Status for access elsewhere
    public void ConvdBPriority2localvariables(String priority) {
        switch (priority) {
            case "High":
                dbPriority[0] = Hi;
                sPriorint[0] = 0;
                break;
            case "Medium":
                dbPriority[0] = Med;
                sPriorint[0] = 1;
                break;
            case "Low":
                dbPriority[0] = Lo;
                sPriorint[0] = 2;
                break;
            default:
                dbPriority[0] = Hi;
                sPriorint[0] = 0;
                break;
        }
    }
    public void ConvdBStatus2localvariables(String status) {
        switch (status) {
            case "TBD":
                dbStatus[0] = "TBD";
                sStatint[0] = 0;
                break;
            case "Done":
                dbStatus[0] = "Done";
                sStatint[0] = 1;
                break;
            default:
                dbStatus[0] = "TBD";
                sStatint[0] = 0;
                break;
        }
    }
    // Return String for Priority to Store in dBase
    public String ConvSpinnerPriorityInt2String(int sP){
        String Priority;
        switch (sP) {
            case 0:
                Priority = Hi;
                break;
            case 1:
                Priority = Med;
                break;
            default:
                Priority = Lo;
                break;
        }
        return Priority;
    }
    // Return String for Status to Store in dBase
    public String ConvSpinnerStatusInt2String(int sP){
        String Status;
        switch (sP) {
            case 0:
                Status = "TBD";
                break;
            case 1:
                Status = "Done";
                break;
            default:
                Status = "TBD";
                break;
        }
        return Status;
    }
    // Convert Priority spinner position to String
    public void getspinnerpriority(){
        mSpinnerPriority = (Spinner)findViewById(R.id.sPriority);
        mSpinnerPriority.setSelection(sPriorint[0]);
        mSpinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true;
                    tPriority.setText(Hi); // default selection
                    return;
                }
                int pos = mSpinnerPriority.getSelectedItemPosition();
                tPriority.setText(ConvSpinnerPriorityInt2String(pos));
            }

        public void onNothingSelected(AdapterView<?> adapterView) {
            int pos = mSpinnerPriority.getSelectedItemPosition();
            tPriority.setText(ConvSpinnerPriorityInt2String(pos));
            if (!((tPriority.getText()).equals(ConvSpinnerPriorityInt2String(pos))))
                tPriority.setText(ConvSpinnerPriorityInt2String(pos));
        }
    });
    }
    // Convert Status spinner position to String
    public void getspinnerstatus(){
        mSpinnerStatus = (Spinner)findViewById(R.id.sTstatus);
        mSpinnerStatus.setSelection(sStatint[0]);
        mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true;
                    tStatus.setText("TBD"); // default selection
                    return;
                }
                int pos = mSpinnerStatus.getSelectedItemPosition();
                tStatus.setText(ConvSpinnerStatusInt2String(pos));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                int pos = mSpinnerStatus.getSelectedItemPosition();
                tStatus.setText(ConvSpinnerStatusInt2String(pos));
                if (!((tStatus.getText()).equals(ConvSpinnerStatusInt2String(pos))))
                    tStatus.setText(ConvSpinnerStatusInt2String(pos));
            }
        });
    }
    // Get date picker's status change and reflect into due date set
    // Make sure that we check for date in the past and not allow a past date
    public class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            int mon=month+1;
            if(year < cYear) {
                Toast.makeText(getApplicationContext(), "Year Set to Current",
                        Toast.LENGTH_SHORT).show();
                year=cYear;
            }
            tDue.setText(mon+"/"+day+"/"+year);
        }
    }
    // set current date into datepicker
    // Check if any dates previously set are less than todays date
    // if so update them provided they are still to be done
    public String setCurrentDateOnView(String lastSetDueDate, String Stat) {
        boolean duedatenotset = false;
        String[] valueDueDate = new String[3];
        if(lastSetDueDate.isEmpty()) duedatenotset = true;
        if(!duedatenotset) valueDueDate = lastSetDueDate.split("/");
        final Calendar c = Calendar.getInstance();
        DatePicker myDate = (DatePicker) findViewById(R.id.datePicker);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        // set datepicker properly
        if(Stat.equals("TBD")) {
            if ((Integer.parseInt(valueDueDate[0])) >= mYear) {
                mYear = Integer.parseInt(valueDueDate[2]);
            }
            if ((Integer.parseInt(valueDueDate[1])) >= mMonth) {
                mMonth = Integer.parseInt(valueDueDate[0]);
                mMonth-=1; // Month starts at 0
                if(mMonth<0) mMonth=0;
            }
            if ((Integer.parseInt(valueDueDate[2])) >= mDay) {
                mDay = Integer.parseInt(valueDueDate[1]);
            }
        }
        myDate.init(mYear, mMonth, mDay, null );
        String rval="";
        rval= (mMonth+1) +"/"+ mDay +"/"+ mYear;
        return rval;
    }
    // Save item on click of action bar click
    public void SaveItem(View view){
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            Task_To_Update = extras.getString("item");
            if(Value>0){
                if(mydb.updateTasks(Task_To_Update,tName.getText().toString(),
                         tNotes.getText().toString(),tPriority.getText().toString(),
                         tStatus.getText().toString(),tDue.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{
                if(mydb.insertTask(tName.getText().toString(), tNotes.getText().toString(),
                        tPriority.getText().toString(), tStatus.getText().toString(),
                        tDue.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
    // Delete item on click of action bar click
    public void DeleteItem(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("*** Warning ***");
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setMessage(R.string.deleteTask)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mydb.deleteTask(Task_To_Update);
                        Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog d = builder.create();
        d.show();
    }


}