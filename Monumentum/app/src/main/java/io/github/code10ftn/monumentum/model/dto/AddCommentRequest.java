package io.github.code10ftn.monumentum.model.dto;

public class AddCommentRequest {

    private long monumentId;

    private String comment;

    public AddCommentRequest() {
    }

    public AddCommentRequest(long monumentId, String comment) {
        this.monumentId = monumentId;
        this.comment = comment;
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
