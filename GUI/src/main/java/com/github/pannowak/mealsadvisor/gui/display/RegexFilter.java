package com.github.pannowak.mealsadvisor.gui.display;

import javafx.scene.control.TextFormatter.Change;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class RegexFilter implements UnaryOperator<Change> {

    private final Pattern allowedPattern;

    public RegexFilter(Pattern allowedPattern) {
        this.allowedPattern = allowedPattern;
    }

    @Override
    public Change apply(Change change) {
        var newText = change.getControlNewText();
        return allowedPattern.matcher(newText).matches() ? change : null;
    }
}
