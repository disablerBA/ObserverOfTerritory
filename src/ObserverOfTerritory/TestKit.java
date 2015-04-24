package ObserverOfTerritory;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** среда тестирования */
public class TestKit {

	public volatile boolean isPause = true;
	private int multipleAcceleration = 1;
	private Thread thread;
	private int time = 0; 
	private volatile int durationTime;	// продолжительность теста
	private Robot[] robots;		// массив роботов
	private Territory territory;	// территория
	private double kpiSolution1 = 0;	// KPIрешения для 1-го алгоритма
	private double kpiSolution2 = 0;	// KPIрешения для 2-го алгоритма
	private IAlgorithm algorithm1;	// 1-й тестируемый алгоритм
	private IAlgorithm algorithm2;	// 2-й тестируемый алгоритм
	private int[][][] coordinatesRobotsPerTime1;	// массив, хранящий координаты роботов в каждый момент времени после тестирования 1-го алгоритма
	private int[][][] coordinatesRobotsPerTime2;	// массив, хранящий координаты роботов в каждый момент времени после тестирования 2-го алгоритма
	private EventListener listener; 
	
	/** конструктор */
	public TestKit(int durTime, int countRobot, int sizeFieldX, int sizeFieldY)
	{
		durationTime = durTime;
		createRobots(countRobot);
		territory = new Territory(sizeFieldX, sizeFieldY);
		joinRobotsToTerritory();
		algorithm1 = new Algorithm();
		algorithm2 = new CrazyAlgorithm();

		coordinatesRobotsPerTime1 = new int[durTime][countRobot][2];
		coordinatesRobotsPerTime2 = new int[durTime][countRobot][2];
		//territory.setRobots(robots);
		//saveBeginPositionRobots();
		setAlgorithmForRobots(algorithm1);
	}
	
	/** создает указанное количество роботов */
	private void createRobots( int countRobot )
	{
		robots = new Robot[countRobot];
		for ( int i = 0; i < countRobot; i++ )
		{
			robots[i] = new Robot();
		}
	}
	
	/** задает роботам территорию */
	private void joinRobotsToTerritory()
	{
		for ( int i = 0; i < robots.length; i++ )
		{
			robots[i].setTerritory(territory);
		}
		
		generatePositionRobots();
	}
	
	/** генерирует начальные позиции роботов случайным образом */
	private void generatePositionRobots()
	{
		Random ran = new Random();
		//  выбираем для роботов случайные позиции
		for(Robot rob: robots)
		{
			rob.setPosition( ran.nextInt(territory.getSizeX()), ran.nextInt(territory.getSizeY())); 
		}
		
		// сравниваем позиции всех роботов между собой
		for(int i = 0; i < robots.length; i++)
		{
			for(int j = 0; j < robots.length; j++)
			{
				// не сравниваем с самим собой
				if (i == j)
				{
					continue;
				}
				
				// если позиции роботов совпали или робота поставили на препятствие, 
				// то выбрать новую позицию и снова сравнить ее с другими роботами
				if( (robots[i].getPosX() == robots[j].getPosX()) && (robots[i].getPosY() == robots[j].getPosY()) 
					|| (territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority() <= 0) )
				{
					robots[i].setPosition( ran.nextInt(territory.getSizeX()), ran.nextInt(territory.getSizeY()) );
					j = -1; // для сравнения новой позиции со всеми другими роботами
				}
			}
		}
		
		// для клеток, на которые поставили роботов, установить удовлетворенность = 1
		for(int i = 0; i < robots.length; i++)
		{
			//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
			territory.setSaturationMax(robots[i].getPosX(), robots[i].getPosY());
		}
	}
	
	/**  */
	public void changeAlgorithm1(IAlgorithm alg1)
	{
		this.algorithm1 = alg1;
	}
	
	public void changeAlgorithm2(IAlgorithm alg2)
	{
		this.algorithm2 = alg2;
	}
	
	public void changeAlgorithms(IAlgorithm alg1, IAlgorithm alg2)
	{
		this.algorithm1 = alg1;
		this.algorithm2 = alg2;
	}
	
	/** устанавливает всем роботам алгоритм */
	private void setAlgorithmForRobots(IAlgorithm alg)
	{
		for ( Robot rob: robots )
		{
			rob.setAlgorithm(alg);
		}
	}
	
