package io.github.code10ftn.monumentum.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import io.github.code10ftn.monumentum.model.User;

public class UserDAO extends RuntimeExceptionDao<User, Integer> {

    public UserDAO(Dao<User, Integer> dao) {
        super(dao);
    }
}
