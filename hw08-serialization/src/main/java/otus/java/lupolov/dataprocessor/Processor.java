package otus.java.lupolov.dataprocessor;

import otus.java.lupolov.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
