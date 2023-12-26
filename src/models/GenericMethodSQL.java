package models;

import java.util.List;

/**
 * @author Work
 */

public interface GenericMethodSQL<T>{
    public void create(T t) throws DaoException;
    public List<T> read(T t) throws DaoException; 
    public void update(T t) throws DaoException;
    public void delete(T t) throws DaoException;
    public void search(T t) throws DaoException;
}
