package com.echo.dzmc4gt.ui.comquery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.SearchView;

import com.echo.dzmc4gt.R;

public class ComQueryFragment extends Fragment {

    private ComQueryViewModel mViewModel;

    public static ComQueryFragment newInstance() {
        return new ComQueryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_com_query, container, false);
        NumberPicker numberPicker1 = view.findViewById(R.id.numberPicker1);
        NumberPicker numberPicker2 = view.findViewById(R.id.numberPicker2);
        CheckBox cb_nv = view.findViewById(R.id.cb_nv);
        CheckBox cb_shao = view.findViewById(R.id.cb_shao);
        CheckBox cb_fei = view.findViewById(R.id.cb_fei);
        CheckBox cb_shiye = view.findViewById(R.id.cb_shiye);
        CheckBox cb_zhengchu = view.findViewById(R.id.cb_zhengchu);
        CheckBox cb_fuchu = view.findViewById(R.id.cb_fuchu);
        CheckBox cb_zhengke = view.findViewById(R.id.cb_zhengke);
        CheckBox cb_fuke = view.findViewById(R.id.cb_fuke);
        CheckBox cb_keyuan = view.findViewById(R.id.cb_keyuan);
        CheckBox cb_qita = view.findViewById(R.id.cb_qita);
        SearchView sv_jl = view.findViewById(R.id.sv_JL);
        Button bt_chongzhi = view.findViewById(R.id.bt_chongzhi);
        Button bt_chaxun = view.findViewById(R.id.bt_chaxun);

        bt_chaxun.setOnClickListener(new View.OnClickListener() {
            String s = "";
            @Override
            public void onClick(View v) {
                if (cb_nv.isChecked()) { s =  " and xb='女'"; }
                if (cb_shao.isChecked()) { s = String.format("%s %s", s, "and mz<>'汉族'"); }
                if (cb_fei.isChecked()) { s = String.format("%s %s", s, "and zzmm not in('中共党员','预备党员')");}
                if (cb_shiye.isChecked()) { s = String.format("%s %s",s,"and rybz not in ('公务员','参照公务员')");}

                if (cb_zhengchu.isChecked() || cb_fuchu.isChecked() || cb_zhengke.isChecked() || cb_fuke.isChecked() || cb_keyuan.isChecked() || cb_qita.isChecked()){
                    String t = "";
                    if (cb_zhengchu.isChecked()) { t = String.format("%s,'%s'",t,"正处");}


                }
                if (cb_zhengchu.isChecked()) {s = String.format("%s %s",s,"and  zwjb='正处'");}
            }
        });


        initNumberPicker(numberPicker1, numberPicker2);

        return view;
    }

    private static void initNumberPicker(NumberPicker numberPicker1, NumberPicker numberPicker2) {
        String[] s1 = new String[]{"0<=","34<","39<","41<","44<","49<","52<","59<"};
        String[] s2 = new String[]{"<=35","<=40","<=42","<=45","<=50","<=53","<=60","<∞"};
        numberPicker1.setDisplayedValues(s1);
        numberPicker2.setDisplayedValues(s2);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(s1.length -1);
        numberPicker1.setValue(0);

        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(s2.length - 1);
        numberPicker2.setValue(7);
    }

 /*   @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ComQueryViewModel.class);
    }*/

}