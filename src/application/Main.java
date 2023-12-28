package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	
	private static void sqlite_init() throws DbException {
        Statement st = null;
        try{
            Connection conn = DB.getConnection();
            st = conn.createStatement();
            st.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS Question(
                    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
                    "text" TEXT NOT NULL,
                    "active" BOOLEAN
                    );
                    CREATE TABLE IF NOT EXISTS Report(
                    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
                    "sent" DATETIME
                    );
                    CREATE TABLE IF NOT EXISTS Answer(
                    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
                    "text" TEXT NOT NULL,
                    "questionId" INTEGER,
                    "reportId" INTEGER,
                    CONSTRAINT fk_question FOREIGN KEY("questionId") REFERENCES Question("id"),
                    CONSTRAINT fk_report FOREIGN KEY("reportId") REFERENCES Report("id")
                    );
                    """);
        } catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
	
	public static Scene getMainScene(){
        return mainScene;
    }
	
	@Override
	public void start(Stage stage) throws Exception {
		try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            ScrollPane root = fxmlLoader.load();

            mainScene = new Scene(root);
            stage.setTitle("Hello!");
            stage.setScene(mainScene);
            stage.show();
        } catch(IOException e){
            throw new MainException(e.getMessage());
        } catch (RuntimeException e) {
            throw new MainRuntimeException(e.getMessage());
        }
	}

	public static void main(String[] args) {
		try{
            sqlite_init();
            launch(args);
        } catch(DbException e){
            System.out.println("Error: " + e.getMessage());
        } catch(RuntimeException e){
            System.out.println("Error: " + e.getMessage());
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        } finally {
        	DB.closeConnection();
        }
	}
}
