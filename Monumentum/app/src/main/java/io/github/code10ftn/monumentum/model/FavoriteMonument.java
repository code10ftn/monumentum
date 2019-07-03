package io.github.code10ftn.monumentum.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "favourite_monuments")
public class FavoriteMonument {

    public static final String MONUMENT_ID_FIELD = "monument";

    @DatabaseField(columnName = MONUMENT_ID_FIELD, canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Monument monument;

    public FavoriteMonument() {
    }

    public FavoriteMonument(Monument monument) {
        this.monument = monument;
    }

    public Monument getMonument() {
        return monument;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }
}
