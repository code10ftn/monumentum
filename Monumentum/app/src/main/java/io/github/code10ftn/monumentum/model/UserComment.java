package io.github.code10ftn.monumentum.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "user_comment")
public class UserComment {

    @DatabaseField(columnName = "user", canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private User user;

    @DatabaseField(columnName = "comment", canBeNull = false)
    private String comment;

    @DatabaseField(columnName = "monument", canBeNull = false, foreign = true)
    private Monument monument;

    @DatabaseField(columnName = "timestamp", canBeNull = false)
    private Date timestamp;

    public UserComment() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Monument getMonument() {
        return monument;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
