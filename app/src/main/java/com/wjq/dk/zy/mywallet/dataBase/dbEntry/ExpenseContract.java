package com.wjq.dk.zy.mywallet.dataBase.dbEntry;

import android.provider.BaseColumns;

/**
 * Created by wangjiaqi on 16/10/27.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public final class ExpenseContract {

    public ExpenseContract() {}

    public static abstract class ExpenseEntry implements BaseColumns {
        public static final String TABLE_NAME = "expense";
        public static final String COLUMN_NAME_EXPENSE_ID = "expense_id";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_SUBCATEGORY_ID = "subcategory_id";
        public static final String COLUMN_NAME_BUDGET_ID = "budget_id";
        public static final String COLUMN_NAME_DAY_CREATED = "day_created";
        public static final String COLUMN_NAME_DATE_CREATED = "date_created";
        public static final String COLUMN_NAME_DATE_UPDATED = "date_updated";

    }
}
