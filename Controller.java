package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int rows=6;
	private static final int columns=7;
	private static final int circledia=80;
	private static final String disccolor1="#24303E";
	private static final String disccolor2="#4CAA88";
	private boolean isinsertiondiscallowed=true;

	private static String player1="Player One";
	private static String player2="Player Two";
	private boolean isplayer1=true;
	private static Disc[][] inserteddiscArray=new Disc[rows][columns];


	@FXML
	public GridPane rootgrid;
	public Pane insertedDiscPane;
	public Label label1;
	public TextField firstplayer;
	public TextField secondplayer;
	public Button submit;

public void setname(){
	submit.setOnAction(event -> {
		player1=firstplayer.getText();
		player2=secondplayer.getText();
		label1.setText(player1);
	});
}

	public void createground(){

		Shape rectwithholes=creategamestructure();
		rootgrid.add(rectwithholes,0,1);
		List<Rectangle> rectangleList=createclickablereactangle();
		for (Rectangle rectangle:rectangleList
		     ) {
			rootgrid.add(rectangle,0,1);
		}


	}

	private Shape creategamestructure(){
		setname();
		Shape rect=new Rectangle((columns+1)*circledia,(rows+1)*circledia);

		for(int row=0;row<rows;row++)
		{
			for (int column=0;column<columns;column++)
			{
				Circle circle=new Circle();
				circle.setRadius(circledia/2);
				circle.setCenterY(circledia/2);
				circle.setCenterX(circledia/2);

				circle.setTranslateX(column*(circledia+5)+circledia/4);
				circle.setTranslateY(row*(circledia+5)+circledia/4);
				circle.setSmooth(true);
				rect=Shape.subtract(rect,circle);

			}
		}



		rect.setFill(Color.WHITE);
		return rect;
	}


	private List<Rectangle> createclickablereactangle(){
		List<Rectangle> rectangleList=new ArrayList<>();

		for (int col=0;col<columns;col++)
		{Rectangle rectangle=new Rectangle(circledia,(rows+1)*circledia);
		rectangle.setFill(Color.TRANSPARENT);
		rectangle.setTranslateX(col*(circledia+5)+circledia/4);
		rectangle.setOnMouseEntered(event -> rectangle.setFill(Paint.valueOf("#eeeeee26")));
		rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
			int finalCol = col;
			rectangle.setOnMouseClicked(event -> {
				if(isinsertiondiscallowed)
				{
					isinsertiondiscallowed=false;
					insertdisc(new Disc(isplayer1), finalCol);}
		});
			rectangleList.add(rectangle);
		}

 return rectangleList;
	}

private  void insertdisc(Disc disc,  int finalCol)
	{
		int row=rows-1;
		while (row>=0)
		{

			if(ifdiscgetpresent(row,finalCol)==null)
				break;
			row--;
		}
		if(row<0)
			return;
		inserteddiscArray[row][finalCol]=disc;
		insertedDiscPane.getChildren().add(disc);
		disc.setTranslateX(finalCol*(circledia+5)+circledia/4);
		TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row*(circledia+5)+circledia/4);
		int finalRow = row;
		translateTransition.setOnFinished(event -> {
				isinsertiondiscallowed=true;
			if(gameended(finalRow,finalCol)){

				gameover();
			}
			isplayer1=!isplayer1;
			label1.setText(isplayer1?player1:player2);
		});
		translateTransition.play();

	}

	private void gameover() {
		String winner=isplayer1?player1:player2;
		System.out.println("winner is"+winner);
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four");
		alert.setHeaderText("The WINNER is "+winner);
		alert.setContentText(" Want to play again ? ");
		ButtonType yesbtn=new ButtonType("Yes");
		ButtonType nobtn=new ButtonType("No");
		alert.getButtonTypes().setAll(yesbtn,nobtn);
		Platform.runLater(()->{Optional<ButtonType> clkdbtn=alert.showAndWait();
			if (clkdbtn.isPresent()&&clkdbtn.get()==yesbtn)
			{
				resetgame();
			}
			else
			{
				Platform.exit();
				System.exit(0);

			}});

	}

	public void resetgame() {
		insertedDiscPane.getChildren().clear();
		for (int i = 0; i <rows ; i++) {
			for (int j = 0; j <columns ; j++) {

				inserteddiscArray[i][j]=null;
			}

		}

		isplayer1=true;
		label1.setText(player1);
		createground();
	}

	private boolean gameended(int finalRow, int finalCol)
	{
		List<Point2D> verticalpoint=IntStream.rangeClosed(finalRow-3,finalRow+3).
				mapToObj(r-> new Point2D(r,finalCol))
				.collect(Collectors.toList());

		List<Point2D> horizontalpoint=IntStream.rangeClosed(finalCol-3,finalCol+3).
				mapToObj(c-> new Point2D(finalRow,c))
				.collect(Collectors.toList());
		Point2D startpoint1=new Point2D(finalRow-3,finalCol+3);
		List<Point2D> diagonal1=IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint1.add(i,-i))
				.collect(Collectors.toList());

		Point2D startpoint2=new Point2D(finalRow-3,finalCol-3);
		List<Point2D> diagonal2=IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint2.add(i,i))
				.collect(Collectors.toList());



		boolean isended =checkCombinations(verticalpoint)||checkCombinations(horizontalpoint)
				||checkCombinations(diagonal1)||checkCombinations(diagonal2);
		return isended;

	}

	private boolean checkCombinations(List<Point2D> points) {

		int chain=0;
		for (Point2D point:points)
		{
			int rowindexforarray= (int) point.getX();
			int columnindexforarray= (int) point.getY();
			Disc disc=ifdiscgetpresent(rowindexforarray,columnindexforarray);
			if(disc!=null && disc.isplayer1onmove == isplayer1)
			{
				chain++;
				if(chain==4)
					return true;
			}
			else
				chain=0;
		}
			return false;
	}
	private Disc ifdiscgetpresent(int row,int col){
		if(row<0||row>=rows||col<0||col>=columns)
			return null;
		else
			return inserteddiscArray[row][col];
	}


	private static class Disc extends Circle{
		private final boolean isplayer1onmove;
		Disc(boolean isplayer1onmove){
			this.isplayer1onmove=isplayer1onmove;
			setRadius(circledia/2);

			setFill(isplayer1onmove?Color.valueOf(disccolor1):Color.valueOf(disccolor2));
			setCenterX(circledia/2);
			setCenterY(circledia/2);

		}
}
	@Override
	public void initialize(URL location, ResourceBundle resources) {


	}
}
