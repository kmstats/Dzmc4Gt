package com.echo.dzmc4gt.ui.transform;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.R;
import com.echo.dzmc4gt.User;
import com.echo.dzmc4gt.Utils;
import com.echo.dzmc4gt.databinding.FragmentTransformBinding;
import com.echo.dzmc4gt.databinding.ItemTransformBinding;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class TransformFragment extends Fragment {

    private FragmentTransformBinding binding;
    public TransformViewModel transformViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //transformViewModel = new ViewModelProvider(this).get(TransformViewModel.class);
        transformViewModel = ((MainActivity)getActivity()).getTransformViewModel();

        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewTransform;
        ListAdapter<User, TransformViewHolder> adapter = new TransformAdapter();
        recyclerView.setAdapter(adapter);
        transformViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            // 执行 adapter 的 submitList 方法
            adapter.submitList(userList);
            // 显示 记录条数
            ((MainActivity)getActivity()).setTvCount("共 " + String.valueOf(userList.size())+" 条记录");
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class TransformAdapter extends ListAdapter<User, TransformViewHolder> {
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

        @NonNull
        @Override
        public TransformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTransformBinding binding = ItemTransformBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TransformViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TransformViewHolder holder, int position) {
            holder.textView.setText(getItem(position).xm);
            Bitmap bmp = Utils.byteToBmp(getItem(position).xp);
            holder.imageView.setImageBitmap(bmp);
            holder.zw_item_transform.setText(getItem(position).zw);
        }
    }

    private static class TransformViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;
        private final TextView zw_item_transform;

        public TransformViewHolder(ItemTransformBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
            zw_item_transform = binding.zwItemTransform;
        }
    }
}