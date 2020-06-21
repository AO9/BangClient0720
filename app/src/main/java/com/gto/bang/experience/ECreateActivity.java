package com.gto.bang.experience;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

public class ECreateActivity extends BaseActivity {

    private TextView content;
    private TextView title;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_experience);
        initViews();
    }

    public void initViews(){

        content=(TextView)findViewById(R.id.experience_content_et);
        title=(TextView)findViewById(R.id.experience_title_et);
        save=(TextView)findViewById(R.id.experience_save_btn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(ECreateActivity.this, "保存成功", Toast.LENGTH_SHORT);
                t.show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