	/** стаавит роботов в исходные позиции */
	private void setBeginPositionRobots()
	{
		for ( int i = 0; i< robots.length; i++)
		{
			robots[i].setPosition(coordinatesRobotsPerTime1[0][i][0], coordinatesRobotsPerTime1[0][i][1]);
			//robots[i].getTerritory().getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
			territory.setSaturationMax(robots[i].getPosX(), robots[i].getPosY());
		}
	}
	
	/** проводит тест алгоритмов */
	public void startTest()
	{	
		kpiSolution1 = computeKpiSolution(durationTime, algorithm1, coordinatesRobotsPerTime1);
		kpiSolution2 = computeKpiSolution(durationTime, algorithm2, coordinatesRobotsPerTime2);
		System.out.println("KPIрешения первого алгоритма: "+kpiSolution1
							+"\nKPIрешения второго алгоритма: "+kpiSolution2+"\n");
		System.out.println("Разность KPIрешений алгоритмов: "+(kpiSolution1 - kpiSolution2)
							+"\nПроцентное соотношение 1-го алгоритма ко 2-му: "+kpiSolution1/kpiSolution2*100);
		reviewSaturationMatrix();
	}
	
	/** возвращает KPIрешения для заданного алгоритма с заданной длительностью */
	private double computeKpiSolution(int duration, IAlgorithm alg, int[][][] posRobs)
	{
		setAlgorithmForRobots(alg);
		double kpiSolution = 0;
		double kpi = 0;
		System.out.println(territory.getCountBarrier());
		for( int time = 0; time < duration; time++ )
		{
			//System.out.println(time);
			for( int i = 0; i<robots.length; i++)
			{
				//System.out.println("Позиция робота: "+robots[i].getPosX()+" "+robots[i].getPosY()+" Приоритет клетки: "+territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority());
				if ( isHit() ) System.out.println("Роботы все таки встают на одну клетку :-(");
				//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
				//r.step();
				//сохраняем позиции роботов
				posRobs[time][i][0] = robots[i].getPosX();//posX
				posRobs[time][i][1] = robots[i].getPosY();//posY
			}
			
			
			territory.decrementSaturations(robots);
			territory.paint(robots);
			kpi = territory.computeKPIperTime();	//а может этот метод нужно описывать в testKit'е?
			kpiSolution += kpi;
			
			

			if ( time+1 != duration )
			{
				for( int i =0; i<robots.length; i++ )
				{
					robots[i].step();
				}
			}

			
			//System.out.println("KPIсреза в момент времени "+time+" равен: "+kpi);
			
			
		}
		
		kpiSolution /= duration;
		//System.out.println("KPIсреза в момент времени "+duration+" равен : "+kpi);
		//System.out.println("KPIрешения равен "+kpiSolution);
		//outputSaturationsMatrix();
		territory.resetTerritorySaturations();
		setBeginPositionRobots();
		
		return kpiSolution;
	}
	
	/** отображает KPIсреза в заданный момент времени, а также выводит матрицу удовлетворенности */
	private void computeKpiSolution( int duration, int[][][] posRobs)
	{
		double kpi = 0;
		System.out.println(territory.getCountBarrier());
		
		for( int time = 0; time <= duration; time++ )
		{
			//System.out.println(time);
			for( int i = 0; i<robots.length; i++)
			{
				//System.out.println("Позиция робота: "+robots[i].getPosX()+" "+robots[i].getPosY()+" Приоритет клетки: "+territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority());
				if ( isHit() ) System.out.println("Роботы все таки встают на одну клетку :-(");
				//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
			}
			
			
			territory.decrementSaturations(robots);
			if ( time == duration)
			{
				outputSaturationsMatrix();				
			}
			
			kpi = territory.computeKPIperTime();
			
			if ( time+1 < durationTime )
			{
				for( int i =0; i<robots.length; i++ )
				{
					robots[i].setPosition(posRobs[time+1][i][0], posRobs[time+1][i][1]);
					//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
					territory.setSaturationMax(robots[i].getPosX(), robots[i].getPosY());
				}
			}
			//System.out.println("KPIсреза в момент времени "+time+" равен: "+kpi);
		}
		
		System.out.println("KPIсреза в момент времени "+duration+" равен:"+kpi);
		
		//outputSaturationsMatrix();
		territory.resetTerritorySaturations();
		setBeginPositionRobots();
	}
	
