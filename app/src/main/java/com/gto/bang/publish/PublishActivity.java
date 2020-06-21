package com.gto.bang.publish;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

import java.util.Calendar;

public class PublishActivity extends BaseActivity {

    private int mYear;
    private int mMonth;
    private int mDay;
    //flag 用于标示 当前区分设置的是开始时间还是结束时间  0：开始  1:结束
    private int flag=0;

    private TextView end;
    private TextView begin;

    public static int BEGINFLAG=1;
    public static int END_FLAG=2;
    public static String DATESPLIT="-";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initViews();
        Calendar c=Calendar.getInstance();
        mDay=c.get(Calendar.DAY_OF_MONTH);
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        updateDatePicker();
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDay=dayOfMonth;
            mYear=year;
            mMonth=monthOfYear;
            updateDatePicker();
        }

    };

    protected Dialog onCreateDialog(int id){
        switch (id)
        {
            case 0:
                return new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
        }
        return null;
    }


    private void updateDatePicker(){
        if(flag==BEGINFLAG){
            begin.setText(new StringBuilder().append(mYear).append(DATESPLIT).append(mMonth+1).append(DATESPLIT).append(mDay));
        }else if (flag==END_FLAG){
            end.setText(new StringBuilder().append(mYear).append(DATESPLIT).append(mMonth+1).append(DATESPLIT).append(mDay));
        }else{
            begin.setText(new StringBuilder().append(mYear).append(DATESPLIT).append(mMonth+1).append(DATESPLIT).append(mDay));
            end.setText(new StringBuilder().append(mYear).append(DATESPLIT).append(mMonth+1).append(DATESPLIT).append(mDay));
        }
    }

    public void initViews(){

        begin=(TextView)findViewById(R.id.publish_begintime_tv);
        end=(TextView)findViewById(R.id.publish_endtime_tv);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=BEGINFLAG;
                showDialog(0);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=END_FLAG;
                showDialog(0);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
