package io.github.code10ftn.monumentum.model.dto;

public class UpdateMonumentRatingRequest {

    private long monumentId;

    private int rating;

    public UpdateMonumentRatingRequest() {
    }

    public UpdateMonumentRatingRequest(long monumentId, int rating) {
        this.monumentId = monumentId;
        this.rating = rating;
    }

    public long getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(long monumentId) {
        this.monumentId = monumentId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
