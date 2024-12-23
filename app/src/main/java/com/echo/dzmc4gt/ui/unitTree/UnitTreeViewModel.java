package com.echo.dzmc4gt.ui.unitTree;

import android.database.Cursor;

import androidx.lifecycle.ViewModel;

import com.echo.dzmc4gt.DbHelper;
import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitTreeViewModel extends ViewModel {
    public List<Unit> gList;
    public List<List<Unit>> cList;

    public UnitTreeViewModel(){
        gList = new ArrayList<>();
        cList = new ArrayList<>();

    }

}