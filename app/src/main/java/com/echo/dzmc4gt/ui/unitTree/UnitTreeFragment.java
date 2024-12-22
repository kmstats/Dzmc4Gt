package com.echo.dzmc4gt.ui.unitTree;

import androidx.lifecycle.ViewModelProvider;
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
import com.echo.dzmc4gt.ui.transform.TransformViewModel;

import java.util.List;

public class UnitTreeFragment extends Fragment {

    private UnitTreeViewModel mViewModel;
    private View view;
    private ExpandableListView expandedListView;
    private View rowView;
    private  MainActivity ma;
    private TransformViewModel transformViewModel;

    public UnitTreeFragment(){

    }

    public static UnitTreeFragment newInstance() {
        return new UnitTreeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ma = (MainActivity)getActivity();
        mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
        transformViewModel = ma.getTransformViewModel();

        view = inflater.inflate(R.layout.fragment_unittree, container,false);
        expandedListView = view.findViewById(R.id.expandableListView);
        com.echo.dzmc4gt.ui.unitTree.ExpandableListAdapter adapter = new ExpandableListAdapter(ma,mViewModel.gList,mViewModel.cList);
        expandedListView.setAdapter(adapter);

        //设置搜索框
        SearchView searchView = view.findViewById(R.id.text_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<User> u;
                if (s != null) {
                    u = DbHelper.getInstance(ma.getApplicationContext()).getUsersByName(s);
                }else{
                    u= DbHelper.getInstance(ma.getApplicationContext()).getUsers();
                }
                transformViewModel.setUserList(u);
                // 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) ma.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
    }
}