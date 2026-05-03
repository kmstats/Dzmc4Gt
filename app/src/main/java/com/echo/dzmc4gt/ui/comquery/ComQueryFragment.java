package com.echo.dzmc4gt.ui.comquery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.SearchView;

import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.R;

import java.util.ArrayList;

public class ComQueryFragment extends Fragment {
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

        bt_chaxun.setOnClickListener(v -> {
            StringBuilder queryStr = new StringBuilder();
            String temp;
            //处理 性别
            if (cb_nv.isChecked()) { queryStr.append(" and xb='女'"); }
            //处理 民族
            if (cb_shao.isChecked()) { queryStr.append(" and mz<>'汉族'"); }
            //处理 政治面貌
            if (cb_fei.isChecked()) { queryStr.append(" and zzmm not in ('中共党员','预备党员')");}
            //处理 人员身份
            if (cb_shiye.isChecked()) { queryStr.append(" and rybz not in ('公务员','参照公务员')");}

            //处理 选定 职级
            if (cb_zhengchu.isChecked() || cb_fuchu.isChecked() || cb_zhengke.isChecked() || cb_fuke.isChecked() || cb_keyuan.isChecked() || cb_qita.isChecked()){
                ArrayList<String> t = new ArrayList<>();
                if (cb_zhengchu.isChecked()) { t.add("'正处'"); }
                if (cb_fuchu.isChecked()) { t.add("'副处'");  }
                if (cb_zhengke.isChecked()) {t.add("'正科'");}
                if (cb_fuke.isChecked()) {t.add("'副科'");}
                if (cb_keyuan.isChecked()) {t.add("'科员'");}
                if (cb_qita.isChecked()) {t.add("NULL");}

                temp = t.toString();
                temp = temp.replace("[", "(");
                temp = temp.replace("]",")");
                queryStr.append(" and zwjb in ").append(temp);
            }

            //处理 简历 内模糊查询
            if (!sv_jl.getQuery().toString().isEmpty()){
                String[] array = sv_jl.getQuery().toString().split("\\s+");
                temp = " and (";
                for (String s : array) {
                    if (temp.equals(" and (")) {
                        temp = String.format("%s gzjl like '%%%s%%'", temp, s);
                    } else {
                        temp = String.format("%s or gzjl like '%%%s%%'", temp, s);
                    }
                }
                temp = temp + ")";
                queryStr.append(temp);
            }

            //处理 年龄段
            String nl1 = numberPicker1.getDisplayedValues()[numberPicker1.getValue()];
            String nl2 = numberPicker2.getDisplayedValues()[numberPicker2.getValue()];
            temp = String.format(" and (strftime('%%Y.%%m','now')-substr(csny,1,7) %s and strftime('%%Y.%%m','now')-substr(csny,1,7) %s)",
                    nl1,
                    nl2);
            temp = temp.replace("∞","1000");

            queryStr.append(temp);

            if (queryStr.length() > 0) {
                // 注意：这里的SQL查询是拼接的，实际应用中应使用参数化查询
                // 新数据库列：substr(csny,1,7)，照片通过CadreID查tb_CadrePhoto表，职级用allRzsjList
                StringBuilder sql = new StringBuilder(
                        "select xm,mz, " +
                                "substr(csny,1,7)||'('||cast(strftime('%Y.%m', datetime('now'))-substr(csny,1,7) as INTEGER)||')' as csnyStr," +
                                "xrz," +
                                "CASE WHEN allRzsjList IS NOT NULL AND allRzsjList != '' THEN allRzsjList ELSE '' END as zjStr," +
                                "CadreID as _photo_id," +
                                "zwjb," +
                                "CadreID as _id " +
                                "from tb_cadre_node as b left outer join tb_cadre as a on a.CadreID=b.ZwCadreID " +
                                "where CadreID is not null "
                ).append(queryStr).append(" group by cadreid order by dwID, zwOrder");

                Log.d("执行sql：", sql.toString());

                // 执行查询（这里只是模拟，实际应调用数据库查询方法）
                MainActivity ma = (MainActivity)getActivity();
                if (ma != null) {
                    ma.setUserList(ma.getDbHelper().getUserBySQL(sql.toString()));
                }
            }


        });

        bt_chongzhi.setOnClickListener(v -> {
            cb_nv.setChecked(false);
            cb_shao.setChecked(false);
            cb_fei.setChecked(false);
            cb_shiye.setChecked(false);
            numberPicker1.setValue(0);
            numberPicker2.setValue(7);
            cb_zhengchu.setChecked(false);
            cb_fuchu.setChecked(false);
            cb_zhengke.setChecked(false);
            cb_fuke.setChecked(false);
            cb_keyuan.setChecked(false);
            cb_qita.setChecked(false);
            sv_jl.setQuery("",false);

        });


        initNumberPicker(numberPicker1, numberPicker2);

        return view;
    }

    private static void initNumberPicker(NumberPicker numberPicker1, NumberPicker numberPicker2) {
        String[] s1 = new String[]{">=0",">34",">39",">41",">44",">49",">52",">59"};
        String[] s2 = new String[]{"<=35","<=40","<=42","<=45","<=50","<=53","<=60","<=∞"};
        numberPicker1.setDisplayedValues(s1);
        numberPicker2.setDisplayedValues(s2);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(s1.length -1);
        numberPicker1.setValue(0);

        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(s2.length - 1);
        numberPicker2.setValue(7);
    }
}
