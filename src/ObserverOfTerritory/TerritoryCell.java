package ObserverOfTerritory;

public class TerritoryCell {
	
	private int priority;	// приоритет клетки
	private double saturation = 0, rateDecrement = 0.03;	// удовлетворенность клетки и коэффициент
															// снижения удовлетворенности соответственно	
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
	
	/** уменьшает удовлетворенность клетки на rateDecrement*priority, если ее приоритет > 0 */
	final public void decrementSaturation()
	{
		//System.out.println("decrementSaturation()");
		if (priority > 0)
		{
			if (this.saturation >= rateDecrement*priority)
			{
				//System.out.println("saturation = saturation - rateDecrement*priority");
				this.saturation = this.saturation - rateDecrement*priority;
			} else 
			{
				//System.out.println("saturation = 0");
				this.saturation = 0;
			}
		}
	}
	
	/** сбрасывает удовлетворенность клетки, если ее приоритет > 0 */
	final public void resetSaturation()
	{
		if ( this.priority > 0)
		{
			this.saturation = 0;
		}
	}

	/** устанавливает удовлетворенность клетки территории в максимальное значение */
	final public void setSaturationMax()	// 
	{
		this.saturation = 1;
	}
}
