package io.github.code10.monumentumservice.model.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserCommentMonument extends BaseModel {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "monument_id")
    private Monument monument;

    @Column(nullable = false)
    private String comment;

    private Date timestamp;

    public UserCommentMonument() {
    }

    public UserCommentMonument(User user, Monument monument, String comment) {
        this.user = user;
        this.monument = monument;
        this.comment = comment;
        this.timestamp = new Date();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Monument getMonument() {
        return monument;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
