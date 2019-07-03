package io.github.code10ftn.monumentum.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import io.github.code10ftn.monumentum.model.Monument;

public class MonumentDAO extends RuntimeExceptionDao<Monument, Integer> {

    public MonumentDAO(Dao<Monument, Integer> dao) {
        super(dao);
    }
}
