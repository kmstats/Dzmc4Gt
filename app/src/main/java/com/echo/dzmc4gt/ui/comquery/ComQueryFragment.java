package com.echo.dzmc4gt.ui.comquery;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

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

        initNumberPicker(numberPicker1, numberPicker2);

        return view;
    }

    private static void initNumberPicker(NumberPicker numberPicker1, NumberPicker numberPicker2) {
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(100);
        numberPicker1.setValue(0);

        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(100);
        numberPicker2.setValue(35);
    }

 /*   @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ComQueryViewModel.class);
    }*/

}