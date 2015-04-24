package ObserverOfTerritory;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainClass extends Application implements EventListener {

	private Label stepsLabel2;
	private Button multipleX1button;
	private Button multipleX3button;
	private Button multipleX10button;
	private TestKit tk;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
		/*TestKit tk = new TestKit(100, 2, 5, 8);
		tk.startTest();*/
	}
	
	public void start(Stage stage)
	{		
		tk = new TestKit(100, 5, 50, 50);
		tk.addListener(this);
		
		stage.setTitle("Среда тестирования");
		HBox rootNode = new HBox(10);
		rootNode.setAlignment(Pos.TOP_LEFT);
		
		//полотно
		ScrollPane scrollPane = new ScrollPane();
		Territory ter = tk.getTerritory();
		ter.paint(tk.getRobots());
		scrollPane.setContent(ter);
		scrollPane.setPannable(true);
		
		//вкладки
		TabPane tabPane = new TabPane();
		Tab dataTab = new Tab("Исходные данные");
		Tab simTab = new Tab("Моделирование");
		dataTab.setClosable(false);
		simTab.setClosable(false);
		tabPane.getTabs().addAll(dataTab, simTab);
		
		//вкладка исходные данные
		HBox paneData = new HBox();
		paneData.setPadding(new Insets(10));
		VBox paneLabel = new VBox(20);
		VBox paneTextField = new VBox(10);
		Label satMaxLabel = new Label("Макс.удвл.");
		Label stepsLabel = new Label("Кол-во шагов");
		Label coeffLabel = new Label("Коэф. уменьш.");
		paneLabel.getChildren().addAll(satMaxLabel, stepsLabel, coeffLabel);
		TextField satMaxField = new TextField(String.valueOf(tk.getSaturationMax()));
		
		
		TextField stepsField = new TextField(String.valueOf(tk.getDuration()));
		stepsField.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				while ( !stepsField.getText().matches("\\d*") )
				{
					stepsField.setText("");
				}
				tk.changeTimeDuration(Integer.parseInt(stepsField.getText()));
				stepsLabel2.setText("Шаг "+tk.getTime()+" из "+tk.getDuration());
			}
		});
		
		
		TextField coeffField = new TextField(String.valueOf(0.03));
		
		paneTextField.getChildren().addAll(satMaxField, stepsField, coeffField);
		paneData.getChildren().addAll(paneLabel, paneTextField);
		dataTab.setContent(paneData);
		
		//вкладка моделирование
		GridPane paneSim = new GridPane();
		paneSim.setPadding(new Insets(10));
		paneSim.setVgap(10);
		//paneSim.setHgap(10);
		Label satMaxLabel2 = new Label("Макс.удвл. = 1");
		stepsLabel2 = new Label("Шаг "+tk.getTime()+" из "+tk.getDuration());
		Label coeffLabel2 = new Label("Коэф. уменьш. = 0.03");
		multipleX1button = new Button("x1");
		multipleX1button.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				tk.setMultipleAcceleration(1);
			}
		});
		
		multipleX3button = new Button("x3");
		multipleX3button.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				tk.setMultipleAcceleration(3);
			}
		});
		
		multipleX10button = new Button("x10");
		multipleX10button.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				tk.setMultipleAcceleration(10);
			}
		});
		Button backButton = new Button("Назад");
		Button forwardButton = new Button("Вперед");
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			synchronized public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				try {
					tk.step();
					//stepsLabel2.setText("Шаг "+tk.getTime()+" из "+tk.getDuration());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Button playButton = new Button("Старт");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			synchronized public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if ( playButton.getText().equals("Старт"))
				{
					playButton.setText("Пауза");
					System.out.println("Будим поток");
					tk.isPause = false;
					notify();
					
				} else
				{
					playButton.setText("Старт");
					
					tk.isPause = true;
				}
				
				
			}
		});
		paneSim.add(satMaxLabel2,0,0);
		paneSim.add(stepsLabel2,0,2);
		paneSim.add(coeffLabel2,0,1);
		paneSim.add(backButton,0,3);
		paneSim.add(forwardButton,1,3);
		paneSim.add(playButton,0,4);
		paneSim.add(multipleX1button,0,5);
		paneSim.add(multipleX3button,1,5);
		paneSim.add(multipleX10button,2,5);
		
		simTab.setContent(paneSim);
		
		satMaxField.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				while ( !satMaxField.getText().matches("\\d*") )
				{
					satMaxField.setText("");
				}
				tk.changeSaturationMax(Integer.parseInt(satMaxField.getText()));
				satMaxLabel2.setText("Макс.удвл. = "+tk.getSaturationMax());
			}
		});
		
		//установка детей для корня
		rootNode.getChildren().addAll(scrollPane, tabPane);
		
		Scene scene = new Scene(rootNode, 700, 700);
		stage.setScene(scene);
		
		
		
		stage.show();
		
		tk.play();
	}

	public void onTimeChange()
	{
		Platform.runLater(new Runnable() 
		{
			public void run()
			{
				stepsLabel2.setText("Шаг "+tk.getTime()+" из "+tk.getDuration());
			}
		});		
	}
}
