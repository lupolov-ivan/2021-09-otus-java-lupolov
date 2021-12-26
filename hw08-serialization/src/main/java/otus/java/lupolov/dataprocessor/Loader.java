package otus.java.lupolov.dataprocessor;

import otus.java.lupolov.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
