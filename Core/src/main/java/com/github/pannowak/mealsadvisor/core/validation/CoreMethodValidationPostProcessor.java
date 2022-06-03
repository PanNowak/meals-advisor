package com.github.pannowak.mealsadvisor.core.validation;

import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Component
class CoreMethodValidationPostProcessor extends MethodValidationPostProcessor {

    private final ExceptionFactory exceptionFactory;

    CoreMethodValidationPostProcessor(@Value("${spring.aop.proxy-target-class:true}") boolean proxyTargetClass,
                                      Validator validator, ExceptionFactory exceptionFactory) {
        setProxyTargetClass(proxyTargetClass);
        setValidator(validator);
        this.exceptionFactory = exceptionFactory;
    }

    @Override
    protected Advice createMethodValidationAdvice(Validator validator) {
        var interceptor = new MethodValidationInterceptor(validator);
        return new RethrowingDecorator(interceptor);
    }

    private final class RethrowingDecorator implements MethodInterceptor {

        private final MethodInterceptor delegate;

        RethrowingDecorator(MethodInterceptor delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            try {
                return delegate.invoke(invocation);
            } catch (ConstraintViolationException e) {
                throw exceptionFactory.validationException(e);
            }
        }
    }
}
