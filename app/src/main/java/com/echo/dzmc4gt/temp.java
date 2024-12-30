/*
package com.echo.dzmc4gt;
// MyFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        // 初始化数据
        data = new ArrayList<>();
        // 向 data 列表中添加一些示例数据
        for (int i = 0; i < 20; i++) {
            data.add("Item " + i);
        }

        // 初始化并设置 Adapter
        adapter = new MyAdapter(data);
        recyclerView.setAdapter(adapter);

        // 默认设置为列表视图
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 你可以在这里添加一个按钮或其他触发条件来切换布局
        // 例如，通过一个按钮点击事件来切换 LayoutManager
        // setupButtonToToggleLayoutManager(view);

        return view;
    }

    // 一个可选的方法，用于设置按钮点击事件来切换布局
    */
/*
    private void setupButtonToToggleLayoutManager(View view) {
        View toggleButton = view.findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLayoutManager();
            }
        });
    }
    *//*


    // 切换 LayoutManager 的方法
    private void toggleLayoutManager() {
        boolean isGridLayout = (recyclerView.getLayoutManager() instanceof GridLayoutManager);

        if (isGridLayout) {
            // 切换到列表视图
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            // 切换到平铺视图，这里假设每行显示 3 个 item
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        // 通知 Adapter 数据集已更改（尽管在这个例子中我们没有更改数据，但有时候可能需要这么做）
        adapter.notifyDataSetChanged();
    }

    // 自定义的 Adapter 类（你需要根据自己的需求来实现这个类）
    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> data;

        public MyAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 创建一个新的 ViewHolder 并绑定到一个新的 View 上
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // 绑定数据到 ViewHolder 上的 View
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text_view); // 假设你的 item 布局中有一个 TextView，其 ID 为 text_view
            }
        }
    }
}


//RecyclerView 如何动态修改item视图为平铺视图、列表视图，在fragment中使用，请简单代码示例*/
