package com.hypnagogix.runlicious;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mDate;
    private String mTime;
    private String mImageUrl;

    private String mKey;

    public  Upload(){
        //empty constructor needed
    }

    public Upload(String name, String date, String time, String imageUrl ){

        if (name.trim().equals("")){
            name = "No Name";
        }
        if (date.trim().equals("")){
            date = "No Date";
        }
        if (time.trim().equals("")){
            time = "No Time";
        }

        mName = name;
        mDate = date;
        mTime = time;
        mImageUrl = imageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

@Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
