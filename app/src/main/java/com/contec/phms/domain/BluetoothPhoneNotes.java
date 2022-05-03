package com.contec.phms.domain;

public class BluetoothPhoneNotes {
    private String action;
    private String brand;
    private String btdevice;
    private String bttype;
    private String duration;
    private String model;

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand2) {
        this.brand = brand2;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model2) {
        this.model = model2;
    }

    public String getBttype() {
        return this.bttype;
    }

    public void setBttype(String bttype2) {
        this.bttype = bttype2;
    }

    public String getBtdevice() {
        return this.btdevice;
    }

    public void setBtdevice(String btdevice2) {
        this.btdevice = btdevice2;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration2) {
        this.duration = duration2;
    }

    public String toString() {
        return "BluetoothPhoneNotes [brand=" + this.brand + ", model=" + this.model + ", bttype=" + this.bttype + ", btdevice=" + this.btdevice + ", action=" + this.action + ", duration=" + this.duration + "]";
    }
}
