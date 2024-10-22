package ru.artur.project.service;

import org.springframework.stereotype.Service;
import ru.artur.project.annotation.HandleException;
import ru.artur.project.annotation.LogException;
import ru.artur.project.annotation.HandleReturning;
import ru.artur.project.annotation.LogBefore;
import ru.artur.project.exception.DivisionByZeroException;
import ru.artur.project.exception.RootOfANegativeNumberException;


@Service
public class MainService {

    public MainService() {
    }

    @LogBefore
    public Double getSum(Double a, Double b) {
        Double sum = a + b;
        System.out.println("Sum of " + a + " and " + b + " is " + sum);
        return sum;
    }

    @HandleReturning
    @LogBefore
    public Double getDifference(Double a, Double b) {
        Double difference = a - b;
        System.out.println("Difference of " + a + " and " + b + " is " + difference);
        return difference;
    }

    @LogBefore
    public Double getProduct(Double a, Double b) {
        Double product = a * b;
        System.out.println("Product of " + a + " and " + b + " is " + product);
        return product;
    }

    // пример с логированием, но дальнейшим пробросом исключения
    @LogException
    @LogBefore
    public Double getDivision(Double a, Double b) {
        Double division = a / b;
        if (b == 0) {
            throw new DivisionByZeroException("Division by zero is not allowed");
        }
        System.out.println("Division of " + a + " and " + b + " is " + division);
        return division;
    }

    // пример с обработкой, без дальнейшего проброса исключения
    @HandleException
    @LogException
    @LogBefore
    public Double getSquareRoot(Double a) {
        if (a < 0) {
            throw new RootOfANegativeNumberException("Root of a negative number is not allowed now");
        }
        System.out.println("Square root of " + a + " is " + Math.sqrt(a));
        return Math.sqrt(a);
    }
}
