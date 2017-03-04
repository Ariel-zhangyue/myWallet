package com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface;

import com.wjq.dk.zy.mywallet.model.Category;

/**
 * Created by wangjiaqi on 16/10/28.
 */

public interface CategoryHandlerInterface {

    public Category queryBySubcategoryId(String id);

}
