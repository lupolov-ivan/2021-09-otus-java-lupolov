package otus.java.lupolov.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import otus.java.lupolov.model.Measurement;

import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper objectMapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
        var module = new SimpleModule();
        module.addDeserializer(Measurement.class, new MeasurementDeserializer());
        this.objectMapper.registerModule(module);
    }

    @Override
    public List<Measurement> load() {

        ClassLoader classLoader = this.getClass().getClassLoader();

        try(InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            return objectMapper.readerForListOf(Measurement.class).readValue(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}
