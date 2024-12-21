package com.echo.dzmc4gt.ui.unitTree;

import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.echo.dzmc4gt.DbHelper;
import com.echo.dzmc4gt.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitTreeViewModel extends ViewModel {
    private MutableLiveData<List<Unit>> groupUnitList;
    private MutableLiveData<List<List<Unit>>> childUnitList;
    public List<Unit> gList;
    public List<List<Unit>> cList;

    public UnitTreeViewModel(){
        gList = new ArrayList<>();
        cList = new ArrayList<>();

        DbHelper dbHelper = DbHelper.getInstance(null);
        Cursor cursor = dbHelper.getUnitByPid(112L);
        int i = 0;
        while(cursor.moveToNext()){
            Unit u = new Unit(cursor.getString(0),cursor.getLong(1));
            gList.add(u);

            Cursor cc = dbHelper.getUnitByPid(u.id);
            List<Unit> l = new ArrayList<>();
            while(cc.moveToNext()){
                Unit su = new Unit(cc.getString(0),cc.getLong(1));
                l.add(su);
            }
            cList.add(l);
            i++;
        }
    }

}