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
        userList.setValue(dbHelper.getUsers());
    }

    public LiveData<List<User>> getUserList() {
        return userList;
    }

    public void setUserList(List<User> users){
        userList.setValue(users);
    }
}