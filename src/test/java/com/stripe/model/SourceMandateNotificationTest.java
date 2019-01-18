package com.stripe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.stripe.BaseStripeTest;
import com.stripe.net.ApiResource;

import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

public class SourceMandateNotificationTest extends BaseStripeTest {
  private void verifyResource(SourceMandateNotification mandateNotification) {
    final Map<String, String> typeData = mandateNotification.getTypeData();

    assertNotNull(mandateNotification);
    assertEquals("srcmn_123", mandateNotification.getId());
    assertEquals("source_mandate_notification", mandateNotification.getObject());
    assertEquals(1000L, (long) mandateNotification.getAmount());
    assertEquals(1516981090, (long) mandateNotification.getCreated());
    assertEquals(false, mandateNotification.getLivemode());
    assertEquals("debit_initiated", mandateNotification.getReason());
    assertEquals("pending", mandateNotification.getStatus());
    assertEquals("sepa_debit", mandateNotification.getType());
    assertEquals("src_123", mandateNotification.getSource().getId());
    assertEquals("sepa_debit", mandateNotification.getSource().getType());

    assertEquals("TEST111111111111111", typeData.get("creditor_identifier"));
    assertEquals("OAAAAAAAAAAAAAAO", typeData.get("mandate_reference"));
    assertEquals("3000", typeData.get("last4"));
  }

  @Test
  public void testDeserialize() throws Exception {
    final String json = getResourceAsString("/api_fixtures/source_mandate_notification.json");
    final SourceMandateNotification resource = ApiResource.GSON.fromJson(json,
        SourceMandateNotification.class);

    verifyResource(resource);
  }

  @Test
  public void testDeserializeEvent() throws Exception {
    final String json = getResourceAsString("/api_fixtures/source_mandate_notification_event.json");
    final Event event = ApiResource.GSON.fromJson(json, Event.class);

    EventData data = Mockito.spy(event.getData());
    Mockito.doReturn("2018-05-21").when(data).getIntegrationApiVersion();

    final SourceMandateNotification mandateNotification
        = (SourceMandateNotification) data.getObject();

    verifyResource(mandateNotification);
  }
}
