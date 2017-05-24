package com.streaming.model;

/**
 * Created by Administrator on 5/24/2017.
 */

public class HLSModel {
    private int id;
    private int status;
    private int percentage;
    private int totalFile;
    private int currentFileLoc;
    private String fileName;
    private String fileData;
    private String fileLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getTotalFile() {
        return totalFile;
    }

    public void setTotalFile(int totalFile) {
        this.totalFile = totalFile;
    }

    public int getCurrentFileLoc() {
        return currentFileLoc;
    }

    public void setCurrentFileLoc(int currentFileLoc) {
        this.currentFileLoc = currentFileLoc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
}
