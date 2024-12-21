package com.echo.dzmc4gt.ui.unitTree;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo.dzmc4gt.R;
import com.echo.dzmc4gt.Unit;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity a;
    private List<Unit> gList;
    private List<List<Unit>> cList;

    ExpandableListAdapter(Activity activity, List<Unit> groupList, List<List<Unit>> childList) {
        a = activity;
        gList = groupList;
        cList = childList;
    }

    @Override
    public int getGroupCount() {
        return gList.size();
    }

    @Override
    public int getChildrenCount(int childID) {
        return cList.get(childID).size();
    }

    @Override
    public Unit getGroup(int groupPosition) {
        return gList.get(groupPosition);
    }

    @Override
    public Unit getChild(int groupPosition, int childPosition) {
        return cList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).id;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View converView, ViewGroup parentView) {
        Unit u = gList.get(groupPosition);
        LayoutInflater inflater = a.getLayoutInflater();
        View v = inflater.inflate(R.layout.row_view, null);
        TextView tv = v.findViewById(R.id.tvName);
        tv.setText(" "+u.name);
        ImageView iv = v.findViewById(R.id.imageView1);
        if (isExpanded) {
            iv.setImageResource(R.drawable.ic_up_arrow);
        } else {
            iv.setImageResource(R.drawable.ic_down_arrow);
        }
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View converView, ViewGroup parentView) {
        Unit u = cList.get(groupPosition).get(childPosition);
        TextView v = getTextView();
        v.setText(" "+u.name);
        return v;
    }

    private TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 64);
        TextView textView = new TextView(a);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(14);
        textView.setSingleLine(true);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);

        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
