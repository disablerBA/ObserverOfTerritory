package ObserverOfTerritory;

import java.util.Random;

public class TestKit {
	
	private Robot[] robots;
	private Territory territory;
	private int[][] beginPosotionRobots;
	private double kpiSolution1 = 0;
	private double kpiSolution2 = 0;
	private IAlgorithm algorithm1;
	private IAlgorithm algorithm2;
	/*private Robot[] beginRobots;
	private Territory beginTerritory; */
	
	
	//private [] stateTerritory;
	
	public TestKit(int countRobot, int sizeFieldX, int sizeFieldY)
	{
		robots = new Robot[countRobot];
		for ( int i = 0; i < countRobot; i++ )
		{
			robots[i] = new Robot();
		}
		
		territory = new Territory(sizeFieldX, sizeFieldY);
		algorithm1 = new Algorithm();
		algorithm2 = new CrazyAlgorithm();
		
		territory.setRobots(robots);
		saveBeginPositionRobots();
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
	
	private void saveBeginPositionRobots()
	{
		beginPosotionRobots = new int[robots.length][2];
		for (int i = 0; i < robots.length; i++)
		{
			beginPosotionRobots[i][0] = robots[i].getPosX();
			beginPosotionRobots[i][1] = robots[i].getPosY();
		}
	}
	
	private void setBeginPositionRobots()
	{
		for ( int i = 0; i< robots.length; i++)
		{
			robots[i].setPosition(beginPosotionRobots[i][0], beginPosotionRobots[i][1]);
		}
	}
	
	private void resetSaturationsTerritory()
	{
		for (int x =0; x < territory.getSizeX(); x++)
		{
			for (int y =0; y < territory.getSizeY(); y++)
			{
				territory.getCellTerritory(x, y).resetSaturation();
			}
		}
	}
	
	public void startTest(int durationTime)
	{	
		kpiSolution1 = computeKpiSolution(algorithm1, durationTime);
		kpiSolution2 = computeKpiSolution(algorithm2, durationTime);
		System.out.println("\nKPI������� ������� ���������: "+kpiSolution1
							+"\nKPI������� ������� ���������: "+kpiSolution2);
	}
	
	private double computeKpiSolution(IAlgorithm alg, int durationTime)
	{
		setAlgorithmForRobots(alg);
		double kpiSolution = 0;
		System.out.println(territory.getCountBarrier());
		for( int time = 0; time <= durationTime; time++ )
		{
			System.out.println(time);
			for( Robot r : robots )
			{
				System.out.println("������� ������: "+r.getPosX()+" "+r.getPosY()+" ��������� ������: "+territory.getCellTerritory(r.getPosX(), r.getPosY()).getPriority());
				if ( isHit() ) System.out.println("������ ��� ���� ������ �� ���� ������");
				territory.getCellTerritory(r.getPosX(), r.getPosY()).saturationSetMax();
				//r.step();
			}
			
			territory.decrementSaturations();
			double kpi = territory.computeKPIperTime();	//� ����� ���� ����� ����� ��������� � testKit'�?
			kpiSolution += kpi;
			
			System.out.println("KPI����� � ������ ������� "+time+" ����� : "+kpi);
			
			for( Robot r : robots )
			{
				
				r.step();
			}
		}
		
		kpiSolution /= durationTime+1;
		System.out.println("KPI������� ����� "+kpiSolution);
		setBeginPositionRobots();
		resetSaturationsTerritory();
		
		return kpiSolution;
	}
	
	private boolean isHit()
	{
		
		for ( int i = 0; i < robots.length-1; i++ )
		{
			for ( int j = i+1; j < robots.length; j++ )
			{
				if ( robots[i].getPosX() == robots[j].getPosX() && robots[i].getPosY() == robots[j].getPosY() )
					return true;
			}
		}
		return false;
	}
}
