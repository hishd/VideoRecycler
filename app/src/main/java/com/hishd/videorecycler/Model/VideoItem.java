package com.hishd.videorecycler.Model;

import android.graphics.Bitmap;

public class VideoItem {
    private String name;
    private String size;
    private String imagePath;

    public VideoItem(String name, String size, String imagePath) {
        this.name = name;
        this.size = size;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
