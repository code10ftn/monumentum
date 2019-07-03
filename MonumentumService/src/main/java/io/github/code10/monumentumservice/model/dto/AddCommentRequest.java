package io.github.code10.monumentumservice.model.dto;

import javax.validation.constraints.NotEmpty;

public class AddCommentRequest {

    private long monumentId;

    @NotEmpty
    private String comment;

    public AddCommentRequest() {
    }

    public long getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(long monumentId) {
        this.monumentId = monumentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
