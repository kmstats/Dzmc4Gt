package com.echo.dzmc4gt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DbHelper  extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    private final String dbName;
    private final Context mCtx;
    private SQLiteDatabase mDB = null;

    private static final String TABLE_UNIT = "tb_department";
    private static final String TABLE_GBMC = "tb_cadre";
    private static final String TABLE_RELATE = "tb_family";

    private static final String COL_UNIT_ID = "id";
    private static final String COL_UNIT_NAME = "name";
    private static final String COL_UNIT_PID = "parentID";

    public static String COL_GBMC_ID = "CadreID";
    public static String COL_GBMC_XM = "xm";
    public static String COL_GBMC_ZW = "xrz";        //职务
    public static String COL_GBMC_ZJZJ = "A0192E";
    private static final String COL_GBMC_XB = "xb";
    public static String COL_GBMC_CSNY = "csnyStr";
    private static final String COL_GBMC_RDSJ = "rdsjStr";
    private static final String COL_GBMC_CJGZSJ = "gzsjStr";
    private static final String COL_GBMC_WHCD = "qrzxl";
    private static final String COL_GBMC_BYYX = "qrzxxzy";
    private static final String COL_GBMC_JG = "jgsheng";
    public static String COL_GBMC_MZ = "mz";
    private static final String COL_GBMC_CSD = "cssheng";
    public static String COL_GBMC_ZJ = "zwjb";        //职务级别
    private static final String COL_GBMC_JKZK = "jkzk";
    private static final String COL_GBMC_ZZJY = "zzxl";
    private static final String COL_GBMC_ZZBYYX = "zzxxzy";
    private static final String COL_GBMC_JL = "gzjl";
    public static String COL_GBMC_XP = "photo";
    private static final String COL_GBMC_JCQK = "jcNote";        //奖惩情况
    private static final String COL_GBMC_NDKH = "khNote";        //年度考核
    private static final String COL_GBMC_DXPXQK = "A3801";
    private static final String COL_GBMC_XH = "zwOrder";        //序号

    private static final String COL_RELATE_ID = "CadreID";        //id
    private static final String COL_RELATE_CW = "cw";                //称谓
    private static final String COL_RELATE_XM = "xm";                //姓名
    private static final String COL_RELATE_NL = "cssjStr";            //年龄
    private static final String COL_RELATE_ZZMM = "zzmm";            //政治面貌
    private static final String COL_RELATE_DWZW = "dw";            //单位职务

    public DbHelper(Context context) {
        super(context, context.getString(R.string.DB_NAME), null, 1);
        mCtx = context;
        dbName = context.getString(R.string.DB_NAME);
        this.mDB = this.getReadableDatabase();
    }

/*    public DbHelper getInstance (Context context){
        if (mHelper == null){
            mHelper = new DbHelper(context);
        }
        return mHelper;
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"打开...");
        File dbFile = new File(dbName);
        if (!dbFile.exists()){
            copyDataBase();
        }
        //mHelper =  getInstance(mCtx);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "数据库升级...");
    }

    /**
     * 打开数据库
     */
    public void open(){
        if (mDB == null || !mDB.isOpen()){
            //mDB = mHelper.getWritableDatabase();
            mDB = this.getReadableDatabase();
        }
    }
    public void close(){
        if (mDB != null){
            mDB.close();
            mDB = null;
        }
    }

    /**
     * 从资源复制数据库
     */
    private void copyDataBase() {
        try{
            File file = new File(dbName);
            if (!file.exists()) {
                FileOutputStream fileOutputStream = new FileOutputStream(dbName);
                byte[] buffer = new byte[1024];
                int read;
                InputStream inputStream = mCtx.getAssets().open("files/cadre.db");
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                }
                fileOutputStream.close();
            }
        }catch (IOException e){
            Log.e("错误:",e.toString());
        }
    }


