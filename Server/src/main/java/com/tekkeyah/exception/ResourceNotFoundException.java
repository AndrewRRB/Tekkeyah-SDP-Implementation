package com.tekkeyah.exception;

/** Thrown when a requested entity does not exist; mapped to 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " " + id + " not found");
    }
}
