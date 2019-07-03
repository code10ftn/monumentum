package io.github.code10ftn.monumentum.database.wrapper;

import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.database.DatabaseHelper;
import io.github.code10ftn.monumentum.database.dao.MonumentDAO;
import io.github.code10ftn.monumentum.database.dao.UserCommentDAO;
import io.github.code10ftn.monumentum.database.dao.VisitedMonumentDAO;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.UserComment;
import io.github.code10ftn.monumentum.model.VisitedMonument;

@EBean
public class VisitedMonumentDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    VisitedMonumentDAO visitedMonumentDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    MonumentDAO monumentDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    UserCommentDAO userCommentDAO;

    public List<Monument> findAll() {
        final List<VisitedMonument> visitedMonuments = visitedMonumentDAO.queryForAll();
        final List<Monument> monuments = new ArrayList<>();

        for (VisitedMonument visitedMonument : visitedMonuments) {
            monuments.add(visitedMonument.getMonument());
        }

        return monuments;
    }

    public Monument findMonumentById(long id) {
        try {
            final QueryBuilder<Monument, Integer> monumentQueryBuilder = monumentDAO.queryBuilder();
            monumentQueryBuilder.where().eq("id", id);

            final QueryBuilder<VisitedMonument, Integer> visitedMonumentQueryBuilder = visitedMonumentDAO.queryBuilder();
            final VisitedMonument visitedMonument = visitedMonumentQueryBuilder.join(monumentQueryBuilder).queryForFirst();
            if (visitedMonument != null) {
                return visitedMonument.getMonument();
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(List<Monument> monuments) {
        for (Monument monument : monuments) {
            Monument persisted = null;
            try {
                persisted = monumentDAO.queryBuilder().where().eq("id", monument.getId()).queryForFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            visitedMonumentDAO.create(new VisitedMonument(persisted != null ? persisted : monument));

            for (UserComment userComment : monument.getUserComments()) {
                userComment.setMonument(monument);
                userCommentDAO.create(userComment);
            }
        }
    }
}
