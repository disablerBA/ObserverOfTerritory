package ObserverOfTerritory;

import java.util.Random;

public class Territory {

	//private int[] [][] property ;
	private TerritoryCell[][] territory;	// ������ ������ ����������
	private int maxSaturation = 1;
	private double rateDecrement = 0.03;
	
	//private ArrayList<Robot> robots;
	
	public Territory(int x, int y)
	{
		territory = new TerritoryCell[x][y];
		generatePriority();
	}
	
	/** ���������� �������� ����������������� ������ � ����������� ������ 0 */
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
	
	/** ���������� ������ ���������� � ������������ �,� */
	final public TerritoryCell getTerritoryCell(int x,int y)
	{
		if ( (x < 0 || y <0) || (x>=getSizeX() || y>=getSizeY()) ) 
		{
			//System.out.println("������������� TerritoryCell �����������");
			return new TerritoryCell(-1); //
		}
		//System.out.println("������������� CellTerritory ������");
		//System.out.println("x= "+x+" y=" +y);
		return territory[x][y];
	}
	
	/** ���������� ������ ���� ������ */
	final public TerritoryCell[][] getTerritoryCell()
	{
		return territory;
	}
	
	/** ���������� ���������� � ��������� ������ */
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
		System.out.println("��� ����� ������");
		return -1;
	}
	
	/** ���������� ���������� � ��������� ������ */
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
		System.out.println("��� ����� ������");
		return -1;
	}
	
	/** ���������� ������� �������� KPI � ������ ������� */
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
	
	/** ���������� ������ ���������� �� � */
	final public int getSizeX()
	{
		return territory.length; 
	}
	
	/** ���������� ������ ���������� �� � */
	final public int getSizeY()
	{
		return territory[0].length;
	}
	
	/** ������ ��������� ��������� ��� ���� ������ ���������� */
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
					setSaturationMax(x,y);
				}
					
			}
		}
	}
	
	/** ��������� �������� ����������������� ���� ������ ����������, ����� ������ � ����������� <=0
	 *  � ���, �� ������� ����� ������ */
	final public void decrementSaturations(Robot [] robots)	//� ����� ���� ����� ����� ��������� � testKit'�?
	{// �������� ��� ������ � ������������
		for (int x=0; x < territory.length; x++)
		{
			for (int y = 0; y < territory[x].length; y++)
			{
				
				if ( !isHitPosition(x, y, robots) && territory[x][y].getPriority() > 0 ) 
				{
					if ( territory[x][y].getSaturation() >= rateDecrement * territory[x][y].getPriority() )
					{
						//System.out.println("saturation = saturation - rateDecrement*priority");
						territory[x][y].setSaturation( territory[x][y].getSaturation() - rateDecrement* territory[x][y].getPriority() );
					} else 
					{
						//System.out.println("saturation = 0");
						territory[x][y].setSaturation(0);
					}
					//territory[x][y].decrementSaturation();
					//paintCell(x,y);
				}
				
			}
		}
	}
	
	/** ���������� ���������� ������ � ����������� <=0 */
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
	
	/** ���������� true, ���� �� ������ � ������������ �,� ����� ����� ��� false � ��������� ������ */
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

	final public void setSaturationMax(int x, int y)	// 
	{
		territory[x][y].setSaturation(maxSaturation);
	}
	
	public void changeSaturationMax(int num)
	{
		maxSaturation = num;
		for (int x=0; x < territory.length; x++)
		{
			for (int y = 0; y < territory[x].length; y++)
			{
				if ( territory[x][y].getSaturation() > 0 )
				{
					territory[x][y].setSaturation(maxSaturation);
				}
			}
		}
	}
	
	public int getSaturationMax()
	{
		return maxSaturation;
	}
	
	public void changeRateDecrement(double rate)
	{
		rateDecrement = rate;
	}
}
