package com.example.assistant;

public class EmoItem {
    private String mEmoName;
    private int mEmoImages;

    public EmoItem(String EmoName, int EmoImage) {
        mEmoName = EmoName;
        mEmoImages = EmoImage;
    }

    public String getEmoName() {
        return mEmoName;
    }

    public int getEmoImage() {
        return mEmoImages;
    }
}
