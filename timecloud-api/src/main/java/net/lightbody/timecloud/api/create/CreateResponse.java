package net.lightbody.timecloud.api.create;

public class CreateResponse {
    private String id;

    public CreateResponse() {
    }

    public CreateResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
