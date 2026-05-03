package com.echo.dzmc4gt.ui.transform;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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


/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class TransformFragment extends Fragment implements  View.OnTouchListener{
    private static MainActivity ma ;
    private FragmentTransformBinding binding;
    private RecyclerView recyclerView;
    private TransformAdapter adapter;
    private ScaleGestureDetector scaleGestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ma = (MainActivity)getActivity();
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //创建手势检测器
        scaleGestureDetector = getScaleGestureDetector();

        recyclerView = binding.recyclerviewTransform;
        adapter = new TransformAdapter();
        recyclerView.setAdapter(adapter);
        ma.getUserList().observe(getViewLifecycleOwner(), userList -> {
            // 执行 adapter 的 submitList 方法
            adapter.submitList(userList);
            ma.setTvCount("共 " + userList.size() +" 条记录");
        });

        root.setOnTouchListener(this);

        setViewType(ma.currentViewType);

        return root;
    }

    private @NonNull ScaleGestureDetector getScaleGestureDetector() {
        return new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(@NonNull ScaleGestureDetector detector) {
                // 获取缩放因子
                float scaleFactor = detector.getScaleFactor();

                // 判断是放大还是缩小，并触发相应事件
                if (scaleFactor > 1.0f) {
                    // 用户进行了放大操作
                    onPinchZoomIn();
                } else if (scaleFactor < 1.0f) {
                    // 用户进行了缩小操作
                    onPinchZoomOut();
                }
                return true; // 表示我们已经处理了这个手势
            }
        });
    }

    private void onPinchZoomOut() {
        // 处理缩小逻辑，比如缩小图片、视图等
        setViewType(MainActivity.TYPE_GRID);
    }

    private void onPinchZoomIn() {
        // 处理放大逻辑，比如放大图片、视图等
        setViewType(MainActivity.TYPE_LIST);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 检查触摸点数量，如果是双指或多指触摸，则进行缩放检测
        if (event.getPointerCount() > 1) {
            // 将触摸事件传递给ScaleGestureDetector
            // 如果ScaleGestureDetector处理了事件（即识别到了缩放手势），则返回true
            return scaleGestureDetector.onTouchEvent(event);
        } else {
            // 如果是单指触摸，不处理缩放，允许事件继续传递（如滑动）
            return false;
        }
    }

    public void setViewType(int viewType){
        ma.currentViewType = viewType;
        //TODO  修改切换视图方法，不再是从当前视图转换另一视图，而应该根据 视图代码，设置视图方式
        boolean isGridLayout = (recyclerView.getLayoutManager() instanceof GridLayoutManager);
        if (viewType == MainActivity.TYPE_LIST && isGridLayout){ // 切换到列表视图
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        if (viewType == MainActivity.TYPE_GRID && !isGridLayout){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        }
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

        @Override
        public int getItemViewType(int position){
            return ma.currentViewType;
        }

        @NonNull
        @Override
        public TransformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == MainActivity.TYPE_LIST) {
                ItemlistTransformBinding listBinding = ItemlistTransformBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new TransformViewHolder(listBinding);
            } else{
                ItemTransformBinding gridBinding = ItemTransformBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new TransformViewHolder(gridBinding);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull TransformViewHolder holder, int position) {
            User user = getItem(position);
            holder.tv_xm.setText(user.xm);
            holder.zw_item_transform.setText(user.zw);
            holder.tv_mz.setText(user.mz);
            holder.tv_csny.setText(user.csny);
            holder.tv_zjzj.setText(user.zjzj);

            holder.userID = user._id;

            // 异步加载照片
            if (user.photoId != null) {
                // 如果已有缓存的照片字节数组，直接使用
                if (user.photoBytes != null && user.photoBytes.length > 0) {
                    Bitmap bmp = Utils.byteToBmp(user.photoBytes);
                    if (bmp != null) {
                        Bitmap compressed = Utils.compressImage(bmp);
                        holder.iv_xp.setImageBitmap(compressed);
                    } else {
                        holder.iv_xp.setImageResource(R.drawable.ic_menu_camera);
                    }
                } else {
                    // 否则异步查询
                    holder.iv_xp.setImageResource(R.drawable.ic_menu_camera);
                    new Thread(() -> {
                        byte[] photoBytes = ma.getDbHelper().getPhotoByID(Long.parseLong(user.photoId));
                        if (photoBytes != null && photoBytes.length > 0) {
                            Bitmap bmp = Utils.byteToBmp(photoBytes);
                            if (bmp != null) {
                                Bitmap compressed = Utils.compressImage(bmp);
                                user.photoBytes = photoBytes;
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    holder.iv_xp.setImageBitmap(compressed);
                                });
                            }
                        }
                    }).start();
                }
            } else {
                holder.iv_xp.setImageResource(R.drawable.ic_menu_camera);
            }

            holder.bind(holder.userID);
        }
    }

    private static class TransformViewHolder extends RecyclerView.ViewHolder{
        private final ImageView iv_xp;
        private final TextView tv_xm;
        private final TextView zw_item_transform;
        private final TextView tv_mz;
        private final TextView tv_csny;
        private final  TextView tv_zjzj;
        public String userID;

        public TransformViewHolder(ItemTransformBinding binding) {
            super(binding.getRoot());
            iv_xp = binding.ivXp;
            tv_xm = binding.tvXm;
            zw_item_transform = binding.tvZw;
            tv_mz = binding.tvMz;
            tv_csny = binding.tvCsny;
            tv_zjzj = binding.tvZjzj;
        }

        public TransformViewHolder(ItemlistTransformBinding binding){
            super(binding.getRoot());
            iv_xp = binding.ivXp;
            tv_xm = binding.tvXm;
            zw_item_transform = binding.tvZw;
            tv_mz = binding.tvMz;
            tv_csny = binding.tvCsny;
            tv_zjzj = binding.tvZjzj;
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

}
