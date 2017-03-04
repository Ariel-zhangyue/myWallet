package com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface;

import com.wjq.dk.zy.mywallet.model.Budget;

import java.util.List;

/**
 * Created by wangjiaqi on 16/10/28.
 */

public interface BudgetHandlerInterface {

    public Budget queryByBudgetId(String id);

    public Budget queryByYearAndMonth(String year, String month);

    public List<Budget> queryAllBudget();

    public long insert(Budget budget);

    public void deleteByBudgetId(String id);

    public long update(Budget budget);
}
