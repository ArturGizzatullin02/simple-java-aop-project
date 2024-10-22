package ru.artur.project;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.artur.project.service.MainService;

@ComponentScan
@EnableAspectJAutoProxy
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        MainService mainService = context.getBean(MainService.class);
        mainService.greeting();
        mainService.sayGoodByeFromMainService();

//        mainService.throwException(0);

        mainService.getString();

    }
}
