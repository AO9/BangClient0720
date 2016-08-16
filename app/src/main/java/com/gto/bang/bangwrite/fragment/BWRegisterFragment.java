package com.gto.bang.bangwrite.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gto.bang.R;

/**
 * Created by shenjialong on 16/6/5.
 */
public class BWRegisterFragment extends Fragment {

    public BWRegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=null;

        rootView = inflater.inflate(R.layout.fragment_register, container, false);
//
//        TextView phone=(TextView) rootView.findViewById(R.id.f_register_phone_et);
//        TextView password=(TextView) rootView.findViewById(R.id.f_register_secret_2_et);
//        TextView password2=(TextView) rootView.findViewById(R.id.f_register_secret_et);
//
//        Button save=(Button) rootView.findViewById(R.id.f_register_ok_btn);
//
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast t = Toast.makeText(getActivity(), "确认", Toast.LENGTH_SHORT);
//                t.show();
//            }
//        });
//
        return rootView;
    }


}
