package com.stripe.model;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.stripe.BaseStripeTest;
import com.stripe.exception.EventDataDeserializationException;
import com.stripe.net.ApiResource;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

public class EventDataDeserializerTest extends BaseStripeTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static final String OLD_EVENT_VERSION = "2013-08-15";
  private static final String CURRENT_EVENT_VERSION = "2017-08-15";
  private static final String NO_MATCH_VERSION = "2000-08-15";

  private void verifyDeserializedEventData(StripeObject stripeObject) {
    final Application application = (Application) stripeObject;
    assertNotNull(application);
    assertNotNull(application.getId());
  }

  private String getCurrentEventStringFixture() throws IOException {
    return getResourceAsString("/api_fixtures/account_application_deauthorized.json");
  }

  private String getOldEventStringFixture() throws IOException {
    return getResourceAsString(
        "/api_fixtures/account_application_deauthorized_old_version.json");
  }

  @Test
  public void testSafeDeserializationOnApiVersionMatch() throws Exception {
    final String data = getCurrentEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertNotNull(event);
    assertNotNull(event.getId());

    assertEquals(CURRENT_EVENT_VERSION, event.getApiVersion());
    EventData stubbedData = stubIntegrationApiVersion(
        event.getData(), CURRENT_EVENT_VERSION);

    assertTrue(stubbedData.safeDeserialize());
    assertNotNull(stubbedData.getObject());
    verifyDeserializedEventData(stubbedData.getObject());
  }

  @Test
  public void testForceDeserializationOnApiVersionMismatch() throws Exception {
    final String data = getCurrentEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertNotNull(event);
    assertNotNull(event.getId());

    assertNotEquals(NO_MATCH_VERSION, event.getApiVersion());
    EventData stubbedData = stubIntegrationApiVersion(
        event.getData(), NO_MATCH_VERSION);

    assertFalse(stubbedData.safeDeserialize());

    // although version mismatch, schema is still compatible
    // so force deserialization is successful
    assertNotNull(stubbedData.forceDeserialize());
    verifyDeserializedEventData(stubbedData.forceDeserialize());
  }

  @Test
  public void testRawJsonAvailable() throws Exception {
    final String data = getCurrentEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);


    assertNotEquals(NO_MATCH_VERSION, event.getApiVersion());
    EventData stubbedData = stubIntegrationApiVersion(
        event.getData(), NO_MATCH_VERSION);

    assertFalse(stubbedData.safeDeserialize());

    // although version mismatch, schema is still compatible
    // so force deserialization is successful
    assertNotNull(stubbedData.forceDeserialize());
    verifyDeserializedEventData(stubbedData.forceDeserialize());
  }

  @Test
  public void testForceDeserializationDoesNotMutateState() throws IOException,
      EventDataDeserializationException {

    final String data = getCurrentEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    EventData eventData = event.getData();

    StripeObject stripeObject;
    if (eventData.safeDeserialize()) {
      stripeObject = eventData.getObject();
    } else {
      try {
        stripeObject = eventData.forceDeserialize();
      } catch (EventDataDeserializationException e) {
        JsonElement rawJson = e.getRawJson();
        // some patch work
      }
    }


    EventData stubbedData = stubIntegrationApiVersion(
        eventData, NO_MATCH_VERSION);

    assertFalse(stubbedData.safeDeserialize());
    // when it is unsafe, getting normal object returns nothing.
    assertNull(stubbedData.getObject());

    StripeObject forceDeserialized = stubbedData.forceDeserialize();
    assertNotNull(forceDeserialized);
    // successful forced deserialization, but get object remains empty
    assertNull(stubbedData.getObject());
  }

  @Test
  public void testFailureOnApiVersionMatch() throws Exception {
    final String data = getOldEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertEquals(OLD_EVENT_VERSION, event.getApiVersion());
    EventData stubbedData = stubIntegrationApiVersion(
        event.getData(), OLD_EVENT_VERSION);

    assertFalse(stubbedData.safeDeserialize());

    try {
      stubbedData.forceDeserialize();
      fail("Expect event data deserialization failure.");
    } catch (EventDataDeserializationException e) {
      JsonElement originalEventData = new JsonParser().parse(data)
          .getAsJsonObject().get("data")
          .getAsJsonObject().get("object");
      assertEquals(originalEventData, e.getRawJson());
      assertEquals(stubbedData.getRawJson(), e.getRawJson());
      assertTrue(e.getMessage()
          .contains("Unable to deserialize event data to respective Stripe object"));
    }
  }

  @Test
  public void testFailureOnApiVersionMisMatch() throws Exception {
    final String data = getOldEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertEquals(OLD_EVENT_VERSION, event.getApiVersion());
    EventData stubbedData = stubIntegrationApiVersion(
        event.getData(), NO_MATCH_VERSION);

    assertFalse(stubbedData.safeDeserialize());

    try {
      stubbedData.forceDeserialize();
      fail("Expect event data deserialization failure.");
    } catch (EventDataDeserializationException e) {
      assertTrue(e.getMessage().contains(
          "Current integration has Stripe API version "
              + NO_MATCH_VERSION + ", but the event data has version "
              + OLD_EVENT_VERSION));
    }
  }

  private EventData stubIntegrationApiVersion(EventData data,
                                              String stripeVersion) {
    EventData dataSpy = Mockito.spy(data);
    Mockito.doReturn(stripeVersion).when(dataSpy).getIntegrationApiVersion();
    return dataSpy;
  }
}
