/*
 * Name: Xinyuan Zhang
 * Class: CS480
 * 
 * Assignment 1
 * Due: 2019/09/24
 * Problem Number: 1, 2
 * Description: 
 * 	Code given for ColorType objects.
 */

public class ColorType
{
	public float r, g, b;

	public ColorType( float _r, float _g, float _b)
	{
		r = _r;
		g = _g;
		b = _b;
	}
	
	public int getBRGUint8()
	{
		int _b = Math.round(b*255.0f); 
		int _g = Math.round(g*255.0f); 
		int _r = Math.round(r*255.0f);
		
		return (_r<<16) | (_g<<8) | _b;
	}

}
