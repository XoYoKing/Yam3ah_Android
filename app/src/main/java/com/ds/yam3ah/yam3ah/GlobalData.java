package com.ds.yam3ah.yam3ah;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 4/24/2015.
 */
public class GlobalData {

    public static String selectedCategory = "",Email,contactNumber,Area;
    public static String blockNumber,streetNumber,avenue,buildingNumber,floorNumber,
            apartmentNumber,notes;
    public static String cityId="";
    public static int TotalCostCart = 0;
    public static int totalItemCount = 0;

    String product_id;
    String product_name;
    String product_currency;
    String product_price;
    String product_desc;
    String product_feature_image;
    String com_cat_id;
    String company_id;
    int quantity;
    String currency;
    String product_Quantity;
    String product_speclRequest;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_currency() {
        return product_currency;
    }

    public void setProduct_currency(String product_currency) {
        this.product_currency = product_currency;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_feature_image() {
        return product_feature_image;
    }

    public void setProduct_feature_image(String product_feature_image) {
        this.product_feature_image = product_feature_image;
    }

    public String getCom_cat_id() {
        return com_cat_id;
    }

    public void setCom_cat_id(String com_cat_id) {
        this.com_cat_id = com_cat_id;
    }

    public String getProduct_speclRequest() {
        return product_speclRequest;
    }

    public void setProduct_speclRequest(String product_speclRequest) {
        this.product_speclRequest = product_speclRequest;
    }


    public String getProduct_Quantity() {
        return product_Quantity;
    }

    public void setProduct_Quantity(String product_Quantity) {
        this.product_Quantity = product_Quantity;
    }
}

