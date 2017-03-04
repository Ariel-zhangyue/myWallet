package com.wjq.dk.zy.mywallet.model;

import com.wjq.dk.zy.mywallet.util.IDGenerator;

import java.util.Date;

/**
 * Created by wangjiaqi on 16/10/28.
 */

public class Subcategory {
    private String subcategoryId;
    private String name;
    private String categoryId;
    private Date dateCreated;
    private Date dateUpdated;
    public Subcategory(){this.subcategoryId = IDGenerator.generateID();}
    public Subcategory(String sId, String n, String cId, Date dC, Date dU){
        this.subcategoryId = sId;
        this.name = n;
        this.categoryId = cId;
        this.dateCreated = dC;
        this.dateUpdated = dU;
    }
    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

}
