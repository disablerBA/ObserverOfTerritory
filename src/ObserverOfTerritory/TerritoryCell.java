package ObserverOfTerritory;

public class TerritoryCell {
	
	private int priority;	// ��������� ������
	private double saturation = 0;
	
	/** ����������� */
	public TerritoryCell(int priority)
	{
		this.priority = priority;
	}
	
	/** ���������� ��������� ������ */
	final public int getPriority()
	{
		return priority;
	}
	
	/** ���������� ����������������� ������ */
	final public double getSaturation()
	{
		return this.saturation;
	}
	
	/** ���������� ����������������� ������, ���� �� ��������� > 0 */
	final public void resetSaturation()
	{
		if ( this.priority > 0)
		{
			this.saturation = 0;
		}
	}
	
	public void setSaturation(double sat)
	{
		this.saturation = sat;
	}
}
