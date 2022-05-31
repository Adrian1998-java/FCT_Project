package fct.shopbasket.app;

import fct.shopbasket.controllers.ListsOfListController;
import fct.shopbasket.controllers.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	LoginController loginController;
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		loginController = new LoginController();
		
		stage = primaryStage;
		
		primaryStage.setScene(new Scene(loginController.getView(), 400, 300));
		primaryStage.setTitle("Shop Basket");
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		App.stage = stage;
	}

	public static void main(String[] args) {
		launch(args);

	}

}
