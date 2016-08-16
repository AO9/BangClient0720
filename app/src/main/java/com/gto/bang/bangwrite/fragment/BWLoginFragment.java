package com.gto.bang.bangwrite.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gto.bang.R;

/**
 * Created by shenjialong on 16/6/5.
 */
public class BWLoginFragment extends Fragment {

    public BWLoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=null;

        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        TextView password=(TextView) rootView.findViewById(R.id.f_login_password_et);
        Button save=(Button) rootView.findViewById(R.id.f_login_save_bt);
        final TextView phone=(TextView) rootView.findViewById(R.id.f_login_phone_et);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getActivity(), "保存成功"+phone, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        return rootView;
    }


}
