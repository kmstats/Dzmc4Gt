package com.echo.dzmc4gt.ui.unitTree;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.echo.dzmc4gt.DbHelper;
import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.R;
import com.echo.dzmc4gt.Unit;
import com.echo.dzmc4gt.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnitTreeFragment extends Fragment {

    private MainActivity ma;
    private DbHelper mDbHelper;
    private ExpandableListAdapter adapter;

    public UnitTreeFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ma = (MainActivity)getActivity();
        mDbHelper = ma.getDbHelper();

        UnitTreeViewModel mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
        Cursor cursor = mDbHelper.getUnitByPid(112L);
        while(cursor.moveToNext()){
            Unit u = new Unit(cursor.getString(0),cursor.getLong(1));
            mViewModel.gList.add(u);
            Cursor cc = mDbHelper.getUnitByPid(u.id);
            List<Unit> l = new ArrayList<>();
            while(cc.moveToNext()){
                Unit su = new Unit(cc.getString(0),cc.getLong(1));
                l.add(su);
            }
            mViewModel.cList.add(l);
        }

        View view = inflater.inflate(R.layout.fragment_unittree, container, false);
        ExpandableListView expandedListView = view.findViewById(R.id.expandableListView);
        adapter = new ExpandableListAdapter(ma, mViewModel.gList, mViewModel.cList);
        expandedListView.setAdapter(adapter);

        //处理单位树 子节点被点击事件
        expandedListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // 处理子项的点击事件
            long childID = adapter.getChildId(groupPosition,childPosition);
            ma.setUserList(mDbHelper.getUsersByUnitID(childID));
            return true;
        });

        //设置搜索框
        SearchView searchView = view.findViewById(R.id.text_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<User> u;
                if (s != null) {
                    u = mDbHelper.getUsersByName(s);
                }else{
                    u= mDbHelper.getUsers();
                }
                ma.setUserList(u);
                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && requireActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(Objects.requireNonNull(requireActivity().getCurrentFocus())).getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    ma.setUserList(mDbHelper.getUsers());
                    return true;
                } else {
                    return false;
                }
            }
        });

        return view;
    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
    }*/
}