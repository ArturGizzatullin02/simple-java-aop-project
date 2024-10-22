package ru.artur.project.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MainAspect {
    @Before("@annotation(ru.artur.project.annotation.LogBefore)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        System.out.println("\n[MAIN ASPECT BEFORE] Before calling method " + joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(pointcut = "@annotation(ru.artur.project.annotation.LogException)", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        System.out.println("[MAIN ASPECT AFTER THROWING] From calling method " + joinPoint.getSignature().toShortString()
                + " threw exception: " + ex);
    }

    @AfterReturning(pointcut = "@annotation(ru.artur.project.annotation.HandleReturning)", returning = "result")
    public void handleReturning(JoinPoint joinPoint, Object result) {
        System.out.println("[MAIN ASPECT AFTER RETURNING] From calling method " + joinPoint.getSignature().toShortString());
        System.out.println("[MAIN ASPECT AFTER RETURNING] Result is: " + result);
    }

    @Around("@annotation(ru.artur.project.annotation.HandleException)")
    public Object handleException(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            System.out.println("[MAIN ASPECT AROUND] From calling method " + joinPoint.getSignature().toShortString()
            + " threw exception: " + ex);
        }
        return result;
    }
}
