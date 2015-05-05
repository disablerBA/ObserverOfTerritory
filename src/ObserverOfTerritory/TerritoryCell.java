package ObserverOfTerritory;

public class TerritoryCell {
	
	private int priority;	// приоритет клетки
	private double saturation = 0;
	
	/** конструктор */
	public TerritoryCell(int priority)
	{
		this.priority = priority;
	}
	
	/** возвращает приоритет клетки */
	final public int getPriority()
	{
		return priority;
	}
	
	/** возвращает удовлетворенность клетки */
	final public double getSaturation()
	{
		return this.saturation;
	}
	
	/** сбрасывает удовлетворенность клетки, если ее приоритет > 0 */
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
