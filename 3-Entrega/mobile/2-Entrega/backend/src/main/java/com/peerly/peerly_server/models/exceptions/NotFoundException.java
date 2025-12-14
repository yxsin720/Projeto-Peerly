package com.peerly.peerly_server.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
  private final String id;
  private final String entity;
  private final String field;

  public NotFoundException(String id, String entity, String field) {
    super(entity + " with " + field + "=" + id + " not found");
    this.id = id;
    this.entity = entity;
    this.field = field;
  }

  public String getId() { return id; }
  public String getEntity() { return entity; }
  public String getField() { return field; }
}
