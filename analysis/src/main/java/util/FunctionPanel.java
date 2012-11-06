package util;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FunctionPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	float xMin=0, xMax=182, yMin=0.0f, yMax=1.0f;
	int larghezza=1200, altezza=600;
	float fattoreScalaX, fattoreScalaY;
	float[] fun;
	
	public void setArray(float[] fun) {
		this.fun = fun;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.white);
		fattoreScalaX=larghezza/((float)xMax-xMin);
		fattoreScalaY=altezza/((float)yMax-yMin);
		
		// cornice
		g.setColor(Color.black);
		//g.drawRect(0,0,larghezza-1,altezza-1);
		// assi cartesiani
		g.setColor(Color.red);
		g.drawLine(0,altezza, larghezza-1,altezza);
		g.drawLine(0,0,0,altezza-1);
		// scrittura valori estremi
		g.drawString(""+xMin, 5,altezza+15);
		g.drawString(""+xMax, larghezza,altezza+15);
		g.drawString(""+yMax, 5,15);
		//g.drawString(""+yMin, larghezza/2+5,altezza-5);
		
		// grafico della funzione
		g.setColor(Color.blue);
		setPixel(g,xMin,f(xMin));
		for (int ix=1; ix<larghezza; ix++){
			float x = xMin+((float)ix)/fattoreScalaX;
			setPixel(g,x,f(x));
		}
	}
	
	public float f(float x){
		return fun[(int)x];
	}
	
	void setPixel(Graphics g, float x, float y){
		if (x<xMin || x>xMax || y<yMin || y>yMax )
			return;
		int ix = Math.round((x-xMin)*fattoreScalaX);
		int iy = altezza-Math.round(
		(y-yMin)*fattoreScalaY);
		g.drawOval(ix,iy,1,4); // singolo punto
	}
}
