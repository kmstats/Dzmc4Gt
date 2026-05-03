package com.echo.dzmc4gt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "错误：";
    private static final String dbPath = "/data/data/com.echo.dzmc4gt/databases/";
    private static final String dbName = "gtcadrepad.db";
    private final Context mCtx;
    private SQLiteDatabase mDB;

    // AES解密相关
    private static final String AES_KEY = "71C63F76D8F9E40A";
    private static final byte[] AES_IV = {
            (byte)0x7c, (byte)0x3e, (byte)0x2a, (byte)0x52,
            (byte)0x20, (byte)0x7d, (byte)0x16, (byte)0x4a,
            (byte)0x5c, (byte)0x57, (byte)0x23, (byte)0x35,
            (byte)0x40, (byte)0x61, (byte)0x2b, (byte)0x2f
    };

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
        super(context, dbName, null, 1);
        mCtx = context;
        this.mDB = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "打开...");
        File dbFile = new File(mCtx.getDatabasePath(dbName).getPath());
        if (!dbFile.exists()){
            copyDataBaseFromAssets();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "数据库升级...");
    }

    /**
     * AES解密
     * @param encryptedBase64 Base64加密的字符串
     * @return 解密后的字符串
     */
    public static String decryptAES(String encryptedBase64) {
        if (encryptedBase64 == null || encryptedBase64.isEmpty()) return "";
        try {
            byte[] encrypted = Base64.decode(encryptedBase64, Base64.DEFAULT);
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(AES_IV);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            Log.w(TAG, "AES解密失败: " + e.getMessage());
            return encryptedBase64;
        }
    }

    /**
     * 打开数据库
     */
    public void open(){
        if (mDB == null || !mDB.isOpen()){
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
    private void copyDataBaseFromAssets() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            inputStream = mCtx.getAssets().open("files/" + dbName);
            File outFile = new File(mCtx.getDatabasePath(dbName).getPath());
            outputStream = Files.newOutputStream(outFile.toPath());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        }catch (IOException e){
            Log.e(TAG, e.toString());
        }finally {
            try {
                if (inputStream != null)  inputStream.close();
                if (outputStream != null) outputStream.close();
            }catch(IOException e){
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * 根据CadreID获取照片
     * @param cadreId 干部ID
     * @return 照片字节数组
     */
    public byte[] getPhotoByID(long cadreId) {
        byte[] photo = null;
        Cursor c = mDB.query("tb_CadrePhoto", new String[]{"JpgPhoto"},
                "cadreid=?", new String[]{String.valueOf(cadreId)}, null, null, null);
        if (c != null && c.moveToFirst()) {
            photo = c.getBlob(0);
        }
        if (c != null) c.close();
        return photo;
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
        String selection = COL_UNIT_PID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(pid)};
        String orderBy = "dwLevel,dwOrder";
        return mDB.query(TABLE_UNIT, columns, selection, selectionArgs, null, null, orderBy);
    }

    /**
     * 返回所有人员
     * @return 所有Cursor
     */
    public List<User> getUsers() {
        String[] columns = new String[]{
                COL_GBMC_XM,
                COL_GBMC_MZ,
                "substr(csny,1,7)||'('||cast(strftime('%Y.%m',datetime('now'))-substr(csny,1,7) as INTEGER)||')' as csnyStr",
                COL_GBMC_ZW,
                "CASE WHEN allRzsjList IS NOT NULL AND allRzsjList != '' THEN allRzsjList ELSE '' END as zjStr",
                "a.CadreID as _photo_id",
                COL_GBMC_ZJ,
                "a.CadreID as _id"
        };
        String selection = "b.dwID IS NOT NULL";
        String groupBy = "CadreID";
        String orderBy = "b.zwOrder";
        Cursor cursor = mDB.query("tb_cadre_node as b left outer join tb_cadre as a on a.CadreID=b.ZwCadreID ", columns, selection, null, groupBy, null, orderBy);
        return getListFromCursor(cursor);
    }

    /**
     *  执行固定查询，sql语句
     * @param sql sql语句
     * @return  返回干部列表
     */
    public List<User> getUserBySQL(String sql){
        Cursor cursor = mDB.rawQuery(sql, null);
        return  getListFromCursor(cursor);
    }

    /**
     * 返回指定单位人员
     *
     * @param uid 单位ID
     * @return 指定单位人员Cursor
     */
    public List<User> getUsersByUnitID(long uid) {
        String[] columns = new String[]{
                COL_GBMC_XM,
                COL_GBMC_MZ,
                "substr(csny,1,7)||'('||cast(strftime('%Y.%m',datetime('now'))-substr(csny,1,7) as INTEGER)||')' as csnyStr",
                COL_GBMC_ZW,
                "CASE WHEN allRzsjList IS NOT NULL AND allRzsjList != '' THEN allRzsjList ELSE '' END as zjStr",
                "a.CadreID as _photo_id",
                COL_GBMC_ZJ,
                "a.CadreID as _id"
        };
        String selection = "b.dwID=?";
        String[] selectionArgs = new String[]{String.valueOf(uid)};
        String orderBy = "b.zwOrder";
        Cursor cursor = mDB.query("tb_cadre_node as b left outer join tb_cadre as a on a.CadreID=b.ZwCadreID ", columns, selection, selectionArgs, null, null, orderBy);
        return getListFromCursor(cursor);
    }

    /**
     * 取得干部信息
     *
     * @param id 人员ID
     * @return 人员信息Cursor
     */
    public Cursor getUserInfoByID(long id) {
        String[] columns = new String[]{
                COL_GBMC_ID + " as _id ",
                COL_GBMC_XM,
                "CadreID as photo_cadreid",
                COL_GBMC_XB,
                "substr(csny,1,7)||'('||cast(strftime('%Y.%m',datetime('now'))-substr(csny,1,7) as INTEGER)||')' as csnyStr",
                COL_GBMC_MZ,
                COL_GBMC_JG,
                COL_GBMC_CSD,
                "substr(rdsj,1,7) as rdsjStr",
                "substr(gzsj,1,7) as gzsjStr",
                COL_GBMC_JKZK,
                COL_GBMC_WHCD,
                COL_GBMC_BYYX,
                COL_GBMC_ZZJY,
                COL_GBMC_ZZBYYX,
                COL_GBMC_ZW,
                COL_GBMC_JL,
                COL_GBMC_JCQK,
                COL_GBMC_NDKH,
                "tel as DXPXQK",
                "address as DXPXADDR",
                "CASE WHEN allRzsjList IS NOT NULL AND allRzsjList != '' THEN allRzsjList||'（'||A0192E||'）' ELSE '' END as zjStr"
        };
        String selection = COL_GBMC_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        return mDB.query(TABLE_GBMC, columns, selection, selectionArgs, null, null, null);
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

    /**
     * 根据姓名搜索干部
     * @param name 姓名关键词
     * @return 干部列表
     */
    public List<User> getUsersByName(String name) {
        String[] columns = new String[]{
                COL_GBMC_XM,
                COL_GBMC_MZ,
                "substr(csny,1,7)||'('||cast(strftime('%Y.%m',datetime('now'))-substr(csny,1,7) as INTEGER)||')' as csnyStr",
                COL_GBMC_ZW,
                "CASE WHEN allRzsjList IS NOT NULL AND allRzsjList != '' THEN allRzsjList ELSE '' END as zjStr",
                "CadreID as _photo_id",
                COL_GBMC_ZJ,
                "CadreID as _id"
        };
        String selection = "replace(xm,' ','') like ? or py = ? or py like ?";
        String[] selectionArgs = new String[]{"%" + name + "%", name, name + "%"};
        Cursor cursor = mDB.query(TABLE_GBMC, columns, selection, selectionArgs, null, null, null);
        return getListFromCursor(cursor);
    }

    /**
     * 从Cursor解析用户列表
     * @param cursor 数据库Cursor
     * @return 用户列表
     */
    public List<User> getListFromCursor(Cursor cursor){
        List<User> ul = new ArrayList<>();
        while (cursor.moveToNext()){
            User u = new User();
            u.xm = cursor.getString(0);
            u.mz = cursor.getString(1);
            u.csny = cursor.getString(2);
            u.zw = cursor.getString(3);
            u.zjzj = cursor.getString(4);
            u.photoId = cursor.getString(5);
            u.zj = cursor.getString(6);
            u._id = cursor.getString(7);
            ul.add(u);
        }
        return ul;
    }
}
