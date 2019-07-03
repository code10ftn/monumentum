package io.github.code10ftn.monumentum.model;

import java.util.Date;

public class Comment {

    private long id;

    private String comment;

    private Monument monument;

    private Date timestamp;

    public Comment() {
    }

    public Comment(String comment, Monument monument, Date timestamp) {
        this.comment = comment;
        this.monument = monument;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
