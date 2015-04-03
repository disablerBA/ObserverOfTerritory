package ObserverOfTerritory;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class TestKit {
	
	private int durationTime;
	private Robot[] robots;
	private Territory territory;
	//private int[][] beginPositionRobots;
	private double kpiSolution1 = 0;
	private double kpiSolution2 = 0;
	private IAlgorithm algorithm1;
	private IAlgorithm algorithm2;
	private int[][][] coordinatesRobotsPerTime1;
	private int[][][] coordinatesRobotsPerTime2;
	/*private Robot[] beginRobots;
	private Territory beginTerritory; */
	
	
	//private [] stateTerritory;
	
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
	}
	
	private void createRobots( int countRobot )
	{
		robots = new Robot[countRobot];
		for ( int i = 0; i < countRobot; i++ )
		{
			robots[i] = new Robot();
		}
	}
	
	private void joinRobotsToTerritory()
	{
		for ( int i = 0; i < robots.length; i++ )
		{
			robots[i].setTerritory(territory);
		}
		
		generatePositionRobots();
	}
	
	private void generatePositionRobots()
	{
		Random ran = new Random();
		//ArrayList<Robot> robots = territory.getRobots();
		for(Robot rob: robots)
		{
			rob.setPosition( ran.nextInt(territory.getSizeX()), ran.nextInt(territory.getSizeY())); 
		}
		
		for(int i = 0; i < robots.length; i++)
		{
			for(int j = 0; j < robots.length; j++)
			{
				if (i == j)
				{
					continue;
				}
				
				if( (robots[i].getPosX() == robots[j].getPosX()) && (robots[i].getPosY() == robots[j].getPosY()) 
					|| (territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority() <= 0) )
				{
					robots[i].setPosition( ran.nextInt(territory.getSizeX()), ran.nextInt(territory.getSizeY()) );
					j = -1;
				}
			}
		}
		
		for(int i = 0; i < robots.length; i++)
		{
			territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
		}
	}
	
	public void setAlgorithm1(IAlgorithm alg1)
	{
		this.algorithm1 = alg1;
	}
	
	public void setAlgorithm2(IAlgorithm alg2)
	{
		this.algorithm2 = alg2;
	}
	
	public void setAlgorithms(IAlgorithm alg1, IAlgorithm alg2)
	{
		this.algorithm1 = alg1;
		this.algorithm2 = alg2;
	}
	
	private void setAlgorithmForRobots(IAlgorithm alg)
	{
		for ( Robot rob: robots )
		{
			rob.setAlgorithm(alg);
		}
	}
	
	/*private void saveBeginPositionRobots()
	{
		beginPositionRobots = new int[robots.length][2];
		for (int i = 0; i < robots.length; i++)
		{
			beginPositionRobots[i][0] = robots[i].getPosX();
			beginPositionRobots[i][1] = robots[i].getPosY();
		}
	}*/
	
	private void setBeginPositionRobots()
	{
		for ( int i = 0; i< robots.length; i++)
		{
			robots[i].setPosition(coordinatesRobotsPerTime1[0][i][0], coordinatesRobotsPerTime1[0][i][1]);
			robots[i].getTerritory().getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
		}
	}
	
	public void startTest()
	{	
		kpiSolution1 = computeKpiSolution(durationTime, algorithm1, coordinatesRobotsPerTime1);
		kpiSolution2 = computeKpiSolution(durationTime, algorithm2, coordinatesRobotsPerTime2);
		System.out.println("KPIрешения первого алгоритма: "+kpiSolution1
							+"\nKPIрешения второго алгоритма: "+kpiSolution2+"\n");
		reviewSaturationMatrix();
	}
	
	private double computeKpiSolution(int duration, IAlgorithm alg, int[][][] posRobs)
	{
		setAlgorithmForRobots(alg);
		double kpiSolution = 0;
		double kpi = 0;
		System.out.println(territory.getCountBarrier());
		for( int time = 0; time < duration; time++ )
		{
			System.out.println(time);
			for( int i = 0; i<robots.length; i++)
			{
				System.out.println("Позиция робота: "+robots[i].getPosX()+" "+robots[i].getPosY()+" Приоритет клетки: "+territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority());
				if ( isHit() ) System.out.println("Роботы все таки встают на одну клетку :-(");
				//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
				//r.step();
				//сохраняем позиции роботов
				posRobs[time][i][0] = robots[i].getPosX();//posX
				posRobs[time][i][1] = robots[i].getPosY();//posY
			}
			
			
			territory.decrementSaturations(robots);
			kpi = territory.computeKPIperTime();	//а может этот метод нужно описывать в testKit'е?
			kpiSolution += kpi;
			
			
			
			if ( time+1 != duration )
			{
				for( int i =0; i<robots.length; i++ )
				{
					robots[i].step();
				}
			}
			System.out.println("KPIсреза в момент времени "+time+" равен: "+kpi);
		}
		
		kpiSolution /= duration;
		//System.out.println("KPIсреза в момент времени "+duration+" равен : "+kpi);
		//System.out.println("KPIрешения равен "+kpiSolution);
		//outputSaturationsMatrix();
		territory.resetTerritorySaturations();
		setBeginPositionRobots();
		
		return kpiSolution;
	}
	
	private void computeKpiSolution( int duration, int[][][] posRobs)
	{
		double kpi = 0;
		System.out.println(territory.getCountBarrier());
		
		for( int time = 0; time <= duration; time++ )
		{
			System.out.println(time);
			for( int i = 0; i<robots.length; i++)
			{
				System.out.println("Позиция робота: "+robots[i].getPosX()+" "+robots[i].getPosY()+" Приоритет клетки: "+territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority());
				if ( isHit() ) System.out.println("Роботы все таки встают на одну клетку :-(");
				//territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
			}
			
			
			territory.decrementSaturations(robots);
			outputSaturationsMatrix();
			kpi = territory.computeKPIperTime();
			
			if ( time+1 < durationTime )
			{
				for( int i =0; i<robots.length; i++ )
				{
					robots[i].setPosition(posRobs[time+1][i][0], posRobs[time+1][i][1]);
					territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).setSaturationMax();
				}
			}
			System.out.println("KPIсреза в момент времени "+time+" равен: "+kpi);
		}
		
		//System.out.println("KPIсреза в момент времени "+duration+" равен:"+kpi);
		
		//outputSaturationsMatrix();
		territory.resetTerritorySaturations();
		setBeginPositionRobots();
	}
	
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
	
	public void reviewSaturationMatrix()
	{
		Scanner scanner = new Scanner(System.in);
		int time;
		while (true)
		{
			try
			{
				time = scanner.nextInt();
				
			}  catch ( InputMismatchException e )
			{
				break;
			}
			
			computeKpiSolution(time, coordinatesRobotsPerTime1);
			//computeKpiSolution(time, algorithm2, coordinatesRobotsPerTime2);
			for (int i = 0; i< robots.length; i++)
			{
				System.out.println("Робот "+i+" имеет координаты "+coordinatesRobotsPerTime1[time][i][0]+" "+coordinatesRobotsPerTime1[time][i][1]);
			}
		}
		scanner.close();
	}
	
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
}
