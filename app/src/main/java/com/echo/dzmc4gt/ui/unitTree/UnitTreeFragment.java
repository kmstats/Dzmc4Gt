package com.echo.dzmc4gt.ui.unitTree;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.echo.dzmc4gt.R;

public class UnitTreeFragment extends Fragment {

    private UnitTreeViewModel mViewModel;
    private View view;
    private ExpandableListView expandedListView;
    private View rowView;
    private static Activity activity;

    public UnitTreeFragment(Activity a) {
        activity = a;
    }

    public UnitTreeFragment(){

    }

    public static UnitTreeFragment newInstance() {
        return new UnitTreeFragment(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);

        view = inflater.inflate(R.layout.fragment_unittree, container,false);
        expandedListView = view.findViewById(R.id.expandableListView);
        com.echo.dzmc4gt.ui.unitTree.ExpandableListAdapter adapter = new ExpandableListAdapter(activity,mViewModel.gList,mViewModel.cList);
        expandedListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
    }
}