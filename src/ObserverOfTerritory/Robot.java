package ObserverOfTerritory;

public class Robot	{

	private int posX, posY; // координаты робота
	private Territory territory; // территория, на которую "поставили" робота
	private IAlgorithm algorithm; // алгоритм, используемый роботом для выбора следующей позиции
	
	public enum MoveDirection {
		Up, Up_Right, Right, Right_Down, Down, Down_Left, Left, Left_Up
	};
	
	/** робот шагает на следующую клетку, выбранную в соответствии с алгоритмом */
	final public void step()
	{
		algorithm.selectNextPosition(this);
		//territory.getTerritoryCell(getPosX(), getPosY()).setSaturationMax();
		territory.setSaturationMax(getPosX(), getPosY());
	}
	
	/** назначает роботу алгоритм */
	final public void setAlgorithm(IAlgorithm alg)
	{
		algorithm = alg;
	}
	
	/** возвращает территорию, на которой находится робот */
	final public Territory getTerritory()
	{
		return territory;
	}
	
	/** назначает территорию для робота */
	final public void setTerritory(Territory ter)
	{
		this.territory = ter;
		//ter.setRobots(this);
		//generatePositionRobots();
	}
	
	/** робот осматривает клетки вокруг себя(8 штук) */
	public TerritoryCell[] lookAround()
	{
		TerritoryCell[] ct = new TerritoryCell[8];
		int i = 0;
		for ( int x=-1; x<=1; x++ )
		{
			for ( int y =-1; y<=1; y++ )
			{
				if ( (x == 0) && (y == 0) ) 
				{
					continue;
				}
				//System.out.println("х="+x+" y="+y);
				ct[i++] = territory.getTerritoryCell(posX+x, posY+y);
			}
		}
		
		return ct;
	}
	
	/** возвращает координату робота по Х */
	final public int getPosX()
	{
		return posX;
	}
	
	/** возвращает координату робота по У */
	final public int getPosY()
	{
		return posY;
	}
	
	/** устанавливает координаты */
	final public void setPosition(int x, int y)
	{
		posX = x;
		posY = y;
	}
	
	/** двигает робота по одному из 8 направлений на 1 клетку */
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
