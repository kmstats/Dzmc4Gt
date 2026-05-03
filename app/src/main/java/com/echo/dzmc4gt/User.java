package com.echo.dzmc4gt;

public class User {
    public String xm;       //  姓名
    public String mz;       //  民族
    public String csny;     //  出生年月
    public String zw;       //  职务
    public String zjzj;     //  职级（公务员职务职级并行）
    public String photoId;  //  照片ID（CadreID，用于异步查询照片）
    public byte[] photoBytes; // 照片字节数组缓存
    public String zj;       //  职级（职务层次）
    public String _id;      //  ID

    public User(){
    }
}
