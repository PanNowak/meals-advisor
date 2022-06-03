package com.github.pannowak.mealsadvisor.gui.progress;

import javafx.scene.control.ProgressIndicator;

import java.util.Deque;
import java.util.LinkedList;

public final class ProgressIndicatorManager {

    private static final Deque<ProgressIndicator> progressIndicators = new LinkedList<>();

    public static void setCurrentProgressIndicator(ProgressIndicator progressIndicator) {
        progressIndicators.addFirst(progressIndicator);
    }

    public static void removeCurrentProgressIndicator() {
        progressIndicators.removeFirst();
    }

    public static void showCurrentProgressIndicator() {
        var currentProgressIndicator = progressIndicators.peekFirst();
        if (currentProgressIndicator != null) {
            System.out.println("Showing progress indicator on " + Thread.currentThread());
            currentProgressIndicator.setVisible(true);
        }
    }

    public static void hideCurrentProgressIndicator() {
        var currentProgressIndicator = progressIndicators.peekFirst();
        if (currentProgressIndicator != null) {
            System.out.println("Hiding progress indicator on " + Thread.currentThread());
            currentProgressIndicator.setVisible(false);
        }
    }
}
