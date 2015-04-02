package ObserverOfTerritory;

import java.util.Random;

public class Territory {

	//private int[] [][] property ;
	private TerritoryCell[][] territory;
	//private ArrayList<Robot> robots;
	
	public Territory(int x, int y)
	{
		//robots = new ArrayList<Robot>();
		territory = new TerritoryCell[x][y];
		generatePriority();
	}
	
	/*final public ArrayList<Robot> getRobots()
	{
		return robots;
	}*/
	
	public void resetTerritorySaturations()
	{
		for (int x =0; x < getSizeX(); x++)
		{
			for (int y =0; y < getSizeY(); y++)
			{
				territory[x][y].resetSaturation();
			}
		}
	}
	
	final public TerritoryCell getTerritoryCell(int x,int y)
	{
		if ( (x < 0 || y <0) || (x>=getSizeX() || y>=getSizeY()) ) 
		{
			//System.out.println("Запрашиваемый TerritoryCell отсутствует");
			return new TerritoryCell(-2); // можно и -1
		}
		//System.out.println("Запрашиваемый CellTerritory найден");
		//System.out.println("x= "+x+" y=" +y);
		return territory[x][y];
	}
	
	final public TerritoryCell[][] getTerritoryCell()
	{
		return territory;
	}
	
	final public int getPosXTerritoryCell(TerritoryCell cellTerritory)
	{
		for ( int j = 0; j<getSizeY(); j++)
		{
			for ( int i = 0; i<getSizeX(); i++)
			{
				if ( territory[i][j] == cellTerritory)
				{
					return i;
				}
			}
		}
		System.out.println("Нет такой ячейки");
		return -1;
	}
	
	final public int getPosYTerritoryCell(TerritoryCell cellTerritory)
	{
		for ( int j = 0; j<getSizeY(); j++)
		{
			for ( int i = 0; i<getSizeX(); i++)
			{
				if ( territory[i][j] == cellTerritory)
				{
					return j;
				}
			}
		}
		System.out.println("Нет такой ячейки");
		return -1;
	}
	
	final public double computeKPIperTime()
	{
		double averageKPI = 0;
		for (TerritoryCell[] arrayTC: territory)
		{
			for (TerritoryCell tc: arrayTC)
			{
				averageKPI += tc.getSaturation();
			}
		}
		
		return averageKPI /= getSizeX()*getSizeY();
	}
	
	/*final public void setRobots(Robot ... robs)
	{
		if (robots.isEmpty()) 
		{
			for (Robot r:robs)
			{
				this.robots.add(r);
				r.setTerritory(this);
			}
				
		} else
		{
			for (Robot rob: robots)
			{
				for (Robot rob1 : robs)
				{
					if (rob == rob1) continue;
					this.robots.add(rob1);
					rob1.setTerritory(this);
				}
			}
		}
		
		
		
	}*/
	
	final public int getSizeX()
	{
		return territory.length; 
	}
	
	final public int getSizeY()
	{
		return territory[0].length;
	}
	
	private void generatePriority()
	{
		Random rand = new Random();
		for (int x = 0; x<territory.length; x++)
		{
			for( int y = 0; y<territory[x].length; y++ )
			{
				territory[x][y] = new TerritoryCell(rand.nextInt(5)-1);
				if (territory[x][y].getPriority() == 0 || territory[x][y].getPriority() == -1)
				{
					territory[x][y].setSaturationMax();
				}
					
			}
		}
	}
	
	final public void copyTerritory(Territory ter)
	{
		for ( int x=0; x<ter.getSizeX(); x++ )
		{
			for ( int y=0; y<ter.getSizeY(); y++ )
			{
				territory[x][y] = new TerritoryCell(ter.getTerritoryCell(x, y).getPriority());
			}
		}
	}
	
	final public void decrementSaturations(Robot [] robots)	//а может этот метод нужно описывать в testKit'е?
	{// возможно тут ошибка с координатами
		for (int x=0; x < territory.length; x++)
		{
			for (int y = 0; y < territory[x].length; y++)
			{
				
				if ( !isHitPosition(x, y, robots) ) 
				{
					territory[x][y].decrementSaturation();
				}
				
			}
		}
	}
	
	public int getCountBarrier()
	{
		int count = 0;
		for ( int i = 0; i<territory.length; i++)
		{
			for ( int j = 0; j<territory[i].length; j++)
			{
				if ( territory[i][j].getPriority() <=0 ) 
				{
					count++;
				}
			}
		}
		return count;
	}
	
	private boolean isHitPosition(int x, int y, Robot [] robots)
	{
		for (Robot robot: robots)
		{
			if ( (robot.getPosX() == x && robot.getPosY() == y) )
			{
				return true;
			}
		}
		return false;
	}
	
	
}
