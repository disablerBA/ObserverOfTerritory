package ObserverOfTerritory;

import java.util.ArrayList;
import java.util.Random;

import ObserverOfTerritory.Robot.MoveDirection;

public class CrazyAlgorithm implements IAlgorithm {

	@Override
	public void algorithm(Robot robot)
	{
		Random ran = new Random();
		CellTerritory ct[] = robot.lookAround();
		ArrayList<CellTerritory> satisfyingCell = new ArrayList<CellTerritory>();
		for (int i = 0; i<ct.length; i++)
		{
			if ( ct[i].getPriority() >= 0 && ct[i].getSaturation() != 1 )
			{	
				satisfyingCell.add(ct[i]);
			} 
		}
		
		CellTerritory randomCell =  satisfyingCell.get( ran.nextInt(satisfyingCell.size()) );
		robot.setPosition( robot.getTerritory().getPosXCellTerritory(randomCell) , robot.getTerritory().getPosYCellTerritory(randomCell));
	}
}
