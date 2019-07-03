package io.github.code10ftn.monumentum.model.dto;

public class UpdateFavoriteMonumentRequest {

    private long monumentId;

    private boolean favorite;

    public UpdateFavoriteMonumentRequest() {
    }

    public UpdateFavoriteMonumentRequest(long monumentId, boolean favorite) {
        this.monumentId = monumentId;
        this.favorite = favorite;
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
