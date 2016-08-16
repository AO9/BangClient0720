package com.gto.bang.question;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

public class QCreateActivity extends BaseActivity {

    private TextView question_theme;
    private TextView question_describe;
    private Button question_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_question);
        initViews();
    }

    public void initViews(){

        question_describe=(TextView)findViewById(R.id.question_describe_et);
        question_theme=(TextView)findViewById(R.id.question_theme_et);
        question_ok=(Button) findViewById(R.id.question_ok_btn);

        question_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(QCreateActivity.this, "保存成功", Toast.LENGTH_SHORT);
                t.show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
