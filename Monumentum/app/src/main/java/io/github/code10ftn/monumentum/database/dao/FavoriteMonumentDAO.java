package io.github.code10ftn.monumentum.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import io.github.code10ftn.monumentum.model.FavoriteMonument;

public class FavoriteMonumentDAO extends RuntimeExceptionDao<FavoriteMonument, Integer> {

    public FavoriteMonumentDAO(Dao<FavoriteMonument, Integer> dao) {
        super(dao);
    }
}
