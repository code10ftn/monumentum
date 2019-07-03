package io.github.code10.monumentumservice.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class UpdateMonumentRatingRequest {

    private long monumentId;

    @Min(0)
    @Max(5)
    private int rating;

    public UpdateMonumentRatingRequest() {
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
