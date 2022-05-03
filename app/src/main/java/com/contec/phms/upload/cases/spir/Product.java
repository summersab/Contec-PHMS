package com.contec.phms.upload.cases.spir;

public class Product {
    private String product_details;
    private int product_image;
    private String product_name;
    private String product_type;

    public Product() {
    }

    public Product(int product_image2, String product_name2, String product_type2) {
        this.product_image = product_image2;
        this.product_name = product_name2;
        this.product_type = product_type2;
    }

    public int getProduct_image() {
        return this.product_image;
    }

    public void setProduct_image(int product_image2) {
        this.product_image = product_image2;
    }

    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(String product_name2) {
        this.product_name = product_name2;
    }

    public String getProduct_type() {
        return this.product_type;
    }

    public void setProduct_type(String product_type2) {
        this.product_type = product_type2;
    }
}
