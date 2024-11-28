package com.example.assistant;

public class InItem {
    private String mInName;
    private int mInImages;

    public InItem(String InName, int InImage) {
        mInName = InName;
        mInImages = InImage;
    }

    public String getInName() {
        return mInName;
    }

    public int getInImage() {
        return mInImages;
    }
}
