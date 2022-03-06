package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.appcontainer.api.*;

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

    private void processConfig(Class<?>... configClasses) {

        checkConfigClass(configClasses);

        List<Class<?>> orderedConfigClasses = Arrays.stream(configClasses)
                .sorted(comparing(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        for (Class<?> configClass : orderedConfigClasses) {

            Object configInstance = ReflectionUtils.getConfigInstance(configClass);

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

                Object component = ReflectionUtils.getComponentInstance(configInstance, method, args);
                AppComponent componentMetaData = method.getAnnotation(AppComponent.class);
                String componentName = componentMetaData.name();

                if (!appComponentsByName.containsKey(componentName)) {
                    appComponentsByName.put(componentName, component);
                    appComponents.add(component);
                }
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
