package com.kapp.library.spf;

/**
 * Created by Administrator on 2016/10/28 0028.
 *
 * 基类需要使用的数据
 * 业务逻辑数据请勿写入此类中
 */
public class StoreWession extends BaseSPF {

    private static final String titleFlag = "storeWession";
    private static StoreWession storeWession = null;

    public StoreWession() {
        super(titleFlag);
    }

    public static StoreWession getInstances(){
        if (storeWession == null)
            storeWession = new StoreWession();
        return storeWession;
    }

    /** 写入Token名字定义 */
    public void setKeyName(String keyName){
        writeString("keyName", keyName);
    }

    /** 读取Token名字定义 */
    public String getKeyName(){
        return readString("keyName");
    }

    /** 写入Token值定义 */
    public void setKeyValue(String keyValue){
        writeString("keyValue", keyValue);
    }

    /** 读取Token值定义 */
    public String getKeyValue(){
        return readString("keyValue");
    }
}
