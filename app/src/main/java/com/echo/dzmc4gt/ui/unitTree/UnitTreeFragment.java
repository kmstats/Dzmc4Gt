package com.echo.dzmc4gt.ui.unitTree;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.dzmc4gt.R;

public class UnitTreeFragment extends Fragment {

    private UnitTreeViewModel mViewModel;

    public static UnitTreeFragment newInstance() {
        return new UnitTreeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unittree, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
        // TODO: Use the ViewModel
    }

}