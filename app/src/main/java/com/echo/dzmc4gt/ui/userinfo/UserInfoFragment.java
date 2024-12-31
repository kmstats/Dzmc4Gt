package com.echo.dzmc4gt.ui.userinfo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.echo.dzmc4gt.CustomWebView;
import com.echo.dzmc4gt.MainActivity;
import com.echo.dzmc4gt.R;
import com.echo.dzmc4gt.Utils;
import com.echo.dzmc4gt.databinding.FragmentUserInfoBinding;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class UserInfoFragment extends Fragment implements CustomWebView.OnRightSwipeListener {
    private MainActivity ma;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ma = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        com.echo.dzmc4gt.CustomWebView webView = view.findViewById(R.id.wv_user);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String webData = getUserInfoData(Objects.requireNonNull(ma.getCursorList().getValue()));
        String s = "file:///android_asset";
        webView.loadDataWithBaseURL(s, webData,
                "text/html", "UTF-8", "");
        webView.setOnRightSwipeListener(this);

        return view;
    }

    // 实现接口中的方法
    @Override
    public void onRightSwipe() {
        // 在这里处理右滑逻辑，比如加载下一页或执行其他操作
        ma.setNavControl(R.id.nav_transform);
    }

    /**
     * 取得干部详细信息并导入模板
     *  ‘干部Cursor， 亲属Cursor
     * @return 模板string
     */
    public String getUserInfoData(List<Cursor> cursorList) {
        String res = "";
        try {
            InputStream is = getResources().getAssets().open("files/demo.html");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            res = new String(buffer, StandardCharsets.UTF_8);

            Cursor c = cursorList.get(0);
            if (c != null) {c.moveToFirst();}
            res = res.replace("@name", c.getString(1) != null ? c.getString(1) : "");
            Bitmap b = Utils.byteToBmp(c.getBlob(2));
            if (b != null) {
                b = Utils.compressImage(b);
                res = res.replace("@zp", Utils.imgToBase64(b));
            } else {
                res = res.replace("@zp", "");
            }

            res = res.replace("@sex", c.getString(3 ) != null ? c.getString(3) : "");
            res = res.replace("@birthday",c.getString(4) != null ? c.getString(4): "");
            res = res.replace("@mz", c.getString(5) != null ? c.getString(5) : "");
            res = res.replace("@jg", c.getString(6) != null ? c.getString(6) : "");
            res = res.replace("@csd", c.getString(7) != null ? c.getString(7) : "");
            res = res.replace("@rdsj",c.getString(8) != null ? c.getString(8): "");
            res = res.replace("@cjgzsj",c.getString(9) != null ? c.getString(9): "");
            res = res.replace("@jkzk", c.getString(10) != null ? c.getString(10) : "");
            res = res.replace("@qrzjy", c.getString(11) != null ? c.getString(11) : "");
            res = res.replace("@qrzbyyx", c.getString(12) != null ? c.getString(12) : "");
            res = res.replace("@zzjy", c.getString(13) != null ? c.getString(13) : "");
            res = res.replace("@zzbyyx", c.getString(14) != null ? c.getString(14) : "");
            res = res.replace("@zw", c.getString(15) != null ? c.getString(15) : "");
            res = res.replace("@jl", Utils.getBRString(c.getString(16)));
            res = res.replace("@jcqk", Utils.getBRString(c.getString(17)));
            res = res.replace("@ndkh", Utils.getBRString(c.getString(18)));
            res = res.replace("@dxpx", Utils.getBRString(c.getString(19)));
            res = res.replace("@zjzj", c.getString(20) != null ? c.getString(20) : "");
            // res = res.replace("@rmly", "");

            // 亲属表
            c = cursorList.get(1);
            if (c != null) {
                int i = 1;
                while (c.moveToNext()) {
                    res = res.replace("@cw" + i, c.getString(1) != null ? c.getString(1) : "");
                    res = res.replace("@xm" + i, c.getString(2) != null ? c.getString(2) : "");
                    res = res.replace("@csny" +i, c.getString(3) != null ? c.getString(3): "");
                    res = res.replace("@zzmm" + i, c.getString(4) != null ? c.getString(4) : "");
                    res = res.replace("@gzdw" + i, c.getString(5) != null ? c.getString(5) : "");
                    i++;
                }

                for (int z = c.getCount(); z < 6; z++) {
                    res = res.replace("@cw" + z, "");
                    res = res.replace("@xm" + z, "");
                    res = res.replace("@csny" + z, "");
                    res = res.replace("@zzmm" + z, "");
                    res = res.replace("@gzdw" + z, "");
                }
            }
            is.close();

        } catch (IOException e) {
            Log.e("错误", e.toString());
        }
        return res;
    }

}