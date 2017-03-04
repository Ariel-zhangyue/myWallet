package com.wjq.dk.zy.mywallet.model;

import java.io.Serializable;
import java.util.Date;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;

/**
 * Created by wangjiaqi on 16/10/27.
 */

public class Budget implements Serializable {
    private String budgetId;
    private String amount;
    private String expenseSum;
    private String month;
    private String year;
    private Date dateCreated;
    private Date dateUpdated;
    public Budget(){}
    public Budget(String bId, String bAmout, String bMonth, String bYear, Date bDateC, Date bDateU){
        this.budgetId = bId;
        this.amount = bAmout;
        this.month = bMonth;
        this.year = bYear;
        this.dateCreated = bDateC;
        this.dateUpdated = bDateU;
    }
    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpenseSum() {
        return expenseSum;
    }

    public void setExpenseSum(String expenseSum) {
        this.expenseSum = expenseSum;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
