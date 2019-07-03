package io.github.code10ftn.monumentum.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

import io.github.code10ftn.monumentum.R;
import io.github.code10ftn.monumentum.model.FavoriteMonument;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.User;
import io.github.code10ftn.monumentum.model.UserComment;
import io.github.code10ftn.monumentum.model.VisitedMonument;

@EBean
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, context.getString(R.string.app_name) + ".db", null, DATABASE_VERSION);
    }

    public void clearAllMonuments(Class c) {
        try {
            TableUtils.clearTable(connectionSource, c);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Monument.class);
            TableUtils.createTable(connectionSource, UserComment.class);
            TableUtils.createTable(connectionSource, FavoriteMonument.class);
            TableUtils.createTable(connectionSource, VisitedMonument.class);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, VisitedMonument.class, true);
            TableUtils.dropTable(connectionSource, FavoriteMonument.class, true);
            TableUtils.dropTable(connectionSource, UserComment.class, true);
            TableUtils.dropTable(connectionSource, Monument.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
