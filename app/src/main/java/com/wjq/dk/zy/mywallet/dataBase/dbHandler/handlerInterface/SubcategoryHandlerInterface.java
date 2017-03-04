package com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface;

import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.util.List;

/**
 * Created by Elvn on 2016/10/28.
 */

public interface SubcategoryHandlerInterface {
    public Subcategory queryBySubcategoryId(String id);

    public List<Subcategory> queryByCategoryId(String id);

    public long insert(Subcategory subcategory);

    public void deleteBySubcategoryId(String id);

    public void deleteAll();
    //public long update(Subcategory subcategory);
}