package com.gto.bang.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

public class PersonalActivity extends BaseActivity {


    public final static int NAME_CHANGE=100;
    public final static int SIGNATURE_CHANGE=101;
    public final static int NAME_UPDATE=1;
    public final static int SIGNATRUE_UPDATE=2;
    public final static int UNDO=0;

    private RelativeLayout headRL;
    private RelativeLayout nameRL;
    private RelativeLayout flagRL;
    private RelativeLayout genderRL;
    private RelativeLayout regionRL;
    private RelativeLayout signatureRL;

    private TextView gender;
    private TextView name;
    private TextView head;
    private TextView flag;
    private TextView region;
    private TextView signature;

    String [] genders= new String[]{"男","女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_personal);
//        initViews();
    }


    private void initViews(){
        headRL=(RelativeLayout)findViewById(R.id.info_head_rl);
        nameRL=(RelativeLayout)findViewById(R.id.info_name_rl);
        flagRL=(RelativeLayout)findViewById(R.id.info_flag_rl);
        genderRL=(RelativeLayout)findViewById(R.id.info_gender_rl);
        regionRL=(RelativeLayout)findViewById(R.id.info_region_rl);
        signatureRL=(RelativeLayout)findViewById(R.id.info_signature_rl);

        gender=(TextView) findViewById(R.id.info_gender_tv);
        flag=(TextView) findViewById(R.id.info_flag_tv);
        name=(TextView) findViewById(R.id.info_name_tv);
        region=(TextView) findViewById(R.id.info_region_tv);
        signature=(TextView) findViewById(R.id.info_signature_tv);


        final DialogInterface.OnClickListener  clickListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("sjl",which+":which");
                dialog.dismiss();
                gender.setText(genders[which]);
            }
        };

        genderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                builder.setTitle("性别").setSingleChoiceItems(genders, 0,clickListener).create().show();
            }

        });

        signatureRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalActivity.this,InfoInputActivity.class);
                intent.putExtra("flag",InfoInputActivity.SIGNATURE_FLAG);
                startActivityForResult(intent,SIGNATURE_CHANGE);
            }
        });
        nameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalActivity.this,InfoInputActivity.class);
                intent.putExtra("flag",InfoInputActivity.NAME_FLAG);
                startActivityForResult(intent,NAME_CHANGE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle b;
        switch (resultCode){
            case NAME_UPDATE:
                b=data.getExtras();
                String newName=b.getString("newName");
                this.name.setText(newName);
                break;
            case SIGNATRUE_UPDATE:
                b=data.getExtras();
                String newSignature=b.getString("newSignature");
                this.signature.setText(newSignature);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
