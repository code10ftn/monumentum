package io.github.code10ftn.monumentum.database.wrapper;

import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.database.DatabaseHelper;
import io.github.code10ftn.monumentum.database.dao.FavoriteMonumentDAO;
import io.github.code10ftn.monumentum.database.dao.MonumentDAO;
import io.github.code10ftn.monumentum.database.dao.UserCommentDAO;
import io.github.code10ftn.monumentum.model.FavoriteMonument;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.UserComment;

@EBean
public class FavoriteMonumentDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    FavoriteMonumentDAO favoriteMonumentDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    MonumentDAO monumentDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    UserCommentDAO userCommentDAO;

    public List<Monument> findAll() {
        final List<FavoriteMonument> favoriteMonuments = favoriteMonumentDAO.queryForAll();
        final List<Monument> monuments = new ArrayList<>();

        for (FavoriteMonument favoriteMonument : favoriteMonuments) {
            monuments.add(favoriteMonument.getMonument());
        }

        return monuments;
    }

    public void save(List<Monument> monuments) {
        for (Monument monument : monuments) {
            Monument persisted = null;
            try {
                persisted = monumentDAO.queryBuilder().where().eq("id", monument.getId()).queryForFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            favoriteMonumentDAO.create(new FavoriteMonument(persisted != null ? persisted : monument));

            for (UserComment userComment : monument.getUserComments()) {
                userComment.setMonument(monument);
                userCommentDAO.create(userComment);
            }
        }
    }

    public Monument findMonumentById(long id) {
        try {
            final QueryBuilder<Monument, Integer> monumentQueryBuilder = monumentDAO.queryBuilder();
            monumentQueryBuilder.where().eq("id", id);

            final QueryBuilder<FavoriteMonument, Integer> favoriteMonumentQueryBuilder = favoriteMonumentDAO.queryBuilder();
            final FavoriteMonument favoriteMonument = favoriteMonumentQueryBuilder.join(monumentQueryBuilder).queryForFirst();
            if (favoriteMonument != null) {
                return favoriteMonument.getMonument();
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
