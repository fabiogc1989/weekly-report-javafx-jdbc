package gui.listener;

import gui.UpdatableController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchButtonActionListener implements EventHandler<ActionEvent> {
	private UpdatableController controller;
	
	public SearchButtonActionListener(UpdatableController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(ActionEvent event) {
		controller.update();
		event.consume();
	}

}
