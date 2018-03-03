package com.stackroute.activitystream.aspect;
  /* Each of the methods of DAOImpls has to be used in the given code snippet, any particular method will have all the four aspectJ annotation(@Before, 
  @After, @AfterReturning, @AfterThrowing). Note: Provided is a sample using a single method, similarly you need to write for all the methods of 
  DAOImpls. */

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
 public class DAOLoggingAspect {

private static final Logger logger = LoggerFactory.getLogger(DAOLoggingAspect.class);

@Before("execution(* com.stackroute.activitystream.daoimpl.UserDAOImpl.validate(..))")
public void logBeforeValidateUser(JoinPoint joinPoint) {

    logger.info("============@Before==========");
    logger.debug("Method Name : " + joinPoint.getSignature().getName());
    logger.debug("*********************************");

}

@After("execution(* com.stackroute.activitystream.daoimpl.UserDAOImpl.validate(..))")
public void logAfterValidateUser(JoinPoint joinPoint) {

    logger.info("============@After==========");
    logger.debug("Method Name : " + joinPoint.getSignature().getName());
    logger.debug("Method arguments : " + Arrays.toString(joinPoint.getArgs()));
    logger.debug("*********************************");

}

@AfterReturning(pointcut = "execution(* com.stackroute.activitystream.daoimpl.UserDAOImpl.validate(..))", returning = "result")
public void logAfterReturningValidateUser(JoinPoint joinPoint, Object result) {

    logger.debug("============@AfterReturning==========");
    logger.debug("Method Name : " + joinPoint.getSignature().getName());
    logger.debug("Method arguments : " + Arrays.toString(joinPoint.getArgs()));
    logger.debug("*********************************");

}

@AfterThrowing(pointcut = "execution(* com.stackroute.activitystream.daoimpl.UserDAOImpl.validate(..))", throwing = "error")
public void logAfterThrowingValidateUser(JoinPoint joinPoint, Throwable error) {

    logger.info("============@AfterThrowing==========");
    logger.debug("Method Name : " + joinPoint.getSignature().getName());
    logger.debug("Method arguments : " + Arrays.toString(joinPoint.getArgs()));
    logger.debug("Exception : " + error);
    logger.debug("*********************************");
}
}