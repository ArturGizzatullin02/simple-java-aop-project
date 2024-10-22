package ru.artur.project.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Aspect
@Component
public class MainAspect {
//    @Before("execution(* ru.artur.project.service.MainService.greeting(..))")
//    public void logBefore(JoinPoint joinPoint) {
//        System.out.println("Before calling method " + joinPoint.getSignature().getName());
//    }
//
//    @Before("within(ru.artur.project.service.MainService)")
//    public void logBefore(JoinPoint joinPoint) {
//        System.out.println("Before calling method " + joinPoint.getSignature().getName());
//    }
//
//    @Before("within(ru.artur.project.service..*)")
//    public void logBefore(JoinPoint joinPoint) {
//        System.out.println("Before calling method " + joinPoint.getSignature().getName());
//    }

    @Before("@annotation(ru.artur.project.annotation.LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before calling method " + joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "@annotation(ru.artur.project.annotation.LogException)",
            throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        System.out.println("Method " + joinPoint.getSignature().getName() + " throwing exception: " + ex);
    }

    @AfterReturning(pointcut = "@annotation(ru.artur.project.annotation.HandlingResult)",
            returning = "result")
    public void handleReturning(JoinPoint joinPoint, List<String> result) {
        System.out.println("Method was calling: " + joinPoint.getSignature().toShortString());
        System.out.println("Result: " + result);
        System.out.println("More info: ");
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(System.out::println);
        }
        System.out.println("End of method: " + joinPoint.getSignature().toShortString());
    }

    @Around("@annotation(ru.artur.project.annotation.LogTracking)")
    public Object logExecTimeAround(ProceedingJoinPoint joinPoint) {
        System.out.println("Arround calling method " + joinPoint.getSignature().toShortString());

        long startTime = System.currentTimeMillis();

        Object result = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Arround finish method " + joinPoint.getSignature().toShortString());
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
        return result;
    }
}
