package ru.artur.project.service;

import org.springframework.stereotype.Service;

@Service
public class SecondService {
    public void sayGoodByeFromSecondService() {
        System.out.println("Goodbye");
    }
}
