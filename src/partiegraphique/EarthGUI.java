package partiegraphique;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class EarthGUI extends Application {
	//ne pas oublier de rajouter : fx:controller="partiegraphique.Controller"
	//dans la ligne du VBOX du .fxml (première ligne)
	//et aussi fx:id="pane3D" dans le Pane dans du .fxml
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent content = FXMLLoader.load(getClass().getResource("gui2D.fxml"));
			primaryStage.setTitle("Global Warming 3D");
			primaryStage.setScene(new Scene(content));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
