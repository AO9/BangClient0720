package com.gto.bang.personal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.message.MessageActivity;

public class InfoInputActivity extends BaseActivity {

    public final static String NAME_FLAG="name";
    public final static String SIGNATURE_FLAG="signature";



    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_input);
        initViews();
    }


    private void initViews(){
        info=(EditText)findViewById(R.id.info_input_et);
        final ActionBar actionBar = getSupportActionBar();
        Intent intent=getIntent();
        String flag=intent.getStringExtra("flag");
        if(flag!=null){
            if(NAME_FLAG.equals(flag)){
                actionBar.setTitle(getString(R.string.change_name_feild));
            }else{
                actionBar.setTitle(getString(R.string.signatrue_feild));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_save:
                intent = new Intent(this, MessageActivity.class);
                Bundle b=new Bundle();
                String newName=info.getText().toString();

                if(!"".equals(newName)){
                    b.putString("newName","鷥鷥的大恐龙");
                    intent.putExtras(b);
                    setResult(PersonalActivity.NAME_UPDATE,intent);
                }else {
                    setResult(PersonalActivity.UNDO);
                }

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id)
        {
            case 0:
                return null;
        }
        return null;
    }



}
