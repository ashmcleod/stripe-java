package com.stripe.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Event extends ApiResource implements HasId {
  @Getter(onMethod = @__({@Override})) String id;
  String object;
  String account;
  String apiVersion;
  Long created;
  TolerantRead<EventData> data;
  Boolean livemode;
  Long pendingWebhooks;
  EventRequest request;
  String type;

  /**
   * Legacy; use `account` instead (https://stripe.com/docs/upgrades#2017-05-25)
   */
  @Deprecated
  String userId;

  // <editor-fold desc="list">
  /**
   * List all events.
   */
  public static EventCollection list(Map<String, Object> params) throws StripeException {
    return list(params, null);
  }

  /**
   * List all events.
   */
  public static EventCollection list(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    return requestCollection(classUrl(Event.class), params, EventCollection.class, options);
  }
  // </editor-fold>

  // <editor-fold desc="retrieve">
  /**
   * Retrieve an event.
   */
  public static Event retrieve(String id) throws StripeException {
    return retrieve(id, (RequestOptions) null);
  }

  /**
   * Retrieve an event.
   */
  public static Event retrieve(String id, RequestOptions options) throws StripeException {
    return retrieve(id, null, options);
  }

  /**
   * Retrieve an event.
   */
  public static Event retrieve(String id, Map<String, Object> params, RequestOptions options)
      throws StripeException {
    return request(RequestMethod.GET, instanceUrl(Event.class, id), params, Event.class, options);
  }

  /**
   * Deprecated in favor of more specific mode of reading data based on API version configured,
   * and the failure during deserialization. When deserialization fails, result is null, and
   * failure information should be checked. See {@link Event#rawReadDataOnFailure()} and
   * {@link Event#getReadDataException()}.
   * @return nullable event data.
   */
  @Deprecated
  public EventData getData() {
    if (data == null) {
      return null;
    }
    if (canSafeReadData()) {
      return data.getSafeData();
    } else {
      return data.getBestAttemptData();
    }
  }

  /**
   * Checks whether API version of the event matches that {@link Stripe#apiVersion} of this
   * integration.
   * @return true when the versions match.
   */
  public boolean canSafeReadData() {
    return apiVersion != null && apiVersion.equals(Stripe.apiVersion);
  }

  /**
   * When non-null, the deserialized data preserves high integrity because
   * of strong correspondence between schema of Stripe response and the Java model class.
   * @return Safe event data with high fidelity.
   */
  public EventData safeReadData() {
    if (data == null) {
      return null;
    }
    return data.getSafeData();
  }

  /**
   * When non-null, the deserialized data is not guaranteed to to contain all the fields, because
   * there can be mismatch between schema of Stripe response and the Java model class.
   *
   * @return Best attempt deserialized data.
   */
  public EventData bestAttemptReadData() {
    if (data == null) {
      return null;
    }
    return data.getBestAttemptData();
  }

  /**
   * When non-null, gets raw JSON data that couldn't be successfully deserialized.
   * The returned response can be mutated and will not affect the original data. This allows for
   * evolving your JSON object to be compatible with that of current model class.
   * @return raw JSON event data
   */
  public JsonObject rawReadDataOnFailure() {
    if (data == null) {
      return null;
    }
    return data.getRawDataOnFailure().deepCopy();
  }

  /**
   * Flag whether read fails due to deserialization failure.
   */
  public boolean isReadDataFailure() {
    return data != null && data.getRawDataOnFailure() != null && data.getReadException() != null;
  }

  /**
   * Parsing exception during data deserialization. Other exceptions are not specifically handled.
   * @return parsing exception.
   */
  public JsonParseException getReadDataException() {
    if (data == null) {
      return null;
    }
    return data.getReadException();
  }
  // </editor-fold>
}
