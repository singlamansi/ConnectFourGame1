package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("connect4.fxml"));
        GridPane grid=loader.load();
        controller=loader.getController();
        controller.createground();
        MenuBar menu=createmenu();
        menu.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menupane= (Pane) grid.getChildren().get(0);
        menupane.getChildren().addAll(menu);
        Scene scene=new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    private MenuBar  createmenu(){
        Menu file=new Menu("File");
        MenuItem newgame=new MenuItem("New Game");
        newgame.setOnAction(event -> {controller.resetgame();});
        MenuItem resetgame=new MenuItem("Reset Game");
        resetgame.setOnAction(event -> {controller.resetgame();});
        SeparatorMenuItem sep =new SeparatorMenuItem();
        MenuItem exitgame=new MenuItem("Exit Game");
        exitgame.setOnAction(event -> {exitgamee();});
        file.getItems().addAll(newgame,resetgame,sep,exitgame);

        Menu help=new Menu("Help");
        MenuItem aboutgame=new MenuItem("About Game");
        aboutgame.setOnAction(event -> {aboutgamee();});
        MenuItem aboutus=new MenuItem("About Developer");
        aboutus.setOnAction(event -> {aboutusgame();});
        SeparatorMenuItem sep1=new SeparatorMenuItem();
        help.getItems().addAll(aboutgame,sep1,aboutus);
        MenuBar menubar=new MenuBar();
        menubar.getMenus().addAll(file,help);
        return menubar;
    }

    private void aboutusgame() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("About the game develpor ");
        alert.setTitle("Developers information");
        alert.setContentText("This game is developed by Mansi Singla in 2018.");
        alert.show();
    }

    private void aboutgamee() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Game");
        alert.setHeaderText("What is Connectfour? ");
        alert.setContentText("Connect Four is a two-player connection game in which the " +
                "players first choose a color and then take turns dropping colored discs " +
                "from the top into a seven-column, six-row vertically suspended grid. "+
                "The pieces fall straight down, occupying the next available space within the column. "+
                "The objective of the game is to be the first to form a horizontal, vertical, " +
                "or diagonal line of four of one's own discs. Connect Four is a solved game. " +
                "The first player can always win by playing the right moves.");
        alert.show();

    }


    private void exitgamee() {
        Platform.exit();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
