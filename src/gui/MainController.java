package gui;

import java.io.IOException;
import java.util.function.Consumer;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.QuestionService;

public class MainController {
	
	@FXML
    private void onQuestionListMenuItemClick(ActionEvent event) {
		loadView("/gui/QuestionListView.fxml", (QuestionListController controller) -> {
			controller.setService(new QuestionService());
			controller.update();
		});
    }

    @FXML
    private void onReportListMenuItemClick(ActionEvent event) {
    	loadView("/gui/ReportListView.fxml", (ReportListController controller) -> {});
    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingController){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());
            
            T controller = loader.getController();
            initializingController.accept(controller);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}