package io.github.code10.monumentumservice.model.dto;

import java.util.Date;

public class UserCommentDto {

    private UserProfileDto user;

    private String comment;

    private Date timestamp;

    public UserCommentDto() {
    }

    public UserCommentDto(UserProfileDto userProfileDto, String comment, Date timestamp) {
        this.user = userProfileDto;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public UserProfileDto getUser() {
        return user;
    }

    public void setUser(UserProfileDto user) {
        this.user = user;
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
