package com.github.pannowak.mealsadvisor.core.context;

import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Locale;

@Component
class ContextManager {

    private static final String CONTEXT_PROPAGATION = "CONTEXT_PROPAGATION";

    private final LocaleContextManager localeContextManager;

    ContextManager(LocaleContextManager localeContextManager) {
        this.localeContextManager = localeContextManager;
    }

    @PostConstruct
    void initialize() {
        Hooks.onEachOperator(CONTEXT_PROPAGATION,
                Operators.lift((s, context) -> new ContextPropagatingSubscriber(context)));
    }

    @PreDestroy
    void cleanUp() {
        Hooks.resetOnEachOperator(CONTEXT_PROPAGATION);
    }

    private final class ContextPropagatingSubscriber implements CoreSubscriber<Object> {

        private final CoreSubscriber<Object> delegate;

        ContextPropagatingSubscriber(CoreSubscriber<Object> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void onSubscribe(Subscription s) {
            setCurrentLocale();
            delegate.onSubscribe(s);
        }

        @Override
        public void onNext(Object object) {
            setCurrentLocale();
            delegate.onNext(object);
        }

        @Override
        public void onComplete() {
            delegate.onComplete();
        }

        @Override
        public void onError(Throwable t) {
            delegate.onError(t);
        }

        @Override
        public Context currentContext() {
            return delegate.currentContext();
        }

        private void setCurrentLocale() {
            Locale currentLocale = currentContext().getOrDefault(Locale.class, Locale.ENGLISH);
            localeContextManager.setCurrentLocale(currentLocale);
        }
    }

}
