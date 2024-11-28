package com.example.assistant;

public class DailyItem {
    private String mDailyName;
    private int mDailyImages;

    public DailyItem(String DailyName, int DailyImage) {
        mDailyName = DailyName;
        mDailyImages = DailyImage;
    }

    public String getDailyName() {
        return mDailyName;
    }

    public int getDailyImage() {
        return mDailyImages;
    }
}
