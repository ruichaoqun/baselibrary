package com.kapp.library.api.http.download;

import android.text.TextUtils;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.request.FileDownloadRequest;
import com.duowan.mobile.netroid.toolbox.FileDownloader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 * 文件下载
 */

public class DownloadManager {

    private LinkedList<DownloadTask> mTaskList;
    private static DownloadManager manager = null;

    public static DownloadManager getInstances(){
        if (manager == null)
            manager = new DownloadManager();
        return manager;
    }

    public DownloadManager(){
        Netroid.init();
        Netroid.setFileDownloder(new FileDownloader(Netroid.getRequestQueue(), 1){
            @Override
            public FileDownloadRequest buildRequest(String storeFilePath, String url) {
                return new FileDownloadRequest(storeFilePath, url){
                    @Override
                    public void prepare() {
                        addHeader("Accept-Encoding", "identity");
                        super.prepare();
                    }
                };
            }
        });
        mTaskList = new LinkedList<>();
    }

    public DownloadTask addDownTask(DownloadTask task){
        return formatTaskList(task);
    }

    /** storeFilePath: 保存地址， downUrl: 下载地址 */
    public DownloadTask addDownTask(String storeFilePath, String downUrl){
        return formatTaskList(new DownloadTask(storeFilePath, downUrl));
    }

    /** tag: 标记, storeFilePath: 保存地址, downUrl: 下载地址 */
    public DownloadTask addDownTask(String tag, String storeFilePath, String downUrl){
        return formatTaskList(new DownloadTask(tag, storeFilePath, downUrl));
    }

    private DownloadTask formatTaskList(DownloadTask task){
        if (TextUtils.isEmpty(task.tag)) {
            controllerTaskListener(task);
            mTaskList.add(task);
            return task;
        }else{
            for (DownloadTask loadTask : mTaskList){
                if (TextUtils.equals(loadTask.tag, task.tag)){
                    return loadTask;
                }
            }
            controllerTaskListener(task);
            mTaskList.add(task);
            return task;
        }
    }

    private void controllerTaskListener(final DownloadTask task){
        task.controller = Netroid.getFileDownloader().add(task.storeFilePath, task.downFilePath, new Listener<Void>() {
            @Override
            public void onPreExecute() {
                task.invalidate();
            }

            @Override
            public void onSuccess(Void response) {

            }

            @Override
            public void onError(NetroidError error) {
                if (task.listener != null)
                    task.listener.onError(error.getMessage());
            }

            @Override
            public void onFinish() {
                task.invalidate();
                mTaskList.remove(task);
            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                task.onProgressChange(fileSize, downloadedSize);
            }
        });
    }
}
