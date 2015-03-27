package ObserverOfTerritory;

import java.util.ArrayList;
import java.util.Random;

public class Robot	{

	private int posX, posY;
	private Territory territory;
	private IAlgorithm algorithm;
	
	public enum MoveDirection {
		Up, Up_Right, Right, Right_Down, Down, Down_Left, Left, Left_Up
	};
	
	public Robot()
	{
		
	}
	
	final public void step()
	{
		algorithm.algorithm(this);
	}
	
	final public void setAlgorithm(IAlgorithm alg)
	{
		algorithm = alg;
	}
	
	final public Territory getTerritory()
	{
		return territory;
	}
	
	private void generatePositionRobots()
	{
		Random ran = new Random();
		ArrayList<Robot> robots = territory.getRobots();
		for(Robot rob: robots)
		{
			rob.setPosition( ran.nextInt(territory.getSizeX()), ran.nextInt(territory.getSizeY())); 
		}
		
		for(int i = 0; i < robots.size(); i++)
		{
			for(int j = 0; j < robots.size(); j++)
			{
				if (i == j) continue;
				if( (robots.get(i).getPosX() == robots.get(j).getPosX()) && (robots.get(i).getPosY() == robots.get(j).getPosY()) 
					|| (territory.getCellTerritory(robots.get(i).getPosX(), robots.get(i).getPosY()).getPriority() <= 0) )
				{
					robots.get(i).setPosition( ran.nextInt(territory.getSizeX()), ran.nextInt(territory.getSizeY()) );
					j = 0;
				}
			}
		}
	}
	
	final public void setTerritory(Territory ter)
	{
		this.territory = ter;
		//ter.setRobots(this);
		generatePositionRobots();
	}
	
	public CellTerritory[] lookAround()
	{
		CellTerritory[] ct = new CellTerritory[8];
		int i = 0;
		for ( int x=-1; x<=1; x++ )
		{
			for ( int y =-1; y<=1; y++ )
			{
				if ( (x == 0) && (y == 0) ) 
				{
					continue;
				}
				//System.out.println("õ="+x+" y="+y);
				ct[i++] = territory.getCellTerritory(posX+x, posY+y);
			}
		}
		
		return ct;
	}
	
	final public int getPosX()
	{
		return posX;
	}
	
	final public int getPosY()
	{
		return posY;
	}
	
	final public void setPosition(int x, int y)
	{
		posX = x;
		posY = y;
	}
	
	final public void move(MoveDirection direct)
	{
		switch (direct)
		{
			case Up: posY-=1;
			break;
			
			case Up_Right: posX+=1; posY-=1;
			break;
			
			case Right: posX+=1;
			break;
			
			case Right_Down: posX+=1; posY+=1;
			break;
			
			case Down: posY+=1;
			break;
			
			case Down_Left: posX-=1; posY+=1;
			break;
			
			case Left: posX-=1;
			break;
			
			case Left_Up: posX-=1; posY-=1;
			break;
		}
	}
}
