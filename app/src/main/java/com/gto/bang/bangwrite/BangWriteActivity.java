package com.gto.bang.bangwrite;

import android.os.Bundle;
import android.view.MenuItem;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

public class BangWriteActivity extends BaseActivity {

//    private Button login;
//    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_login);
//        initViews();
    }

//    public void initViews(){
//
//        login=(Button)findViewById(R.id.bang_write_login_bt);
//        register=(Button)findViewById(R.id.bang_write_register_bt);
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(BangWriteActivity.this, LoginActionActivity.class);
//                startActivity(intent);
//                Toast t = Toast.makeText(BangWriteActivity.this, "登录", Toast.LENGTH_SHORT);
//                t.show();
//            }
//        });
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(BangWriteActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                Toast t = Toast.makeText(BangWriteActivity.this, "注册", Toast.LENGTH_SHORT);
//                t.show();
//            }
//        });
//
//
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
