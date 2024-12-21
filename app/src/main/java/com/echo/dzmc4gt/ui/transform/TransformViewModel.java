package com.echo.dzmc4gt.ui.transform;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.echo.dzmc4gt.DbHelper;
import com.echo.dzmc4gt.User;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class TransformViewModel extends ViewModel {
    private MutableLiveData<List<User>> userList;

    public TransformViewModel() {
        userList = new MutableLiveData<>();
        List<User> ul = new ArrayList<>();
        DbHelper dbHelper = DbHelper.getInstance(null);
        Cursor cursor = dbHelper.getUsers();
        while (cursor.moveToNext()){
            User u = new User();
            u.xm = cursor.getString(0);
            u.mz = cursor.getString(1);
            u.csny = cursor.getString(2);
            u.zw = cursor.getString(3);
            u.zjzj = cursor.getString(4);
            u.xp = cursor.getBlob(5);
            u.zj = cursor.getString(6);
            u._id = cursor.getString(7);
            ul.add(u);
        }
        userList.setValue(ul);
        cursor.close();
    }

    public LiveData<List<User>> getUserList() {
        return userList;
    }
}