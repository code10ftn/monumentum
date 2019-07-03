package io.github.code10ftn.monumentum.database.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.List;

import io.github.code10ftn.monumentum.database.DatabaseHelper;
import io.github.code10ftn.monumentum.database.dao.MonumentDAO;
import io.github.code10ftn.monumentum.database.dao.UserCommentDAO;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.UserComment;

@EBean
public class MonumentDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    MonumentDAO monumentDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    UserCommentDAO userCommentDAO;

    public List<Monument> findAll() {
        return monumentDAO.queryForAll();
    }

    public void save(List<Monument> monuments) {
        monumentDAO.create(monuments);

        for (Monument monument : monuments) {
            for (UserComment userComment : monument.getUserComments()) {
                userComment.setMonument(monument);
                userCommentDAO.create(userComment);
            }
        }
    }
}
