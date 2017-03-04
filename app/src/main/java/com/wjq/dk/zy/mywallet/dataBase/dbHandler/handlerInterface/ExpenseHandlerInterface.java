package com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface;

import com.wjq.dk.zy.mywallet.model.Expense;

import java.util.List;

/**
 * Created by wangjiaqi on 16/10/28.
 */

public interface ExpenseHandlerInterface {
    public Expense queryByExpenseId(String id);

    public long insert(Expense expense);

    public void deleteByExpenseId(String id);

    public long update(Expense expense);

    public List getExpenseListGroupBySubcategory(String startDate, String endDate);

    public List getExpenseListGroupByDay(String startDate, String endDate);

//    public List getExpenseListInMonth(String startDate,String endDate);

    public void deleteAll();

    public List<Expense> queryByDay(String day);

    public List<String> queryAllDate();

}

