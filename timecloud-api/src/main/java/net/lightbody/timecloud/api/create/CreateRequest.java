package net.lightbody.timecloud.api.create;

public class CreateRequest {
    private String foo;

    public CreateRequest() {
    }

    public CreateRequest(String foo) {
        this.foo = foo;
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }
}
