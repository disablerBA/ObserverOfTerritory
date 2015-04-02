package ObserverOfTerritory;

public class TerritoryCell {
	
	private int priority;
	private double saturation = 0, rateDecrement = 0.03;
	
	public TerritoryCell(int priority)
	{
		this.priority = priority;
	}
	
	final public int getPriority()
	{
		return priority;
	}
	
	final public double getSaturation()
	{
		return this.saturation;
	}
	
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
	
	final public void resetSaturation()
	{
		if ( this.priority > 0)
		{
			this.saturation = 0;
		}
			
	}

	/** устанавливает удовлетворенность квадрата территории в максимальное значение */
	final public void setSaturationMax()	// 
	{
		this.saturation = 1;
	}
}
