package edu.senla.aspect;

import edu.senla.helper.ConnectionHolder;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Aspect
@Component
@AllArgsConstructor
public class TransactionAspect {

    private ConnectionHolder connectionHolder;

    @Pointcut("@annotation(edu.senla.annotation.Transaction)")
    public void transactionAspect() {
    }

    @Around("transactionAspect()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        final Connection connection = connectionHolder.getConnection();
        try {
            connection.setAutoCommit(false);
            Object proceed = joinPoint.proceed();
            connection.commit();
            return proceed;
        } catch (RuntimeException e) {
            connection.rollback();
            throw e;
        } catch (Exception e) {
            connection.commit();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @AfterThrowing(value = "transactionAspect()", throwing = "e")
    public void doAfterThrowing(ProceedingJoinPoint joinPoint, Throwable e) {
        e.printStackTrace();
    }

}
