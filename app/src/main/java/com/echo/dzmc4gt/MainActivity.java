package com.echo.dzmc4gt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.dzmc4gt.ui.comquery.ComQueryFragment;
import com.echo.dzmc4gt.ui.stubquery.StubQueryFragment;
import com.echo.dzmc4gt.ui.transform.TransformViewModel;
import com.echo.dzmc4gt.ui.unitTree.UnitTreeFragment;
import com.echo.dzmc4gt.ui.unitTree.UnitTreeViewModel;
import com.echo.dzmc4gt.ui.userinfo.UserInfoViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import com.echo.dzmc4gt.databinding.ActivityMainBinding;

import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 定义权限请求码
    private static final int REQUEST_CODE_PERMISSION = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private DbHelper dbHelper;
    private ViewPager2 viewPager;
    private TransformViewModel transformViewModel;
    private UserInfoViewModel userInfoViewModel;
    private UnitTreeViewModel unitTreeViewModel;

    public static final int TYPE_LIST = 0;
    public static final int TYPE_GRID = 1;
    public int currentViewType = TYPE_GRID; // 默认是列表视图

    ArrayList<String> titles;
    NavHostFragment navHostFragment;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkAndRequestPermissions();

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //设置共享viewMode
        transformViewModel = new ViewModelProvider(this).get(TransformViewModel.class);
        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        unitTreeViewModel = new ViewModelProvider(this).get(UnitTreeViewModel.class);
        //初始化所有干部列表
        transformViewModel.setUserList(dbHelper.getUsers());
        //初始化单位树
        initUnitTree();

/*        //设置工具栏
        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).setAnchorView(R.id.fab).show());
        }*/

        //设置主显示区
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();


        NavigationView navigationView = binding.navView;
        if (navigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    //R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings)
                    R.id.nav_transform, R.id.wv_user)
                    .setOpenableLayout(binding.drawerLayout)
                    .build();
            //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            //设置tabLayout
            TabLayout tableLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);

            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add( new UnitTreeFragment());
            fragments.add(new StubQueryFragment());
            fragments.add(new ComQueryFragment());

            titles = new ArrayList<>();
            titles.add(getString(R.string.unitTree));
            titles.add(getString(R.string.stubQuery));
            titles.add(getString(R.string.comQuery));

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter((this),fragments);
            viewPager.setAdapter(viewPagerAdapter);
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tableLayout, viewPager, (tab, position) -> tab.setText(titles.get(position)));
            tabLayoutMediator.attach();
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TextView tv = findViewById(R.id.tv_TabName);
                tv.setText(titles.get(position));
            }
        });

      /*  BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }*/
        //navController.navigate(R.id.nav_slideshow);
    }

     @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // 处理权限请求的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被用户同意，可以进行文件操作
                performFileOperations();
            } else {
                // 权限被用户拒绝，需要引导用户到设置页面手动开启权限
                // 可以选择引导用户到应用的设置页面
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    //**************** 自己的代码
    // 检查权限并请求
    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        } else {
            // 权限已经被授予，可以进行文件操作
            performFileOperations();
        }
    }


    private void performFileOperations() {
        // 此处可以进行文件读写操作
        Toast.makeText(this, "可以进行文件读写操作", Toast.LENGTH_SHORT).show();
        Log.d("tag", String.valueOf(android.os.Environment.getExternalStorageDirectory()));
        //初始化数据库
        setDatabase();
    }

    /**
     * 初始化数据库
     */
    private void setDatabase() {
        // 数据库存储目录
        this.dbHelper = new DbHelper(this  );
        this.dbHelper.open();
    }

    /**
     * 共享DbHelper
     */
    public DbHelper getDbHelper(){
        return this.dbHelper;
    }

    public NavController getNavController(){return navController;}

    /**
     * 共享toolbar
     */
    public void setTvCount(String s){
        TextView tv = findViewById(R.id.tv_Count);
        tv.setText(s);
    }

    public UnitTreeViewModel getUnitTreeViewModel() {return unitTreeViewModel;}

    public LiveData<List<User>> getUserList() {return transformViewModel.getUserList();}
    public void setUserList(List<User> users){
        transformViewModel.setUserList(users);
    }

    public LiveData<List<Cursor>> getCursorList() {return userInfoViewModel.getCursorList();}

    public void setCursorList(List<Cursor> list) {userInfoViewModel.setCursorList(list);}

    //设置主显示页面的导航    resourceID 为 fragment ID
    public void setNavControl(int resourceID){
        navController.navigate(resourceID);
    }

    private void initUnitTree() {
        if (unitTreeViewModel.gList.isEmpty()) {
            Cursor cursor = dbHelper.getUnitByPid(112L);
            while (cursor.moveToNext()) {
                Unit u = new Unit(cursor.getString(0), cursor.getLong(1));
                unitTreeViewModel.gList.add(u);
                Cursor cc = dbHelper.getUnitByPid(u.id);
                List<Unit> l = new ArrayList<>();
                while (cc.moveToNext()) {
                    Unit su = new Unit(cc.getString(0), cc.getLong(1));
                    l.add(su);
                }
                unitTreeViewModel.cList.add(l);
            }
        }
    }
}