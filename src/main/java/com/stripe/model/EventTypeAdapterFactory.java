package com.stripe.model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class EventTypeAdapterFactory implements TypeAdapterFactory {

  private static final Gson GSON_SKIP_EVENT_DATA = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .setExclusionStrategies(new ExcludeEventDataFieldStrategy())
      .create();

  private static class ExcludeEventDataFieldStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> arg0) {
      return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
      // skip `EventData` serialization, to allow for custom mapping the data
      // into its original JSON response shape
      return (f.getDeclaringClass() == Event.class && f.getName().equals("data"));
    }
  }

  public static class EventSerializer implements JsonSerializer<Event> {
    @Override
    public JsonElement serialize(Event event, Type typeOfSrc, JsonSerializationContext context) {
      // when mapping versioned data back to JSON, we select only the original fields and ignore
      // the augmented fields we added during deserialization or during the object lifetime.
      EventData data = event.getData();
      JsonObject eventDataJson = new JsonObject();
      eventDataJson.add("object", data.getRawJson());
      eventDataJson.add("previous_attributes",
          GSON_SKIP_EVENT_DATA.toJsonTree(event.getData().getPreviousAttributes()));

      // serialize the event object except for the `data` field which we have custom handling
      JsonObject rootEventJson = GSON_SKIP_EVENT_DATA.toJsonTree(event, typeOfSrc)
          .getAsJsonObject();
      rootEventJson.add("data", eventDataJson);
      return rootEventJson;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (!Event.class.isAssignableFrom(type.getRawType())) {
      return null;
    }
    final TypeAdapter<Event> eventTypeAdapter =
        gson.getDelegateAdapter(this, TypeToken.get(Event.class));
    final TypeAdapter<JsonElement> jsonElementAdapter =
        gson.getAdapter(JsonElement.class);
    final TypeAdapter<EventData> eventDataAdapter =
        gson.getAdapter(EventData.class);

    TypeAdapter<Event> resultCustomTypeAdapter =
        new TypeAdapter<Event>() {
          @Override
          public void write(JsonWriter out, Event value) throws IOException {
            eventTypeAdapter.write(out, value);
          }

          @Override
          public Event read(JsonReader in) throws IOException {
            JsonObject rootEventJsonObject = jsonElementAdapter.read(in).getAsJsonObject();

            JsonObject eventDataJsonObject = rootEventJsonObject.getAsJsonObject("data");
            // remove data from root, and we handle deserialization separately
            rootEventJsonObject.remove("data");

            Event event = eventTypeAdapter.fromJsonTree(rootEventJsonObject);

            if (eventDataJsonObject != null) {
              if (event.getApiVersion() != null) {
                // motivation for deserializing event data from its root event object:
                // we want the event data to know which api version it is tagged with.
                eventDataJsonObject.add("api_version", new JsonPrimitive(event.getApiVersion()));
              }
              EventData eventData = eventDataAdapter.fromJsonTree(eventDataJsonObject);
              event.setData(eventData);
            }

            return event;
          }
        };
    return (TypeAdapter<T>) resultCustomTypeAdapter.nullSafe();
  }
}
