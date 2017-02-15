package com.codepath.todoli;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hkanekal
 */
public class MainActivity extends AppCompatActivity {

    TableRow ttRow[];
    TextView ttCol[];
    private int TableRowIndex;
    DBHelper mydb;
    boolean backfromdisptask = false;
    boolean fontsizechanged = false;
    String stateoftxtsize;
    int ScreenHeight;
    int ScreenWidth;
    int rowHeight;
    int textSize = 14;
    String TAG = "ToDoLi: ";
    final int ROW_HEIGHT = 128;

    public MainActivity() {
        ttCol = new TextView[3];
        ttRow = new TableRow[1000];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show_main();
    }
    private void show_main() {
        final TableLayout taskTableView = (TableLayout)findViewById(R.id.task_table);
        taskTableView.setClickable(true);
        //Log.i(TAG,"in MainActivity");

        int[] screenSize= getScreenSIze();
        ScreenWidth=screenSize[0];
        ScreenHeight=screenSize[1];
        final int tNmWidth = ScreenWidth/3;
        final int tDuWidth = ScreenWidth/3;
        final int tPrWidth = ScreenWidth/3;
        mydb = new DBHelper(this);
        ArrayList TaskList = mydb.getAllTasks();
        ArrayList PrioritiesList = mydb.getAlltPriorities();
        ArrayList DueDateList = mydb.getAlltDue();
        ArrayList StatusList = mydb.getAlltStatus();
        ArrayList TaskTextSize = mydb.getAlltTxtSize();
        boolean TaskDone = false;
        TableRowIndex=mydb.getTotalRows();
        if (TableRowIndex > 0) {
                rowHeight = ScreenHeight / TableRowIndex;
        } else {
                rowHeight = ROW_HEIGHT;
        }
        if (rowHeight > ROW_HEIGHT) rowHeight = ROW_HEIGHT;
        // For all populated rows of the data base
        // get the values and populate the table
        for(int row = 0;row<TableRowIndex;row++){
            ttRow[row] = new TableRow(this);
            String checkdone = StatusList.get(row).toString();
            if(checkdone.equals("Done")) { TaskDone = true; }
            ttCol[0] = new TextView(this);
            ttCol[0].setId(row);
            String TaskName = TaskList.get(row).toString();
            ttCol[0].setTextColor(Color.BLUE);
            ttCol[0].setText(TaskName);
            if (TaskDone) {
                ttCol[0].setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ttCol[0].setTextColor(Color.BLACK);
            }
            ttCol[0].setBackgroundColor(Color.rgb(225, 231, 184));
            ttCol[0].setGravity(Gravity.CENTER_HORIZONTAL);
            ttCol[0].setWidth(tNmWidth);

            ttCol[1] = new TextView(this);
            ttCol[1].setId(row);
            String Due = DueDateList.get(row).toString();
            ttCol[1].setTextColor(Color.BLUE);
            ttCol[1].setText(Due);
            if (TaskDone) {
                ttCol[1].setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ttCol[1].setTextColor(Color.BLACK);
            }
            ttCol[1].setBackgroundColor(Color.rgb(219, 190, 223));
            ttCol[1].setGravity(Gravity.CENTER_HORIZONTAL);
            ttCol[1].setWidth(tDuWidth);

            ttCol[2] = new TextView(this);
            ttCol[2].setId(row);
            String dbPriority = PrioritiesList.get(row).toString();
            ttCol[2].setTextColor(Color.BLUE);
            ttCol[2].setText(dbPriority);
            if (TaskDone) {
                ttCol[2].setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ttCol[2].setTextColor(Color.BLACK);
            }
            if (dbPriority.equals("High")) {
                ttCol[2].setBackgroundColor(Color.rgb(252, 135, 106));
            } else if (dbPriority.equals("Med")) {
                ttCol[2].setBackgroundColor(Color.rgb(179, 252, 106));
            } else if (dbPriority.equals("Low")) {
                ttCol[2].setBackgroundColor(Color.rgb(161, 202, 254));
            }
            if (TaskDone) {
                ttCol[1].setBackgroundColor(Color.rgb(225, 231, 184));
                ttCol[2].setBackgroundColor(Color.rgb(225, 231, 184));
            }
            ttCol[2].setGravity(Gravity.CENTER_HORIZONTAL);
            ttCol[2].setWidth(tPrWidth);
            ttCol[0].setHeight(rowHeight);
            ttCol[1].setHeight(rowHeight);
            ttCol[2].setHeight(rowHeight);
            String txtSze = TaskTextSize.get(row).toString(); // get last updated from db
            textSize = Integer.parseInt(txtSze);
            if(textSize<14) { textSize = 14; }
            ttCol[0].setTextSize(textSize);
            ttCol[1].setTextSize(textSize);
            ttCol[2].setTextSize(textSize);
            if(fontsizechanged) {
                textSize = Integer.parseInt(stateoftxtsize);
                if(textSize<14) { textSize = 14; }
                ttCol[0].setTextSize(textSize);
                ttCol[1].setTextSize(textSize);
                ttCol[2].setTextSize(textSize);
            }
            ttRow[row].addView(ttCol[0]);
            ttRow[row].addView(ttCol[1]);
            ttRow[row].addView(ttCol[2]);
            TaskDone = false;
            taskTableView.addView(ttRow[row]); // display each row as they are populated

            final int dBTaskRow = row+1; // Since Row starts from 0
            // Now look for any key click on a row for updates
            ttRow[row].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    stateoftxtsize = String.valueOf(textSize);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", dBTaskRow);
                    TableRow t = (TableRow) view;
                    TextView taskCol = (TextView) t.getChildAt(0);
                    String taskName = taskCol.getText().toString();
                    dataBundle.putString("item", taskName);
                    dataBundle.putString("tsz",stateoftxtsize);
                    Intent intent = new Intent(getApplicationContext(),DisplayTasks.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }
    }
    private int[] getScreenSIze(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int h = displaymetrics.heightPixels;
        int w = displaymetrics.widthPixels;

        int[] size={w,h};
        return size;

    }
    // Create the action bar menu button for adding task
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // On action bar click of add task kick off display task
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.AddItem:
                fontsizechanged = false;
                backfromdisptask = true; // set so that text size can be maintained
                stateoftxtsize = String.valueOf(textSize);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                dataBundle.putString("tsz",stateoftxtsize);
                backfromdisptask = true; // set so that text size can be maintained
                Intent intent = new Intent(getApplicationContext(),DisplayTasks.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return true;
            case R.id.AlphaInc:
                textSize+=8;
                if (textSize > 48) {
                    textSize = 12;
                }
                if (TableRowIndex > 0) {
                    rowHeight = ScreenHeight / TableRowIndex;
                } else {
                    rowHeight = ROW_HEIGHT;
                }
                if(textSize > (rowHeight/8)) textSize -= 8;
                stateoftxtsize = String.valueOf(textSize);
                fontsizechanged = true;
                onResume();
                return true;
            case R.id.AlphaDec:
                textSize-=8;
                if (textSize < 12) {
                    textSize = 12;
                }
                stateoftxtsize = String.valueOf(textSize);
                fontsizechanged = true;
                onResume();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        if(!fontsizechanged) { backfromdisptask = true; }
        show_main();
    }
}