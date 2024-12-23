package com.echo.dzmc4gt.ui.stubquery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.echo.dzmc4gt.StubQuery;

import java.util.ArrayList;
import java.util.List;

public class StubQueryViewModel extends ViewModel {
    private final MutableLiveData<List<StubQuery>> _stubQueryList;

    private StubQueryViewModel() {
        _stubQueryList = new MutableLiveData<>();
        _stubQueryList.setValue(new ArrayList<>());
    }

    public void setStubQueryList(List<StubQuery> _stubQueryList) {
        this._stubQueryList.setValue(_stubQueryList);
    }
    public LiveData<List<StubQuery>> getStubQuery(){
        return this._stubQueryList;
    }
}