package otus.java.lupolov.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import otus.java.lupolov.model.Measurement;

import java.io.File;
import java.net.URL;
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

        URL fileResource = this.getClass().getClassLoader().getResource(fileName);

        if (fileResource == null) {
            throw new FileProcessException("File not found");
        }
        try {
            return objectMapper.readerForListOf(Measurement.class).readValue(new File(fileResource.getFile()));
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}
