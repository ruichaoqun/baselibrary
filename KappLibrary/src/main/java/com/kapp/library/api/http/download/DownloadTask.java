package com.kapp.library.api.http.download;

import com.duowan.mobile.netroid.toolbox.FileDownloader;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class DownloadTask {

    protected final static DecimalFormat DECIMAL_POINT = new DecimalFormat("0.0");
    protected FileDownloader.DownloadController controller;
    protected String storeFilePath;
    protected String downFilePath;
    protected String tag;

    protected long fileSize;
    protected long downloadedSize;
    protected OnDownloadStausListener listener;

    public DownloadTask(String storeFilePath, String downFilePath) {
        this(null, storeFilePath, downFilePath);
    }

    public DownloadTask(String tag, String storeFilePath, String downFilePath){
        this.tag = tag;
        this.storeFilePath = storeFilePath;
        this.downFilePath = downFilePath;
    }

    protected void onProgressChange(long fileSize, long downloadedSize) {
        this.fileSize = fileSize;
        this.downloadedSize = downloadedSize;
        invalidate();
    }

    protected void invalidate() {

        switch (controller.getStatus()) {
            case FileDownloader.DownloadController.STATUS_DOWNLOADING://下载中
                String progress;
                if (fileSize > 0 && downloadedSize > 0) {
                    progress = DECIMAL_POINT.format(downloadedSize * 1.0f / fileSize * 100) + "%";
                } else {
                    progress = "0%";
                }
                if (listener != null)
                    listener.onDownloadStatus(false, progress);
                break;
            case FileDownloader.DownloadController.STATUS_WAITING://等待
                break;
            case FileDownloader.DownloadController.STATUS_PAUSE://暂停
                break;
            case FileDownloader.DownloadController.STATUS_SUCCESS://完成
                if (listener != null)
                    listener.onDownloadStatus(true, "100%");
                break;
        }

//        txvDownloadedSize.setText(Formatter.formatFileSize(FileDownloadActivity.this, downloadedSize));
//        txvFileSize.setText(Formatter.formatFileSize(FileDownloadActivity.this, fileSize));
    }

    public void setOnDownloadStatusListener(OnDownloadStausListener listener){
        this.listener = listener;
    }

    public interface OnDownloadStausListener{

        void onDownloadStatus(boolean isDone, String progress);

        void onError(String error);
    }
}
