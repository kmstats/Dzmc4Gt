package com.echo.dzmc4gt.ui.unitTree;

import android.widget.ExpandableListView;
import androidx.lifecycle.ViewModel;
import com.echo.dzmc4gt.Unit;
import java.util.ArrayList;
import java.util.List;

public class UnitTreeViewModel extends ViewModel {
    public List<Unit> gList;
    public List<List<Unit>> cList;
    public ExpandableListAdapter adapter;          // 用于刷新单位树
    public ExpandableListView expandableListView;  // 用于收起展开的组
    public long currentLevelId = 2456L;            // 当前层级ID，默认科级及以上

    public UnitTreeViewModel(){
        gList = new ArrayList<>();
        cList = new ArrayList<>();
    }
}
