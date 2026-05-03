package com.echo.dzmc4gt.ui.unitTree;

import android.content.Context;
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
import com.echo.dzmc4gt.User;

import java.util.List;
import java.util.Objects;

public class UnitTreeFragment extends Fragment {

    private MainActivity ma;
    private DbHelper mDbHelper;
    private ExpandableListAdapter adapter;
    UnitTreeViewModel mViewModel;

    public UnitTreeFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ma = (MainActivity)getActivity();
        if (ma != null) {
            mDbHelper = ma.getDbHelper();
        }
        if (ma != null) {
            mViewModel = ma.getUnitTreeViewModel();
        }

        View view = inflater.inflate(R.layout.fragment_unittree, container, false);
        ExpandableListView expandableListView = view.findViewById(R.id.expandableListView);
        adapter = new ExpandableListAdapter(ma, mViewModel.gList, mViewModel.cList);
        mViewModel.adapter = adapter;                    // 存储adapter引用，用于刷新
        mViewModel.expandableListView = expandableListView;  // 存储listView引用，用于收起组
        expandableListView.setAdapter(adapter);

        //设置分组（部门）点击监听器
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            long groupID = adapter.getGroupId(groupPosition);
            ma.setUserList(mDbHelper.getUsersByUnitID(groupID));
            return false;  // 允许默认展开/折叠行为
        });

        //设置子单位点击监听器
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // 获取点击的子单位ID
            long childID = adapter.getChildId(groupPosition,childPosition);
            ma.setUserList(mDbHelper.getUsersByUnitID(childID));
            return true;
        });

        //搜索功能
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
}
