package com.github.pannowak.mealsadvisor.gui;

import com.github.pannowak.mealsadvisor.gui.views.main.controller.MainViewController;
import com.github.pannowak.springconfigurationprovider.SpringApplicationProvider;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MealsAdvisorGuiApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    public static final class JavaFxApplication extends Application {

        private ConfigurableApplicationContext springContext;

        @Override
        public void init() {
            springContext = new SpringApplicationBuilder(MealsAdvisorGuiApplication.class)
                    .sources(SpringApplicationProvider.getSources())
                    .run(getArgs());
        }

        @Override
        public void start(Stage stage) {
            FxWeaver fxWeaver = springContext.getBean(FxWeaver.class);
            Scene scene = new Scene(fxWeaver.loadView(MainViewController.class));
            setUpStage(stage, scene);
        }

        @Override
        public void stop() {
            springContext.close();
        }

        private String[] getArgs() {
            return getParameters().getRaw().toArray(String[]::new);
        }

        private void setUpStage(Stage stage, Scene scene) {
            stage.setTitle("Posiłkowy pomocnik");
            stage.setScene(scene);
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        }
    }
}
