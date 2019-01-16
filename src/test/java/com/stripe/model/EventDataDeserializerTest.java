package com.stripe.model;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonParseException;
import com.stripe.BaseStripeTest;
import com.stripe.Stripe;
import com.stripe.net.ApiResource;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EventDataDeserializerTest extends BaseStripeTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static final String OLD_EVENT_VERSION = "2013-08-15";
  private static final String CURRENT_EVENT_VERSION = "2017-08-15";
  private static final String NO_MATCH_VERSION = "2000-08-15";

  private String originalApiVersion;

  @Before
  public void saveOriginalApiVersion() {
    originalApiVersion = Stripe.apiVersion;
  }

  @After
  public void restoreOriginalStripeVersion() {
    Stripe.apiVersion = originalApiVersion;
  }

  private void verifyDeserializedEventData(EventData eventData) {
    final Application application = (Application) eventData.getObject();
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
  public void testApiVersionMatchesGetSafeData() throws Exception {
    Stripe.apiVersion = CURRENT_EVENT_VERSION;
    final String data = getCurrentEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertEquals(Stripe.apiVersion, event.getApiVersion());

    assertNotNull(event);
    assertNotNull(event.getId());

    assertNotNull(event.safeReadData());
    verifyDeserializedEventData(event.safeReadData());
  }

  @Test
  public void testApiVersionMismatchGetBestAttemptData() throws Exception {
    Stripe.apiVersion = NO_MATCH_VERSION;
    final String data = getCurrentEventStringFixture();
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertNotEquals(Stripe.apiVersion, event.getApiVersion());

    assertNotNull(event);
    assertNotNull(event.getId());

    assertFalse(event.canSafeReadData());
    assertNull(event.safeReadData());

    assertNotNull(event.bestAttemptReadData());
    verifyDeserializedEventData(event.bestAttemptReadData());
  }

  @Test
  public void testFailsLoudlyApiVersionMatches() throws Exception {
    Stripe.apiVersion = OLD_EVENT_VERSION;
    // if version match, we should not tolerate parsing failure.
    final String data = getOldEventStringFixture();
    thrown.expect(JsonParseException.class);
    ApiResource.GSON.fromJson(data, Event.class);
  }

  @Test
  public void testHandlesFailureOnApiVersionMatches() throws Exception {
    Stripe.apiVersion = NO_MATCH_VERSION;
    final String data = getOldEventStringFixture();

    final Event event = ApiResource.GSON.fromJson(data, Event.class);
    assertNotNull(event);
    assertNotNull(event.getId());

    assertNull(event.safeReadData());
    assertNull(event.bestAttemptReadData());

    assertTrue(event.isReadDataFailure());
    assertNotNull(event.rawReadDataOnFailure());

    // name is not primitive, causing the failure
    assertTrue(
        event.rawReadDataOnFailure().getAsJsonObject("object").get("name").isJsonArray());
    assertNotNull(event.getReadDataException());
    assertTrue(event.getReadDataException().getMessage()
        .contains("Expected STRING but was BEGIN_ARRAY at path $.name"));
  }
}
