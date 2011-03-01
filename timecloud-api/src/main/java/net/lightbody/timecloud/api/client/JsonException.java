package net.lightbody.timecloud.api.client;

import net.lightbody.timecloud.api.TimeCloudException;

public class JsonException {
    private String message;
    private String exception;

    public JsonException() {
    }

    public JsonException(Exception e) {
        if (e instanceof TimeCloudException) {
            @SuppressWarnings({"unchecked"}) Class<? extends TimeCloudException> c = (Class<? extends TimeCloudException>) e.getClass();
            exception = c.getName().substring("net.lightbody.timecloud.api.".length());
        } else {
            exception = "GenericException";
        }

        message = e.getMessage();
    }

    TimeCloudException makeTypedException() {
        String name = "net.lightbody.timecloud.api." + exception;
        try {
            return (TimeCloudException) Class.forName(name).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate exception " + exception, e);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
