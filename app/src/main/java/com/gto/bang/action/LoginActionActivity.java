package com.gto.bang.action;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

public class LoginActionActivity extends BaseActivity {

    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        initViews();
    }

    public void initViews(){

        login=(Button)findViewById(R.id.f_login_save_bt);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(LoginActionActivity.this, "登录", Toast.LENGTH_SHORT);
                t.show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
