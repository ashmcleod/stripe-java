package com.stripe.model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

  private static final Gson GSON_EVENT = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      // excludes `data` field during automatic deserialization because we have custom handling
      .setExclusionStrategies(new ExcludeEventDataFieldStrategy())
      .create();

  private static class ExcludeEventDataFieldStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> arg0) {
      return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
      return (f.getDeclaringClass() == Event.class && f.getName().equals("data"));
    }
  }

  public static class EventSerializer implements JsonSerializer<Event> {
    @Override
    public JsonElement serialize(Event event, Type typeOfSrc, JsonSerializationContext context) {

      // get event data from different read modes
      JsonElement rawEventData;
      if (event.isReadDataFailure()) {
        rawEventData = event.rawReadDataOnFailure();
      } else if (event.canSafeReadData()) {
        rawEventData = context.serialize(event.safeReadData());
      } else {
        rawEventData = context.serialize(event.bestAttemptReadData());
      }

      // serialized content here has no `data` field due to the exclusion strategy
      JsonObject serialized =  GSON_EVENT.toJsonTree(event, typeOfSrc).getAsJsonObject();
      serialized.add("data", rawEventData);
      return serialized;
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
    final TypeAdapter<JsonElement> jsonElementAdapter = gson.getAdapter(JsonElement.class);
    final TypeAdapter<EventData> eventDataAdapter = gson.getAdapter(EventData.class);

    TypeAdapter<Event> resultCustomTypeAdapter =
        new TypeAdapter<Event>() {
          @Override
          public void write(JsonWriter out, Event value) throws IOException {
            eventTypeAdapter.write(out, value);
          }

          @Override
          public Event read(JsonReader in) throws IOException {
            JsonObject eventJsonObject = jsonElementAdapter.read(in).getAsJsonObject();

            JsonObject eventDataJsonObject = eventJsonObject.getAsJsonObject("data");
            eventJsonObject.remove("data");
            // event starts without event data, to be set later depending on data deserialization
            Event event = eventTypeAdapter.fromJsonTree(eventJsonObject);

            TolerantRead<EventData> tolerantRead = new TolerantRead<>();

            if (eventDataJsonObject == null) {
              // no data set
            } else if (event.canSafeReadData()) {
              // when version matches, parsing exception should fail loudly
              EventData safeEventData = eventDataAdapter.fromJsonTree(eventDataJsonObject);
              tolerantRead.setSafeData(safeEventData);
            } else {
              // otherwise, we can afford handle parsing exception
              try {
                EventData bestAttemptEventData = eventDataAdapter.fromJsonTree(eventDataJsonObject);
                tolerantRead.setBestAttemptData(bestAttemptEventData);
              } catch (JsonParseException e) {
                tolerantRead.setRawDataOnFailure(eventDataJsonObject);
                tolerantRead.setReadException(e);
              }
            }
            event.setData(tolerantRead);

            return event;
          }
        };
    return (TypeAdapter<T>) resultCustomTypeAdapter.nullSafe();
  }
}
