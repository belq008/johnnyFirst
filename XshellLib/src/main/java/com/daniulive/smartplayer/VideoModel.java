package com.daniulive.smartplayer;

/**
 * Created by wn on 2016/8/2.
 */
public class VideoModel {

    private String roomid;
    private String roomtitle;
    private String roomdes;
    private String authorname;
    private String headurl;
    private String imageurl;
    private String userid;
    private String starttime;
    private String endtime;
    private String state;
    private String id;
    private String types;
    private String loginname;


    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public String getTypes() {
        return types;
    }

    public String getLoginname() {
        return loginname;
    }

    public String getRoomid() {
        return roomid;
    }

    public String getRoomtitle() {
        return roomtitle;
    }

    public String getRoomdes() {
        return roomdes;
    }

    public String getAuthorname() {
        return authorname;
    }

    public String getHeadurl() {
        return headurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public void setRoomtitle(String roomtitle) {
        this.roomtitle = roomtitle;
    }

    public void setRoomdes(String roomdes) {
        this.roomdes = roomdes;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
                "roomid='" + roomid + '\'' +
                ", roomtitle='" + roomtitle + '\'' +
                ", roomdes='" + roomdes + '\'' +
                ", authorname='" + authorname + '\'' +
                ", headurl='" + headurl + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", userid='" + userid + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", state='" + state + '\'' +
                ", id='" + id + '\'' +
                ", types='" + types + '\'' +
                ", loginname='" + loginname + '\'' +
                '}';
    }
}
