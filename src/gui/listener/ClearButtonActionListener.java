package gui.listener;

import gui.UpdatableController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ClearButtonActionListener implements EventHandler<ActionEvent> {
	private UpdatableController controller;
	
	public ClearButtonActionListener(UpdatableController controller) {
		this.controller = controller;
	}

	@Override
	public void handle(ActionEvent event) {
		controller.reset();
		event.consume();
	}

}
