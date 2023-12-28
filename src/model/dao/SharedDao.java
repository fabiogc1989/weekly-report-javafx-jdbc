package model.dao;

import java.util.List;

public interface SharedDao<D, T> {
	
	List<T> findAll();

    T findById(D id);

    void insert(T obj);

    void deleteById(D id);

    void delete(T obj);

    void update(T obj);
}
