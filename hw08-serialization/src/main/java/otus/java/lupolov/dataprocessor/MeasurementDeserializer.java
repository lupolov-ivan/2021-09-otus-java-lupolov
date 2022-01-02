package otus.java.lupolov.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import otus.java.lupolov.model.Measurement;

import java.io.IOException;

public class MeasurementDeserializer extends StdDeserializer<Measurement> {

    public MeasurementDeserializer() {
        this(null);
    }

    protected MeasurementDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Measurement deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {

        JsonNode node = parser.getCodec().readTree(parser);

        String name = node.get("name").asText();
        double value = node.get("value").asDouble();

        return new Measurement(name, value);
    }
}
