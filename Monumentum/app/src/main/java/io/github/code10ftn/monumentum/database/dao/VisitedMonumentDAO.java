package io.github.code10ftn.monumentum.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import io.github.code10ftn.monumentum.model.VisitedMonument;

public class VisitedMonumentDAO extends RuntimeExceptionDao<VisitedMonument, Integer> {

    public VisitedMonumentDAO(Dao<VisitedMonument, Integer> dao) {
        super(dao);
    }
}
