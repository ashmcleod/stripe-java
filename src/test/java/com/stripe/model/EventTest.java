package com.stripe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.stripe.BaseStripeTest;
import com.stripe.net.ApiResource;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;

public class EventTest extends BaseStripeTest {

  @Test
  public void testDeserialize() throws Exception {
    final String data = getResourceAsString("/api_fixtures/event_plan.json");
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    assertNotNull(event);
    assertNotNull(event.getId());
    assertEquals("event", event.getObject());

    EventData eventData = stubIntegrationApiVersion(event.getData(), "2018-09-24");

    final Plan plan = (Plan) eventData.getObject();
    assertNotNull(plan);
    assertNotNull(plan.getId());
  }

  @Test
  public void testReserialize() throws Exception {
    final String data = getResourceAsString("/api_fixtures/event_plan.json");
    final Event event = ApiResource.GSON.fromJson(data, Event.class);

    final Event reserializedEvent = ApiResource.GSON.fromJson(event.toJson(), Event.class);

    assertEquals(reserializedEvent.getId(), event.getId());
    assertEquals(reserializedEvent.getObject(), event.getObject());
    assertEquals(reserializedEvent.getAccount(), event.getAccount());
    assertEquals(reserializedEvent.getApiVersion(), event.getApiVersion());
    assertEquals(reserializedEvent.getCreated(), event.getCreated());
    assertEquals(reserializedEvent.getLivemode(), event.getLivemode());
    assertEquals(reserializedEvent.getRequest().getId(), event.getRequest().getId());
    assertEquals(reserializedEvent.getRequest().getIdempotencyKey(),
        event.getRequest().getIdempotencyKey());
    assertEquals(reserializedEvent.getType(), event.getType());

    // because non-null `previousAttributes` is a map without equals being implemented
    // comparing them directly returns inequality
    assertNotEquals(reserializedEvent.getData(), event.getData());

    // we can compare setting the map to null, and compare the untyped map separately
    final EventData eventData = event.getData();
    final Map<String, Object> previousAttributes = eventData.getPreviousAttributes();
    eventData.setPreviousAttributes(null);

    final EventData reserializedEventData = reserializedEvent.getData();
    final Map<String, Object> reserializedPreviousAttributes =
        reserializedEventData.getPreviousAttributes();
    reserializedEventData.setPreviousAttributes(null);

    // events without the map are equal
    assertEquals(reserializedEvent.getData(), event.getData());
    // comparing previous attribute map
    assertEquals(
        ApiResource.GSON.toJsonTree(reserializedPreviousAttributes),
        ApiResource.GSON.toJsonTree(previousAttributes));

    // comparing deserialized stripe object
    StripeObject reserializedStripeObject = stubIntegrationApiVersion(
        reserializedEventData, "2018-09-24").getObject();
    StripeObject stripeObject = stubIntegrationApiVersion(
        eventData, "2018-09-24").getObject();
    assertEquals(reserializedStripeObject, stripeObject);
  }

  private EventData stubIntegrationApiVersion(EventData data,
                                              String stripeVersion) {
    EventData dataSpy = Mockito.spy(data);
    Mockito.doReturn(stripeVersion).when(dataSpy).getIntegrationApiVersion();
    return dataSpy;
  }
}
