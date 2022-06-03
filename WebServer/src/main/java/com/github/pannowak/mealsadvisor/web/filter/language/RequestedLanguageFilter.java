package com.github.pannowak.mealsadvisor.web.filter.language;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Locale;

@Component
class RequestedLanguageFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var requestedLocale = resolveLocale(exchange.getLocaleContext());
        return chain.filter(exchange).contextWrite(Context.of(Locale.class, requestedLocale));
    }

    private Locale resolveLocale(LocaleContext localeContext) {
        var requestedLocale = localeContext.getLocale();
        return requestedLocale != null ? requestedLocale : Locale.ENGLISH;
    }
}
