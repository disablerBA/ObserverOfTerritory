package ObserverOfTerritory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
	private Button forwardButton;
	private Button playButton;
	private Button multipleX1button;
	private Button multipleX3button;
	private Button multipleX10button;
	private Label instantKpiLabel;
	private Label solutionKpiLabel;
	private Viewer viewer;
	private TestKit tk;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
		/*TestKit tk = new TestKit(100, 2, 5, 8);
		tk.startTest();*/
	}
	
	public void start(Stage stage)
	{		
		tk = new TestKit(200, 30, 50, 50);
		tk.addListener(this);
		
		stage.setTitle("����� ������������");
		HBox rootNode = new HBox(10);
		rootNode.setAlignment(Pos.TOP_LEFT);
		
		//�������
		ScrollPane scrollPane = new ScrollPane();
		viewer = new Viewer(50, 50);
		viewer.paint(tk.getRobots());
		scrollPane.setContent(viewer);
		//scrollPane.setPannable(true);
		
		//�������
		TabPane tabPane = new TabPane();
		Tab dataTab = new Tab("�������� ������");
		Tab simTab = new Tab("�������������");
		dataTab.setClosable(false);
		simTab.setClosable(false);
		tabPane.getTabs().addAll(dataTab, simTab);
		
		//������� �������� ������
		HBox paneData = new HBox();
		paneData.setPadding(new Insets(10));
		VBox paneLabel = new VBox(20);
		VBox paneTextField = new VBox(10);
		Label satMaxLabel = new Label("����.����.");
		Label stepsLabel = new Label("���-�� �����");
		Label coeffLabel = new Label("����. ������.");
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
				stepsLabel2.setText("��� "+tk.getTime()+" �� "+tk.getDuration());
			}
		});
		
		
		TextField rateField = new TextField(String.valueOf(0.03));
		rateField.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event)
			{
				while ( !rateField.getText().matches("\\d*\\.\\d*") )
				{
					rateField.setText("");
				}
				tk.getTerritory().changeRateDecrement(Double.parseDouble(rateField.getText()));
			}
		});
		
		paneTextField.getChildren().addAll(satMaxField, stepsField, rateField);
		paneData.getChildren().addAll(paneLabel, paneTextField);
		dataTab.setContent(paneData);
		
		//������� �������������
		GridPane paneSim = new GridPane();
		paneSim.setPadding(new Insets(10));
		paneSim.setVgap(10);
		//paneSim.setHgap(10);
		Label satMaxLabel2 = new Label("����.����. = 1");
		instantKpiLabel = new Label("");
		solutionKpiLabel = new Label("");
		stepsLabel2 = new Label("��� "+tk.getTime()+" �� "+tk.getDuration());
		Label coeffLabel2 = new Label("����. ������. = 0.03");
		multipleX1button = new Button("x1");
		multipleX1button.setDisable(true);
		multipleX1button.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				tk.setMultipleAcceleration(1);
				multipleX1button.setDisable(true);
				multipleX3button.setDisable(false);
				multipleX10button.setDisable(false);
			}
		});
		
		multipleX3button = new Button("x3");
		multipleX3button.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				tk.setMultipleAcceleration(3);
				multipleX1button.setDisable(false);
				multipleX3button.setDisable(true);
				multipleX10button.setDisable(false);
			}
		});
		
		multipleX10button = new Button("x10");
		multipleX10button.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				tk.setMultipleAcceleration(10);
				multipleX1button.setDisable(false);
				multipleX3button.setDisable(false);
				multipleX10button.setDisable(true);
			}
		});
		Button backButton = new Button("�����");
		backButton.setDisable(true);
		forwardButton = new Button("������");
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
				try {
					tk.step();
					//stepsLabel2.setText("��� "+tk.getTime()+" �� "+tk.getDuration());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		playButton = new Button("�����");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if ( playButton.getText().equals("�����"))
				{
					playButton.setText("�����");
					System.out.println("����� �����");
					tk.isPause = false;
					synchronized(tk.getPlayThread())
					{
						tk.getPlayThread().notify();
					}
					
				} else
				{
					playButton.setText("�����");
					tk.isPause = true;
				}
			}
		});
		paneSim.add(satMaxLabel2,0,0);
		paneSim.add(coeffLabel2,0,1);
		paneSim.add(stepsLabel2,0,2);
		paneSim.add(backButton,0,3);
		paneSim.add(forwardButton,1,3);
		paneSim.add(playButton,0,4);
		paneSim.add(multipleX1button,0,5);
		paneSim.add(multipleX3button,1,5);
		paneSim.add(multipleX10button,2,5);
		
		
		paneSim.add(instantKpiLabel, 0, 6);
		GridPane.setColumnSpan(instantKpiLabel, 3);
		paneSim.add(solutionKpiLabel, 0, 7);
		GridPane.setColumnSpan(solutionKpiLabel, 3);
		
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
				satMaxLabel2.setText("����.����. = "+tk.getSaturationMax());
			}
		});
		
		dataTab.setOnSelectionChanged(new EventHandler<Event>() 
		{
			public void handle(Event event)
			{
				System.out.println("����");
				instantKpiLabel.setText("KPI �����: "+Math.rint(tk.getInstantKpi() * 1000 ) / 1000);
				solutionKpiLabel.setText("KPI �������: "+Math.rint(tk.getSolutionKpi() * 1000) / 1000) ;
				
				if ( dataTab.isSelected() )
				{
					tk.reset();
				}
			}
		});
		
		//��������� ����� ��� �����
		rootNode.getChildren().addAll(scrollPane, tabPane);
		
		Scene scene = new Scene(rootNode, 760, 550);
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
				stepsLabel2.setText("��� "+tk.getTime()+" �� "+tk.getDuration());
				viewer.paint(tk.getRobots());
				instantKpiLabel.setText("KPI �����: "+Math.rint(tk.getInstantKpi() * 1000) / 1000 ); 
				solutionKpiLabel.setText("KPI �������: "+Math.rint( (tk.getSolutionKpi()/(tk.getTime()+1)) * 1000 ) / 1000 ) ;
				
				if ( tk.getTime() == tk.getDuration() )
				{
					playButton.setDisable(true);
					playButton.setText("�����");
					forwardButton.setDisable(true);
					tk.isPause = true;
				} else
				{
					playButton.setDisable(false);
					forwardButton.setDisable(false);
				}
				//Platform.exit();
			}
		});
	}
	
	/*@Override
	public void stop()
	{
		;
	}*/
}
