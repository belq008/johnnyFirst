package com.xshell.xshelllib.greendao.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "FILE_CACHE".
 */
public class FileCache {

    private Long id;
    private String urlToMD5;
    private String localUrl;
    private Long fileSize;
    private String tactics;

    public FileCache() {
    }

    public FileCache(Long id) {
        this.id = id;
    }

    public FileCache(Long id, String urlToMD5, String localUrl, Long fileSize, String tactics) {
        this.id = id;
        this.urlToMD5 = urlToMD5;
        this.localUrl = localUrl;
        this.fileSize = fileSize;
        this.tactics = tactics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlToMD5() {
        return urlToMD5;
    }

    public void setUrlToMD5(String urlToMD5) {
        this.urlToMD5 = urlToMD5;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTactics() {
        return tactics;
    }

    public void setTactics(String tactics) {
        this.tactics = tactics;
    }

}
