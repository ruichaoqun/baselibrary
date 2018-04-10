package com.kapp.library.widget.pictureimage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 * 查看大图数据组装封装类
 * 支持：String（imagesList）、Integer（imageResList）两大类列表，
 * 及单张图片查看（addItem...）
 *
 * 注意：HostUrl为String列表提供头部拼接成完成Url，Integer列表不起作用，如果传入String为完整
 *       连接，则不需要设置此参数
 */
public class PicturePagerInfo implements Parcelable {

    private int currentItem = 0;//指定Position
    private String hostUrl;//请求头部

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    private List<Integer> imageResList;
    private List<String> imagesList;
    private List<String> titleList;

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public void setImagesList(String[] images) {
        this.imagesList = Arrays.asList(images);
    }

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public void setTitleList(String[] titles) {
        this.titleList = Arrays.asList(titles);
    }

    public List<Integer> getImageResList() {
        return imageResList;
    }

    public void setImageResList(List<Integer> imageResList) {
        this.imageResList = imageResList;
    }

    public void setImageResList(Integer[] images) {
        this.imageResList = Arrays.asList(images);
    }

    public void addItemImages(String url){
        if (imagesList == null)
            imagesList = new ArrayList<>();
        imagesList.add(url);
    }

    public void addItemImageRes(int resourcesId){
        if (imageResList == null)
            imageResList = new ArrayList<>();
        imageResList.add(resourcesId);
    }

    public void addItemTitle(String title){
        if (titleList == null)
            titleList = new ArrayList<>();
        titleList.add(title);
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public PicturePagerInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentItem);
        dest.writeString(this.hostUrl);
        dest.writeList(this.imageResList);
        dest.writeStringList(this.imagesList);
        dest.writeStringList(this.titleList);
    }

    protected PicturePagerInfo(Parcel in) {
        this.currentItem = in.readInt();
        this.hostUrl = in.readString();
        this.imageResList = new ArrayList<Integer>();
        in.readList(this.imageResList, List.class.getClassLoader());
        this.imagesList = in.createStringArrayList();
        this.titleList = in.createStringArrayList();
    }

    public static final Creator<PicturePagerInfo> CREATOR = new Creator<PicturePagerInfo>() {
        public PicturePagerInfo createFromParcel(Parcel source) {
            return new PicturePagerInfo(source);
        }

        public PicturePagerInfo[] newArray(int size) {
            return new PicturePagerInfo[size];
        }
    };
}
