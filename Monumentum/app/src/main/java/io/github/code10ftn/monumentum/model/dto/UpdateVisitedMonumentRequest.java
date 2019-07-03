package io.github.code10ftn.monumentum.model.dto;

public class UpdateVisitedMonumentRequest {

    private long monumentId;

    private boolean visited;

    public UpdateVisitedMonumentRequest() {
    }

    public UpdateVisitedMonumentRequest(long monumentId, boolean visited) {
        this.monumentId = monumentId;
        this.visited = visited;
    }

    public long getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(long monumentId) {
        this.monumentId = monumentId;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}