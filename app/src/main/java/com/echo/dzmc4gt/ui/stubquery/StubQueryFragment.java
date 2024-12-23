package com.echo.dzmc4gt.ui.stubquery;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.R;
import com.echo.dzmc4gt.StubQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class StubQueryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stub_query, container, false);
        StubQueryViewModel mViewModel = new ViewModelProvider(this).get(StubQueryViewModel.class);
        //初始化 固定查询语句列表
        initQueryList(mViewModel);

        ListView listView =view.findViewById(R.id.lv_stubquery);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        StubQueryAdpter adapter = new StubQueryAdpter(getContext(), mViewModel.getStubQuery().getValue());
        listView.setAdapter(adapter);

        // 设置点击事件监听器
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            listView.setItemChecked(position,true);
            StubQuery clickedItem = mViewModel.getStubQuery().getValue().get(position);
            MainActivity ma =  ((MainActivity) getActivity());
            ma.setUserList((ma.getDbHelper()).getUserBySQL(clickedItem.value));
        });

        return view;
    }

    private void initQueryList(StubQueryViewModel mViewModel) {
        //AssetManager assetManager = Objects.requireNonNull(requireActivity()).getAssets();
        try {
            //InputStream inputStream = assetManager.open("file/StubQuery.json");
            InputStream inputStream = getResources().getAssets().open("files/StubQuery.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();
            String jsonString = stringBuilder.toString();

            Gson gson = new GsonBuilder().create();
            Type stubQueryListType = new TypeToken<List< StubQuery>>(){}.getType();
            List<StubQuery> stubQueryList = gson.fromJson(jsonString,stubQueryListType);
            mViewModel.setStubQueryList(stubQueryList);
        }catch (IOException e){
            Log.d(getString(R.string.app_name),e.toString());
        }
    }

    private static class StubQueryAdpter extends BaseAdapter {
        private final List<StubQuery> stubQueryList;
        private final LayoutInflater inflater;

        private StubQueryAdpter(Context context, List<StubQuery> stubQueryList) {
            this.stubQueryList = stubQueryList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return stubQueryList.size();
        }

        @Override
        public StubQuery getItem(int position) {
            return stubQueryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.stub_query_item,parent,false);
                StubQuery stubQuery = stubQueryList.get(position);
                TextView keyTextView = convertView.findViewById(R.id.key_text_view);
                keyTextView.setText(stubQuery.key);
            }
            return convertView;
        }
    }

}