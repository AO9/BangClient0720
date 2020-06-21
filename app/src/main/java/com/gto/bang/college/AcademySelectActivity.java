package com.gto.bang.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.Constant;

public class AcademySelectActivity extends BaseActivity {

    private TextView bang_college_1_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_college);
        initViews();
    }

    int [] ids=new int[]{
            R.id.bang_college_1_tv,R.id.bang_college_2_tv,R.id.bang_college_3_tv,R.id.bang_college_4_tv
    };

    public void initViews(){

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                Intent intent=new Intent();
                Bundle b=new Bundle();
                Toast t;
                switch(id){
                    case R.id.bang_college_1_tv:
                        b.putString(Constant.ACADEMY,"软件工程");
                        intent.putExtras(b);
                        setResult(20001,intent);
                        finish();
                        break;

                    case R.id.bang_college_2_tv:
                        b.putString(Constant.ACADEMY,"计算机科学与技术");
                        intent.putExtras(b);
                        setResult(20001,intent);
                        finish();
                        break;
                    case R.id.bang_college_3_tv:
                        b.putString(Constant.ACADEMY,"电子信息与技术");
                        intent.putExtras(b);
                        setResult(20001,intent);
                        finish();
                        break;
                    case R.id.bang_college_4_tv:
                        b.putString(Constant.ACADEMY,"其他IT相关专业");
                        intent.putExtras(b);
                        setResult(20001,intent);
                        finish();
                        break;

                }

            }
        };

        for(int i=0;i<ids.length;i++){
            bang_college_1_tv=(TextView)findViewById(ids[i]);
            bang_college_1_tv.setOnClickListener(listener);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
