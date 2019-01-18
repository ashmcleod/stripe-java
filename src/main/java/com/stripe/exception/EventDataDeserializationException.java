package com.stripe.exception;

import com.google.gson.JsonElement;
import lombok.Getter;

public class EventDataDeserializationException extends StripeException {
  private static final long serialVersionUID = 2L;

  @Getter
  private final JsonElement rawJson;

  public EventDataDeserializationException(String message,
                                           JsonElement rawJson) {
    super(message, null, null, null);
    this.rawJson = rawJson;
  }

}
