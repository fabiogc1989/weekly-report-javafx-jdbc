package gui.listener;

import gui.DeletableController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;

public class DeleteButtonActionListener<T> implements EventHandler<ActionEvent> {
	private DeletableController<T> controller;
	private TableCell<T, Void> tblCell;
	
	public DeleteButtonActionListener(DeletableController<T> controller, TableCell<T, Void> tblCell) {
		this.controller = controller;
		this.tblCell = tblCell;
	}

	@Override
	public void handle(ActionEvent event) {
		controller.delete(tblCell.getTableView().getItems().get(tblCell.getIndex()));
	}

}
