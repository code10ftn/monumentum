package io.github.code10ftn.monumentum.database.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import io.github.code10ftn.monumentum.database.DatabaseHelper;
import io.github.code10ftn.monumentum.database.dao.UserCommentDAO;

@EBean
public class UserCommentDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    UserCommentDAO userCommentDAO;
}
