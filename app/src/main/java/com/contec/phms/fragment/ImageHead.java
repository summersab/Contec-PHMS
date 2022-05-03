package com.contec.phms.fragment;

import java.io.Serializable;

public class ImageHead implements Serializable {
    private int image_head;

    public ImageHead() {
    }

    public ImageHead(int image_head2) {
        this.image_head = image_head2;
    }

    public int getImage_head() {
        return this.image_head;
    }

    public void setImage_head(int image_head2) {
        this.image_head = image_head2;
    }
}
