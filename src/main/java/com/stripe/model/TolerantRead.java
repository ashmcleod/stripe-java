package com.stripe.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final class TolerantRead<T extends StripeObject> {
  private T safeData;
  private T bestAttemptData;
  private JsonObject rawDataOnFailure;
  private JsonParseException readException;
}
