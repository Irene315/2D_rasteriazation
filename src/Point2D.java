/*
 * Name: Xinyuan Zhang
 * Class: CS480
 * 
 * Assignment 1
 * Due: 2019/09/24
 * Problem Number: 1, 2
 * Description: 
 * 	Code given for Point2D object. 
 *  Two helper methods added to sort points through y-axis, 
 *  adapted from https://howtodoinjava.com/sort/collections-sort/
 */

public class Point2D implements Comparable<Point2D>
{
	public int x, y;
	public float u, v; // uv coordinates for texture mapping
	public ColorType c;
	public Point2D(int _x, int _y, ColorType _c)
	{
		u = 0;
		v = 0;
		x = _x;
		y = _y;
		c = _c;
	}
	public Point2D(int _x, int _y, ColorType _c, float _u, float _v)
	{
		u = _u;
		v = _v;
		x = _x;
		y = _y;
		c = _c;
	}
	public Point2D()
	{
		c = new ColorType(1.0f, 1.0f, 1.0f);
	}
	public Point2D( Point2D p)
	{
		u = p.u;
		v = p.v;
		x = p.x;
		y = p.y;
		c = new ColorType(p.c.r, p.c.g, p.c.b);
	}
	
	// helper method added to sort vertices
	public Integer getY() {
		return this.y;
	}
	
	public int compareTo(Point2D o) {
		return this.getY().compareTo(o.getY());
	}
	
}