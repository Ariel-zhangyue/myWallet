package com.wjq.dk.zy.mywallet.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangjiaqi on 16/10/28.
 */

public class Expense implements Serializable{
//    private static final long serialVersionUID = 1L;
    private String expenseId;
    private String amount;
    private Subcategory subcategory;
    private String budgetId;
    private Date dayCreated;
    private Date dateCreated;
    private Date dateUpdated;
    public Expense(){}
    public Expense(String eId, String am, Subcategory sub, String bId, Date dayC, Date dC, Date dU){
        this.expenseId = eId;
        this.amount = am;
        this.subcategory = sub;
        this.budgetId = bId;
        this.dayCreated = dayC;
        this.dateCreated = dC;
        this.dateUpdated = dU;
    }
    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public Date getDayCreated() {
        return dayCreated;
    }

    public void setDayCreated(Date dayCreated) {
        this.dayCreated = dayCreated;
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
