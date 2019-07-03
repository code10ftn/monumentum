package io.github.code10.monumentumservice.model.dto;

public class UpdateFavoriteMonumentRequest {

    private long monumentId;

    private boolean favorite;

    public UpdateFavoriteMonumentRequest() {
    }

    public long getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(long monumentId) {
        this.monumentId = monumentId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
