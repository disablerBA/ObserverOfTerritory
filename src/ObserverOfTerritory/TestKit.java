package ObserverOfTerritory;

import java.util.Random;

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
					j = 0;
				}
			}
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
		}
	}
	
	public void startTest()
	{	
		kpiSolution1 = computeKpiSolution(algorithm1, coordinatesRobotsPerTime1);
		kpiSolution2 = computeKpiSolution(algorithm2, coordinatesRobotsPerTime2);
		System.out.println("\nKPIрешения первого алгоритма: "+kpiSolution1
							+"\nKPIрешения второго алгоритма: "+kpiSolution2);
	}
	
	private double computeKpiSolution(IAlgorithm alg, int[][][] posRobs)
	{
		setAlgorithmForRobots(alg);
		double kpiSolution = 0;
		System.out.println(territory.getCountBarrier());
		for( int time = 0; time < durationTime; time++ )
		{
			System.out.println(time);
			for( int i = 0; i<robots.length; i++)
			{
				System.out.println("Позиция робота: "+robots[i].getPosX()+" "+robots[i].getPosY()+" Приоритет клетки: "+territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).getPriority());
				if ( isHit() ) System.out.println("Роботы все таки встают на одну клетку :-(");
				territory.getTerritoryCell(robots[i].getPosX(), robots[i].getPosY()).saturationSetMax();
				//r.step();
				//сохраняем позиции роботов
				posRobs[time][i][0] = robots[i].getPosX();//posX
				posRobs[time][i][1] = robots[i].getPosY();//posY
			}
			
			
			territory.decrementSaturations(robots);
			double kpi = territory.computeKPIperTime();	//а может этот метод нужно описывать в testKit'е?
			kpiSolution += kpi;
			
			System.out.println("KPIсреза в момент времени "+time+" равен : "+kpi);
			
			for( Robot r : robots )
			{
				r.step();
			}
		}
		
		kpiSolution /= durationTime;
		System.out.println("KPIрешения равен "+kpiSolution);
		setBeginPositionRobots();
		territory.resetTerritorySaturations();
		
		return kpiSolution;
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
