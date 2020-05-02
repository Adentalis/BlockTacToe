package appV2;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import contractUtils.GasLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BlockTacToeAppV2 extends Application {

	private UserInfo userInfo;
	private GasLogger gaslogger;
	private GameSceneController gameSceneController;
	private HomeSceneController homeSceneController;
	private WelcomeSceneController welcomeSceneController;
	private Stage primaryStage;
	private static final Logger log = LoggerFactory.getLogger(BlockTacToeAppV2.class);
	
	
	
	private void initializeScenes() {
		userInfo = new UserInfo();
		gaslogger = new GasLogger();
		welcomeSceneController = new WelcomeSceneController(gaslogger, userInfo, this);
		homeSceneController = new HomeSceneController(gaslogger, userInfo, this);
		gameSceneController = new GameSceneController(gaslogger, userInfo, this);
	}
	
	public void switchToHomeScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScene.fxml"));
		loader.setController(homeSceneController);
		Parent root = loader.load();	
		Scene scene = new Scene(root);
			
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();	
		primaryStage.show();
		log.info("Switched to Home Screen.");
	}
	
	public void switchToGameScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
		loader.setController(gameSceneController);
		Parent root = loader.load();
		Scene scene = new Scene(root);	
		
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();		
		primaryStage.show();	
		log.info("Switched to Game Screen.");
	}
	
	public void switchToWelcomeScene() throws IOException {
		userInfo = new UserInfo();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomeScene.fxml"));
		loader.setController(welcomeSceneController);
		Parent root = loader.load();	
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("BlockTacToe");	
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();	
		log.info("Switched to Welcome Screen.");
	}
	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		initializeScenes();	
	
		primaryStage.setTitle("BlockTacToe");
		switchToWelcomeScene();
	}
	
	public static void main(String[] args) {
		launch(BlockTacToeAppV2.class ,args);
	}
	
}
