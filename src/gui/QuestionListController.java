package gui;

import application.Contants;
import java.net.URL;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import model.dto.QuestionDto;
import model.service.QuestionService;
import gui.listener.*;
import java.util.ResourceBundle;
import model.dto.SearchModel;
import model.util.Pager;

public class QuestionListController implements Initializable, PaginableController, DeletableController<QuestionDto> {
    private static SearchModel<QuestionDto> searchParam = new SearchModel<>();
    
    private Pager<QuestionDto> pager;

    private QuestionService service;

    @FXML
    private TextField question_textField;

    @FXML
    private CheckBox isActive_checkbox;

    @FXML
    private Button searchBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pagination questionPagination;

    @FXML
    private Pane tableView_Pane;

    private TableView<QuestionDto> questionTableView;

    private TableColumn<QuestionDto, Void> actionsColumn;

    private TableColumn<QuestionDto, String> questionViewColumn;

    private TableColumn<QuestionDto, Boolean> isActiveViewColumn;

    private ObservableList<QuestionDto> obsList;

    public void setService(QuestionService service) {
        this.service = service;
    }

    private void initFilterFields() {
        question_textField.setText("");
        isActive_checkbox.setSelected(true);
    }
    
    private void createTableView(){
        questionTableView = new TableView<>();
        questionTableView.prefWidthProperty().bind(Main.getMainScene().getWindow().widthProperty());
        questionTableView.prefHeightProperty().bind(Main.getMainScene().getWindow().heightProperty());

        questionViewColumn = new TableColumn<>("Question");
        questionViewColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        questionViewColumn.setSortable(false);
        questionTableView.getColumns().add(questionViewColumn);

        isActiveViewColumn = new TableColumn<>("Is Active");
        isActiveViewColumn.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        isActiveViewColumn.setSortable(false);
        questionTableView.getColumns().add(isActiveViewColumn);
        
        DeletableController<QuestionDto> ctrl = this;
            Callback<TableColumn<QuestionDto, Void>, TableCell<QuestionDto, Void>> actionCellFactory = new Callback<>() {

                @Override
                public TableCell<QuestionDto, Void> call(TableColumn<QuestionDto, Void> param) {
                    final TableCell<QuestionDto, Void> cell = new TableCell<QuestionDto, Void>() {
                        private final Button delBtn = new Button("Del");
                        private final Button updBtn = new Button("Upd");

                        {
                            delBtn.setOnAction(new DeleteButtonActionListener<QuestionDto>(ctrl, this));
                            updBtn.setOnAction((ActionEvent event) -> {

                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                HBox box = new HBox();
                                box.setSpacing(5);
                                box.getChildren().addAll(delBtn, updBtn);
                                setGraphic(box);
                            }
                        }
                    };

                    return cell;
                }
            };
            actionsColumn = new TableColumn<>("");
            actionsColumn.setCellFactory(actionCellFactory);
            questionTableView.getColumns().add(actionsColumn);

            tableView_Pane.getChildren().clear();
            tableView_Pane.getChildren().add(questionTableView);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPane.setStyle("-fx-background-color:transparent;");

        //Filter fields
        initFilterFields();
        
        //TableView
        createTableView();

        // Pagination
        questionPagination.currentPageIndexProperty().addListener((obs, oldIdx, newIdx) ->{
            if (oldIdx.intValue() != newIdx.intValue()){
                setPage(newIdx.intValue());
                update();
            }
        });
        questionPagination.setCurrentPageIndex(0);
        // Buttons
        searchBtn.setOnAction(new SearchButtonActionListener(this));
        clearBtn.setOnAction(new ClearButtonActionListener(this));
    }

    @Override
    public void update() {
        QuestionDto question = new QuestionDto();
        question.setText(question_textField.getText());
        question.setActive(isActive_checkbox.isSelected());
        searchParam.setModel(question);
        
        Pager<QuestionDto> page = service.search(searchParam);
        int pagesCount = page.getTotalItems() % Contants.ITEMS_PER_PAGE == 0 ? page.getTotalItems() / Contants.ITEMS_PER_PAGE : page.getTotalItems() / Contants.ITEMS_PER_PAGE + 1;
        questionPagination.setPageCount(pagesCount);
        // int(int((i / t) * i))
        obsList = FXCollections.observableList(page.getItems());
        questionTableView.setItems(obsList);
    }

    @Override
    public void reset() {
        initFilterFields();
        update();
    }

    @Override
    public void delete(QuestionDto obj) {
        service.delete(obj);
    }

    @Override
    public void setPage(int page) {
        searchParam.setPage(page);
    }

}
