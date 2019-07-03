package io.github.code10ftn.monumentum.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import io.github.code10ftn.monumentum.model.UserComment;

public class UserCommentDAO extends RuntimeExceptionDao<UserComment, Integer> {

    public UserCommentDAO(Dao<UserComment, Integer> dao) {
        super(dao);
    }
}
