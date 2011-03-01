package net.lightbody.timecloud.api;

public class DatabaseDoesNotExistException extends TimeCloudException {
    public DatabaseDoesNotExistException() {
        super("Database does not exist - did you create it first?", 404);
    }
}
