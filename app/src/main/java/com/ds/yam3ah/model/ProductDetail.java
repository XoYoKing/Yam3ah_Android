package com.ds.yam3ah.model;

/**
 * Created by Shivangi on 6/11/2015.
 */
public class ProductDetail {

    private String product_company_id;
    private String product_id;
    private String product_name;
    private String product_desc;
    private String product_price;
    private String product_currency;
    private String product_feature_image;
    private String product_feature_thumb;
    private String product_quantity;
    private String com_cat_id;
    private String com_cat_title;
    private boolean icClicked;
    private int clickCount;


    public String getProduct_company_id() {
        return product_company_id;
    }

    public void setProduct_company_id(String product_company_id) {
        this.product_company_id = product_company_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_currency() {
        return product_currency;
    }

    public void setProduct_currency(String product_currency) {
        this.product_currency = product_currency;
    }

    public String getProduct_feature_image() {
        return product_feature_image;
    }

    public void setProduct_feature_image(String product_feature_image) {
        this.product_feature_image = product_feature_image;
    }

    public String getProduct_feature_thumb() {
        return product_feature_thumb;
    }

    public void setProduct_feature_thumb(String product_feature_thumb) {
        this.product_feature_thumb = product_feature_thumb;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getCom_cat_id() {
        return com_cat_id;
    }

    public void setCom_cat_id(String com_cat_id) {
        this.com_cat_id = com_cat_id;
    }

    public boolean isIcClicked() {
        return icClicked;
    }

    public void setIcClicked(boolean icClicked) {
        this.icClicked = icClicked;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getCom_cat_title() {
        return com_cat_title;
    }

    public void setCom_cat_title(String com_cat_title) {
        this.com_cat_title = com_cat_title;
    }
}