	/** выводит матрицу удовлетворенности */
	private void outputSaturationsMatrix()
	{
		for ( int y =0; y < territory.getSizeY(); y++)
		{
			for ( int x = 0; x < territory.getSizeX(); x++)
			{
				System.out.printf("%.2f", territory.getTerritoryCell(x, y).getSaturation());
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/** обеспечивает диалог */
	private void reviewSaturationMatrix()
	{
		Scanner scanner = new Scanner(System.in);
		int time;
		while (true)
		{
			try
			{
				System.out.println("\nВведите момент времени в диапазоне 0-"+(durationTime-1));
				time = scanner.nextInt();
				if ( time < 0 || time >= durationTime )
				{
					System.out.println("Недопустимое значение");
					continue;
				}
			}  catch ( InputMismatchException e )
			{
				break;
			}
			
			computeKpiSolution(time, coordinatesRobotsPerTime1);
			computeKpiSolution(time, coordinatesRobotsPerTime2);
			for (int i = 0; i< robots.length; i++)
			{
				System.out.println("Робот "+i+" имеет координаты "+coordinatesRobotsPerTime1[time][i][0]+" "+coordinatesRobotsPerTime1[time][i][1]);
			}
		}
		scanner.close();
	}
	
	/** возвращает true, если тест уже был пройден или false в противном случае(не используется) */
	private boolean isAlready( int[][][] posRobs, int time)
	{
		if ( posRobs[time][0][0] == 0 && posRobs[time][0][1] == 0 && posRobs[time][1][0] == 0 && posRobs[time][1][1] == 0 )
		{
			return false;
		} else
		{
			return true;
		}//?		
	}
	
	/** возвращает true, если роботы все-таки встали на одну клетку или false в противном случае */
	private boolean isHit()
	{
		for ( int i = 0; i < robots.length-1; i++ )
		{
			for ( int j = i+1; j < robots.length; j++ )
			{
				if ( robots[i].getPosX() == robots[j].getPosX() && robots[i].getPosY() == robots[j].getPosY() )
				{
					return true;
				}				
			}
		}
		return false;
	}
	
	public Territory getTerritory()
	{
		return territory;
	}
	
	public Robot[] getRobots()
	{
		return robots;
	}
	
	public void step() throws InterruptedException
	{
		//notify();

		if ( time < durationTime )
		{
			for( int i = 0; i<robots.length; i++)
			{
				//System.out.println("Позиция робота: "+robots[i].getPosX()+" "+robots[i].getPosY()+" Приоритет клетки: "+territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority());
				if ( isHit() ) System.out.println("Роботы все таки встают на одну клетку :-(");
				//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
				//r.step();
				//сохраняем позиции роботов
				coordinatesRobotsPerTime1[time][i][0] = robots[i].getPosX();//posX
				coordinatesRobotsPerTime1[time][i][1] = robots[i].getPosY();//posY
			}
			
			
			//territory.decrementSaturations(robots);
			//territory.paint(robots);
		
			
		
			for( int i =0; i<robots.length; i++ )
			{
				robots[i].step();
			}
			
			time++;
			//territory.paint(robots);
			territory.decrementSaturations(robots);
			listener.onTimeChange();
			System.out.println("Время: "+time);
		}
			//wait();
	}
	
	synchronized public void play() 
	{
		
		thread = new Thread(new Runnable()
		{
			synchronized public void run()
			{

				
				while (time < durationTime)
				{
					
					try 
					{
						
						while ( isPause )
						{
							//System.out.println("111111");
							Thread.sleep(3000); //wait();
							//System.out.println("222222");
						}
						//System.out.println("asdasd");
						step();
						Thread.sleep(1000/multipleAcceleration);
					} catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		//thread.setDaemon(false);
		thread.setName("Моделирование");
		thread.start();

	}
	
	public Thread getThread()
	{
		return thread;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public int getDuration()
	{
		return durationTime;
	}
	
	public void addListener(EventListener listen)
	{
		listener = listen;
	}
	
	public void setMultipleAcceleration(int num)
	{
		multipleAcceleration = num;
	}
	
	public void changeSaturationMax(int num)
	{
		territory.changeSaturationMax(num);
	}
	
	public void changeTimeDuration(int num)
	{
		durationTime = num;
		coordinatesRobotsPerTime1 = new int[num][robots.length][2];
		coordinatesRobotsPerTime2 = new int[num][robots.length][2];
	}
	
	public int getSaturationMax()
	{
		return territory.getSaturationMax();
	}
}
