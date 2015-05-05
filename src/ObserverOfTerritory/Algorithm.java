package ObserverOfTerritory;

import java.util.ArrayList;
import java.util.Random;

public class Algorithm implements IAlgorithm {

	/** �������� ��� ������ ��������� ������� ��������� �������:
	 * �� ����� ������ ���������� ������ ���������� ������ � ���������� ������������������,
	 * ����� �� �� ����� ���������� ������ � ���������� �����������.
	 * ���� ����� ���������, �� �� ����� ������ ���������� �������� */
	@Override
	public void selectNextPosition(Robot rob)
	{
		TerritoryCell[] ct = rob.lookAround();
		int maxPriority;
		double minSaturation;
		Random ran = new Random();
		ArrayList<TerritoryCell> satisfyingCell = new ArrayList<TerritoryCell>();
		minSaturation = rob.getTerritory().getSaturationMax();
		for (int i = 0; i<ct.length; i++)
		{
			if ( ct[i].getPriority() > -1 && minSaturation > ct[i].getSaturation() )
			{	
				minSaturation = ct[i].getSaturation();
			} 
		}
		
		for (int i = 0; i<ct.length; i++)
		{
			if ( ct[i].getSaturation() == minSaturation )
			{	
				satisfyingCell.add( ct[i] );
			} 
		}
		
		maxPriority = satisfyingCell.get(0).getPriority();
		for ( int i = 1; i<satisfyingCell.size(); i++ )
		{
			if ( maxPriority < satisfyingCell.get(i).getPriority() )
			{
				maxPriority = satisfyingCell.get(i).getPriority();
			}
		}
		
		for ( int i = 0; i < satisfyingCell.size(); i++ )
		{
			if ( satisfyingCell.get(i).getPriority() != maxPriority && satisfyingCell.get(i).getPriority() != 0)
			{
				satisfyingCell.remove(i--);
			}
		}
		
		for ( int i = 0; i < satisfyingCell.size(); i++ )
		{
			if ( 	satisfyingCell.get(i).getPriority() != 0 
				&&	satisfyingCell.get(i).getSaturation() == rob.getTerritory().getSaturationMax() )
			{
				satisfyingCell.remove(i--);
			}
		}
		
		if ( satisfyingCell.isEmpty() )
		{
			return;
		}
		
		TerritoryCell randomCell = satisfyingCell.get( ran.nextInt(satisfyingCell.size()) );
		rob.setPosition( rob.getTerritory().getPosXTerritoryCell( randomCell ), rob.getTerritory().getPosYTerritoryCell( randomCell )  );
	}
}
