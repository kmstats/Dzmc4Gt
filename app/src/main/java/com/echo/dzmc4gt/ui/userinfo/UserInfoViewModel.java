package com.echo.dzmc4gt.ui.userinfo;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserInfoViewModel extends ViewModel {
    // List<Cursor>  第一个为干部cursor，第二个为亲属cursor
    private MutableLiveData<List<Cursor>> cursorList;

    public UserInfoViewModel(){
        cursorList = new MutableLiveData<>();
    }

    public LiveData<List<Cursor>> getCursorList(){
        return cursorList;
    }
    public void setCursorList(List<Cursor> list){
        cursorList.setValue(list);
    }
}