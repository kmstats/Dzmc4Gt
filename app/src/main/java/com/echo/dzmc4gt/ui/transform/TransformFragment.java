package com.echo.dzmc4gt.ui.transform;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.R;
import com.echo.dzmc4gt.User;
import com.echo.dzmc4gt.Utils;
import com.echo.dzmc4gt.databinding.FragmentTransformBinding;
import com.echo.dzmc4gt.databinding.ItemTransformBinding;
import com.echo.dzmc4gt.databinding.ItemlistTransformBinding;

import java.util.ArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class TransformFragment extends Fragment {
    private static MainActivity ma ;
    private FragmentTransformBinding binding;
    private RecyclerView recyclerView;
    private ListAdapter<User, TransformViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ma = (MainActivity)getActivity();
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerviewTransform;
        adapter = new TransformAdapter();
        recyclerView.setAdapter(adapter);
        ma.getUserList().observe(getViewLifecycleOwner(), userList -> {
            // 执行 adapter 的 submitList 方法
            adapter.submitList(userList);
            ma.setTvCount("共 " + userList.size() +" 条记录");
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class TransformAdapter extends ListAdapter<User, TransformViewHolder> {
        private final int TYPE_LAYOUT_ONE=1;
        private final int TYPE_LAYOUT_TWO=2;

        protected TransformAdapter() {
            super(new DiffUtil.ItemCallback<User>() {
                @Override
                public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem._id.equals(newItem._id);
                }
            });
        }

        @Override
        public int getItemViewType(int position){
            // 根据数据或位置返回不同的视图类型
            if (1==1) {
                return TYPE_LAYOUT_ONE; // 常量，代表第一种布局
            } else {
                return TYPE_LAYOUT_TWO; // 常量，代表第二种布局
            }
        }

        @NonNull
        @Override
        public TransformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTransformBinding binding1;
            ItemlistTransformBinding binding2;
            if (viewType == TYPE_LAYOUT_ONE) {
                binding1 = ItemTransformBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new TransformViewHolder(binding1);
            }else{
                binding2 = ItemlistTransformBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new TransformViewHolder(binding2);
            }

            //TODO  需要增加列表模式绑定
        }

        @Override
        public void onBindViewHolder(@NonNull TransformViewHolder holder, int position) {
            holder.textView.setText(getItem(position).xm);
            Bitmap bmp = Utils.byteToBmp(getItem(position).xp);
            holder.imageView.setImageBitmap(bmp);
            holder.zw_item_transform.setText(getItem(position).zw);
            holder.userID = getItem(position)._id;
            holder.bind(holder.userID);
        }

    }

    private static class TransformViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final TextView textView;
        private final TextView zw_item_transform;
        public String userID;

        public TransformViewHolder(ItemTransformBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
            zw_item_transform = binding.zwItemTransform;
        }

        public TransformViewHolder(ItemlistTransformBinding binding){
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
            zw_item_transform = binding.zwItemTransform;
        }

        public void bind(String userID) {
            itemView.setOnClickListener(v -> {
                ArrayList<Cursor> list = new ArrayList<>();
                Cursor cursor1 = ma.getDbHelper().getUserInfoByID(Long.parseLong(userID));
                Cursor cursor2 = ma.getDbHelper().getRelateById(Long.parseLong(userID));
                list.add(cursor1);
                list.add(cursor2);
                ma.setCursorList(list);
                ma.setNavControl(R.id.nav_userinfo);
            });
        }
    }

    // 定义一个方法来切换 LayoutManager
    @SuppressLint("NotifyDataSetChanged")
    private void toggleLayoutManager() {
        boolean isGridLayout = (recyclerView.getLayoutManager() instanceof GridLayoutManager);

        if (isGridLayout) {
            // 切换到列表视图
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            // 切换到平铺视图，这里假设每行显示 3 个 item
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        }
        // 可选：通知 Adapter 数据集已更改（尽管在这个例子中我们没有更改数据）
        //adapter.notifyDataSetChanged();
    }

}