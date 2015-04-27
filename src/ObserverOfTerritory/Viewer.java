package ObserverOfTerritory;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Viewer extends Canvas {

	private GraphicsContext gc;
	
	public Viewer(int x, int y)
	{
		super(10*x, 10*y);
		gc = this.getGraphicsContext2D();
	}
	
	public void paint(Robot[] robs)
	{
		Territory ter = robs[0].getTerritory();
		double maxSaturation = ter.getSaturationMax();
		int r, g, b;
		double sat; 
		for(int x = 0; x<ter.getSizeX();x++)
		{
			for (int y = 0; y< ter.getSizeY(); y++)
			{
				sat = ter.getTerritoryCell(x, y).getSaturation();
				if ( ter.getTerritoryCell(x, y).getPriority() > 0 )
				{
					b = r = (int)(0xFF-((sat/maxSaturation) * 0xFF) );
					g = (int)(0xFF-((sat/maxSaturation) * (0xFF - 0xA0)) );
					gc.setFill(Color.rgb(r,g,b));
				} else	
				{
					if ( ter.getTerritoryCell(x, y).getPriority() == 0 )
					{
						//цвет клеток с приоритетом 0
						gc.setFill(Color.rgb(0xFF, 0xFF, 0x80));
					} else
					{
						//цвет препятствия
						gc.setFill(Color.rgb(0x00,0x00,0x00));
					}
				}
				
				gc.fillRect(10*x, 10*y, 10, 10);
			}
		}
		
		for ( int i = 0; i < robs.length; i++)
		{
			gc.setFill(Color.rgb(0xFF, 0x00, 0x00));
			gc.fillRect(10*robs[i].getPosX()+2, 10*robs[i].getPosY()+2, 6, 6);
		}
	}
}
