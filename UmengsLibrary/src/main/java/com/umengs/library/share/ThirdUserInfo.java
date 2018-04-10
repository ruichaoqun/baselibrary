package com.umengs.library.share;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class ThirdUserInfo {
    private String nickName="";
    private String id;
    private String headUrl="";
    private String gender="";
    private String unionid="";

    public ThirdUserInfo(String nickName, String id, String headUrl, String gender,String unionid) {
        this.nickName = nickName;
        this.id = id;
        this.headUrl = headUrl;
        this.gender = gender;
        this.unionid = unionid;
    }

    public ThirdUserInfo() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
