package net.lightbody.timecloud.api;

public class TimeCloudException extends Exception {
    private int statusCode;

    public TimeCloudException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
