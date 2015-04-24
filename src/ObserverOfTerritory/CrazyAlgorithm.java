package ObserverOfTerritory;

import java.util.ArrayList;
import java.util.Random;

public class CrazyAlgorithm implements IAlgorithm {
	
	/** выбирает для робота следующую позицию случайным образом */
	@Override
	public void selectNextPosition(Robot robot)
	{
		Random ran = new Random();
		TerritoryCell ct[] = robot.lookAround();
		ArrayList<TerritoryCell> satisfyingCell = new ArrayList<TerritoryCell>();
		for (int i = 0; i<ct.length; i++)
		{
			if ( ct[i].getPriority() >= 0 )
			{	
				satisfyingCell.add(ct[i]);
			} 
		}
		
		TerritoryCell randomCell =  satisfyingCell.get( ran.nextInt(satisfyingCell.size()) );
		robot.setPosition( robot.getTerritory().getPosXTerritoryCell(randomCell) , robot.getTerritory().getPosYTerritoryCell(randomCell));
	}
}
