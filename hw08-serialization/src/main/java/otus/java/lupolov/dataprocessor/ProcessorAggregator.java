package otus.java.lupolov.dataprocessor;

import otus.java.lupolov.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream()
                .collect(groupingBy(Measurement::getName, TreeMap::new, summingDouble(Measurement::getValue)));
    }
}
