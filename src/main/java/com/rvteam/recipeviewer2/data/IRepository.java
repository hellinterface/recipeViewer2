package com.rvteam.recipeviewer2.data;

import java.util.List;

public interface IRepository<T extends IEntity> {
    public boolean createTable();
    public List<IEntity> selectAll();
    public IEntity selectByID(int id);
    public boolean update(T entity);
    public Integer insert(T entity);
    public boolean remove(IEntity entity);
    public Integer push(T entity);
}
