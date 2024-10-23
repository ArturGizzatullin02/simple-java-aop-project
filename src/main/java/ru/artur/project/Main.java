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

        mainService.getSum(5.0, 2.0);
        mainService.getDivision(5.0, 2.0);
        mainService.getDifference(5.0, 2.0);
        mainService.getProduct(5.0, 2.0);
        mainService.getSquareRoot(4.0);
        mainService.getSquareRoot(-5.0);

        mainService.getDivision(5.0, 0.0);
    }
}
