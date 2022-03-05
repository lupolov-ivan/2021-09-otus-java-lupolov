package ru.otus.appcontainer;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.api.ComponentInitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Comparator.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    public static final String COMPONENT_INIT_ERR_MSG
            = "Error during initialization component with type [%s]. Not found necessary component with type [%s]";

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packageName) {
        processConfig(findConfigClasses(packageName));
    }

    @SneakyThrows
    private void processConfig(Class<?>... configClasses) {

        checkConfigClass(configClasses);

        List<Class<?>> orderedConfigClasses = Arrays.stream(configClasses)
                .sorted(comparing(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        for (Class<?> configClass : orderedConfigClasses) {

            Constructor<?> constructor = configClass.getDeclaredConstructor();
            Object configInstance = constructor.newInstance();

            List<Method> factoryMethods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(AppComponent.class))
                    .sorted(comparing(method -> method.getAnnotation(AppComponent.class).order()))
                    .toList();

            for (Method method : factoryMethods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] args = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    Object appComponent = getAppComponent(parameterTypes[i]);

                    if (appComponent == null) {
                        String errMsg = String.format(COMPONENT_INIT_ERR_MSG, method.getReturnType(), parameterTypes[i]);
                        throw new ComponentInitializationException(errMsg);
                    }

                    args[i] = appComponent;
                }

                Object component = method.invoke(configInstance, args);
                appComponents.add(component);

                AppComponent annotation = method.getAnnotation(AppComponent.class);
                appComponentsByName.put(annotation.name(), component);
            }
        }
    }

    private Class<?>[] findConfigClasses(String packageName) {
        var reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        return classes.toArray(Class[]::new);
    }

    private void checkConfigClass(Class<?>... configClasses) {
        for (Class<?> configClass : configClasses) {
            if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
            }
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(obj -> componentClass.isAssignableFrom(obj.getClass()) || componentClass.isInstance(obj))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
