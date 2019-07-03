package io.github.code10.monumentumservice.model.dto;

public class UpdateVisitedMonumentRequest {

    private long monumentId;

    private boolean visited;

    public UpdateVisitedMonumentRequest() {
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
