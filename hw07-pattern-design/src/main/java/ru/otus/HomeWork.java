package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.homework.EvenSecondThrowableProcessor;
import ru.otus.processor.homework.SwapField11AndField12Processor;
import ru.otus.processor.homework.DateTimeProviderImpl;

import java.util.List;
import java.util.function.Consumer;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {

        var processors = List.of(
                new SwapField11AndField12Processor(),
                new LoggerProcessor(new EvenSecondThrowableProcessor(new DateTimeProviderImpl())));

        Consumer<Exception> errorHandler = ex -> System.out.printf("Exception handled: %s%n", ex.getMessage());

        var complexProcessor = new ComplexProcessor(processors, errorHandler);

        var listenerPrinterConsole = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();

        complexProcessor.addListener(listenerPrinterConsole);
        complexProcessor.addListener(historyListener);

        var objMsg = new ObjectForMessage();
        objMsg.setData(List.of("data"));

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(objMsg)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinterConsole);
        complexProcessor.removeListener(historyListener);
    }
}