// 以下为数据操作方法
    /**
     * 取得单位byPID
     *
     * @param pid 单位父ID
     * @return 单位Cursor
     */
    public Cursor getUnitByPid(long pid) {
        String[] columns = new String[]{COL_UNIT_NAME, COL_UNIT_ID + " as _id"};
        String selection = COL_UNIT_PID + "=? and DepTag<>2";
        String[] selectionArgs = new String[]{String.valueOf(pid)};
        String orderBy = "dwLevel,dwOrder";
        return mDB.query(TABLE_UNIT, columns, selection, selectionArgs, null, null, orderBy);
    }

    /**
     * 返回所有人员
     * @return 所有Cursor
     */
    public List<User> getUsers() {
        String[] columns = new String[]{COL_GBMC_XM, COL_GBMC_MZ, " csnyStr||'('||cast(strftime('%Y.%m', datetime('now'))-csnyStr as INTEGER)||')' as csnyStr", COL_GBMC_ZW,COL_GBMC_ZJZJ,COL_GBMC_XP, COL_GBMC_ZJ,COL_GBMC_ID + " as _id" };
        String selection = "classid=0";
        String groupBy = "CadreID";
        Cursor cursor= mDB.query("tb_cadre_node as b left outer join tb_Cadre as a on a.CadreID=b.ZwCadreID ", columns, selection, null, groupBy, null, COL_GBMC_XH);
        return getListFromCursor(cursor);
    }

    /**
     *  执行固定查询，sql语句
     * @param sql sql语句
     * @return  返回干部列表
     */
    public List<User> getUserBySQL(String sql){
        Cursor cursor = mDB.rawQuery(sql,null);
        return  getListFromCursor(cursor);
    }

    /**
     * 返回指定单位人员
     *
     * @param uid 单位ID
     * @return 指定单位人员Cursor
     */
    public List<User> getUsersByUnitID(long uid) {
        String[] columns = new String[]{COL_GBMC_XM, COL_GBMC_MZ, " csnyStr||'('||cast(strftime('%Y.%m', datetime('now'))-csnyStr as INTEGER)||')' as csnyStr", COL_GBMC_ZW,COL_GBMC_ZJZJ,COL_GBMC_XP, COL_GBMC_ZJ,COL_GBMC_ID + " as _id" };
        String selection = "classid=0 and b.dwID=?";
        String[] selectionArgs = new String[]{String.valueOf(uid)};
        Cursor cursor = mDB.query("tb_cadre_node as b left outer join tb_Cadre as a on a.CadreID=b.ZwCadreID ", columns, selection, selectionArgs, null, null, COL_GBMC_XH);
        return getListFromCursor(cursor);
    }


    /**
     * 取得干部信息
     *
     * @param id 人员ID
     * @return 人员信息Cursor
     */
    public List<User> getUserInfoByID(long id) {
        String[] columns = new String[]{COL_GBMC_ID + " as _id ", COL_GBMC_XM, COL_GBMC_XP, COL_GBMC_XB, " csnyStr||'('||cast(strftime('%Y.%m', datetime('now'))-csnyStr as INTEGER)||')' as csnyStr", COL_GBMC_MZ, COL_GBMC_JG, COL_GBMC_CSD, COL_GBMC_RDSJ, COL_GBMC_CJGZSJ, COL_GBMC_JKZK, COL_GBMC_WHCD, COL_GBMC_BYYX, COL_GBMC_ZZJY, COL_GBMC_ZZBYYX, COL_GBMC_ZW, COL_GBMC_JL, COL_GBMC_JCQK, COL_GBMC_NDKH, COL_GBMC_DXPXQK, COL_GBMC_ZJZJ};
        String selection = COL_GBMC_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = mDB.query(TABLE_GBMC, columns, selection, selectionArgs, null, null, null);
        return getListFromCursor(cursor);
    }

    /**
     * 取得亲属信息
     *
     * @param id 干部ID
     * @return 亲属 Cursor
     */
    public Cursor getRelateById(long id) {
        String[] columns = new String[]{COL_RELATE_ID + " as _id ", COL_RELATE_CW, COL_RELATE_XM, COL_RELATE_NL, COL_RELATE_ZZMM, COL_RELATE_DWZW};
        String selection = COL_RELATE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        return mDB.query(TABLE_RELATE, columns, selection, selectionArgs, null, null, null);
    }

    public List<User> getUsersByName(String name) {
        String[] columns = new String[]{COL_GBMC_XM, COL_GBMC_MZ, COL_GBMC_CSNY, COL_GBMC_ZW, COL_GBMC_ZJZJ, COL_GBMC_XP, COL_GBMC_ZJ, COL_GBMC_ID + " as _id "};
        String selection = COL_GBMC_XM + " like ? or py = ?";
        String[] selectionArgs = new String[]{"%" + name + "%", name};
        Cursor cursor = mDB.query(TABLE_GBMC, columns, selection, selectionArgs, null, null, null);
        return getListFromCursor(cursor);
    }

    public List<User> getListFromCursor(Cursor cursor){
        List<User> ul = new ArrayList<>();
        while (cursor.moveToNext()){
            User u = new User();
            u.xm = cursor.getString(0);
            u.mz = cursor.getString(1);
            u.csny = cursor.getString(2);
            u.zw = cursor.getString(3);
            u.zjzj = cursor.getString(4);
            u.xp = cursor.getBlob(5);
            u.zj = cursor.getString(6);
            u._id = cursor.getString(7);
            ul.add(u);
        }
        return ul;
    }
}


