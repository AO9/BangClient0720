package com.gto.bang.education;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.Constant;

import java.util.HashMap;
import java.util.Map;

public class EducationSelectActivity extends BaseActivity {

    private TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_education);
        initViews();
    }

    int [] ids=new int[]{
            R.id.bang_education_1_tv,R.id.bang_education_2_tv,R.id.bang_education_3_tv,
            R.id.bang_education_4_tv,R.id.bang_education_5_tv,R.id.bang_education_6_tv,R.id.bang_education_7_tv
    };

    String [] educations=new String[]{
            "博士","硕士","在职博士","在职硕士","本科","专科","其他"
    };

    public void initViews(){
        final Map<Integer,String> map=new HashMap<Integer, String>();
        for(int i=0;i<ids.length;i++){
            map.put(ids[i],educations[i]);
        }

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                Intent intent=new Intent();
                Bundle b=new Bundle();
                b.putString(Constant.EDUCATION,map.get(id));
                intent.putExtras(b);
                setResult(20002,intent);
                finish();
            }
        };

        for(int i=0;i<ids.length;i++){
            temp=(TextView)findViewById(ids[i]);
            temp.setOnClickListener(listener);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
