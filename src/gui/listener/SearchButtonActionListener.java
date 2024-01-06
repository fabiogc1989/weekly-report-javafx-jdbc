package gui.listener;

import gui.PaginableController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchButtonActionListener implements EventHandler<ActionEvent> {
	private PaginableController controller;
	
	public SearchButtonActionListener(PaginableController controller) {
            this.controller = controller;
	}

	@Override
	public void handle(ActionEvent event) {
            controller.setPage(0);
            controller.update();
            event.consume();
	}

}
