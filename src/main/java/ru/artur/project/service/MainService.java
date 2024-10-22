package ru.artur.project.service;

import org.springframework.stereotype.Service;
import ru.artur.project.annotation.HandlingResult;
import ru.artur.project.annotation.LogException;
import ru.artur.project.annotation.LogExecution;
import ru.artur.project.annotation.LogTracking;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {
    private final SecondService secondService;

    public MainService(SecondService secondService) {
        this.secondService = secondService;
    }

    @LogExecution
    @LogTracking
    public void greeting() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello from MainService");
    }

    public void sayGoodByeFromMainService() {
        secondService.sayGoodByeFromSecondService();
    }

    @LogExecution
    @LogException
    public void throwException(int value) {
        if (value == 0) {
            throw new IllegalStateException("Value is zero");
        }
        System.out.println("Value is " + value);
    }

    @HandlingResult
    public List<String> getString() {
        return new ArrayList<>(List.of("Hello", "from", "main", "service"));
    }
}
