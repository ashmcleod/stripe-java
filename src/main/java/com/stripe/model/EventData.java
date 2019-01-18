package com.stripe.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.stripe.Stripe;
import com.stripe.exception.EventDataDeserializationException;
import com.stripe.net.ApiResource;
import java.util.Map;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class EventData extends StripeObject {
  // Original properties
  StripeObject object;
  Map<String, Object> previousAttributes;

  // Augmented properties during deserialization
  @Setter(AccessLevel.NONE)
  JsonElement rawJson;
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  String apiVersion;

  /**
   * Stripe object {@link EventData#object} is intentionally left out during
   * initialization. Its deserialization from {@link EventData#rawJson} is deferred to
   * user instead. See {@link EventData#safeDeserialize()} and
   * {@link EventData#forceDeserialize()}.
   * @param rawJson             raw JSON for {@code object} field that can be deserialized into
   *     StripeObject.
   * @param previousAttributes  previous attributes of event data (has no further
   *     deserialization concerns)
   * @param apiVersion          api version of the event data, used to determine whether the
   *     deserialization to {@code object} field is safe.
   */
  EventData(JsonElement rawJson,
            Map<String, Object> previousAttributes,
            String apiVersion) {
    this.previousAttributes = previousAttributes;
    this.rawJson = rawJson;
    this.apiVersion = apiVersion;
  }

  /**
   * Safe deserialize raw JSON into StripeObject. This operation mutates the state, and the
   * successful result can be accessed via {@link EventData#getObject()}. Matching API
   * version between the current integration and the event is necessary to guarantee safe
   * deserialization.
   * @return whether deserialization has been successful.
   */
  public boolean safeDeserialize() {
    if (!apiVersionMatch()) {
      // when version mismatch, even when deserialization is successful,
      // we cannot guarantee data correctness. Old events containing fields that should be
      // translated/mapped to the new schema will simply not be captured by the new schema.
      return false;
    } else if (object != null) {
      // already successfully deserialized
      return true;
    } else {
      try {
        object = deserializeStripeObject(rawJson);
        return true;
      } catch (JsonParseException e) {
        // intentionally ignore exception to fulfill simply whether deserialization succeeds
        return false;
      }
    }
  }

  /**
   * When non-null, the deserialized {@code StripeObject} preserves high data integrity because of
   * strong correspondence between schema of the API response and the Java model class. This is
   * when API versions of current integration and the event are the same.
   * @return Object with high integrity from its original raw JSON response.
   */
  public StripeObject getObject() {
    if (object != null) {
      return object;
    }
    if (safeDeserialize()) {
      return object;
    } else {
      return null;
    }
  }

  /**
   * Raw JSON response to be deserialized into {@code StripeObject}. This is the same response in
   * {@link EventDataDeserializationException#getRawJson()} should exception takes place when
   * forcing deserialization with the current schema.
   */
  public JsonElement getRawJson() {
    return rawJson;
  }

  /**
   * Force deserialize raw JSON to {@code StripeObject}. The deserialized data is not guaranteed
   * to fully represent the JSON. For example, events of new API version having fields that are not
   * captured by current Java model class will be lost. Similarly, events of old API version
   * having fields that should be translated into the new fields, like field rename, will be lost.
   *
   * @return Object with no guarantee on full representation of its original raw JSON response.
   * @throws EventDataDeserializationException exception that contains the message error and the
   *     raw JSON response of the {@code StripeObject} to be deserialized.
   */
  public StripeObject forceDeserialize() throws EventDataDeserializationException {
    try {
      return deserializeStripeObject(rawJson);
    } catch (JsonParseException e) {
      String errorMessage;
      if (!apiVersionMatch()) {
        errorMessage = String.format(
            "Current integration has Stripe API version %s, but the event data has version %s. "
                + "This schema mismatch can cause the deserialization failure. Please see our API "
                + "version upgrade guide, and consider evolve the JSON to be compatible with "
                + "current integration schema. Original error message: ",
            getIntegrationApiVersion(), this.apiVersion, e.getMessage()
        );
      } else {
        errorMessage = "Unable to deserialize event data to respective Stripe object. "
            + "Please see the raw JSON object, and contact support@stripe.com for assistance.";
      }
      throw new EventDataDeserializationException(errorMessage, rawJson);
    }
  }

  private boolean apiVersionMatch() {
    return (this.apiVersion != null)
        && (this.apiVersion.equals(getIntegrationApiVersion()));
  }

  private StripeObject deserializeStripeObject(JsonElement rawJsonElement) {
    String type = rawJsonElement.getAsJsonObject().get("object").getAsString();
    Class<? extends StripeObject> cl = EventDataClassLookup.findClass(type);
    return ApiResource.GSON.fromJson(
        rawJsonElement, cl != null ? cl : StripeRawJsonObject.class);
  }

  /**
   * Internal method to allow for stubbing with different Stripe version.
   */
  String getIntegrationApiVersion() {
    return Stripe.apiVersion;
  }
}
