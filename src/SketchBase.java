//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

/*
 * 
 * Name: Xinyuan Zhang
 * Class: CS480
 * 
 * Assignment 1
 * Due: 2019/09/24
 * Problem Number: 1, 2
 * 
 * Description: 
 *  1. Implemented function drawLine() for line drawings through Bresenham algorithm 
 *  adapted from textbook "Computer Graphics with OpenGL" p137.
 *  When slope is between -1 to 1, it decides a y value for each x value. 
 *  When slope is greater than 1 or less than -1, it decides a x value for each y value.
 *  After measuring which 
 *  By inputing two points on the canvas, it draws a line in between the two points.  
 *  dx, dy, r_change, g_change, b_change: total amount of the position or color change
 *  dr, dg, db: color change for each pixel
 *  x, y: current position of point to draw
 *  p: the difference between distance lower/left and distance upper/right, decides which pixel to choose by comparing it to zero
 *  twoDy, twoDx, twoDyMinusDx, twoDxMinusDy: choose the pixel by adding the distance
 *  
 *  2. Implemented function drawTriangle() for triangle drawings through Bresenham algorithm
 *  introduced in http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html.
 *  It draws the triangle from bottom to top. 
 *  First draw the bottom half which is a flat-top if exists. Then stop if it is just a flat-top triangle.
 *  After that, draw the top half which is a flat-bottom if exists.
 *  By inputing three points on the canvas, it draws a triangle with the three points as vertices.
 *  v1, v2, v3: store sorted vertices
 *  startP, endP: start point and end point for drawing each line of the triangle
 *  dr_start, dg_start, db_start, dr_end, dg_end, db_end: color change for each point
 *  x_start, x_end: track each point's x value
 *  y: counter for tracking current position on y-axis
 *  
 */

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	// draw a line segment
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2)
	{
		
		// initialize the variables for line endpoints and slope
		int dx = Math.abs(p2.x-p1.x);
		int dy = Math.abs(p2.y-p1.y);
		
		// initialize the variables for line color transition
		float r_change = p2.c.r-p1.c.r;
		float g_change = p2.c.g-p1.c.g;
		float b_change = p2.c.b-p1.c.b;
		
		// counters for drawing position
		int x,y;
		
		// draw the first point
		drawPoint(buff, p1);
		x = p1.x;
		y = p1.y;
		
		// special cases
		if ((p1.x == p2.x) && (p1.y == p2.y)) {
			return;
		}
		
		else if (p1.x == p2.x) {
			float dr = r_change/dy;
			float dg = g_change/dy;
			float db = b_change/dy;
			ColorType c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
			while (dy > 0) {
				if (p1.y < p2.y) {
					y++;
				}
				else {
					y--;
				}
				c.r += dr;
				c.g += dg;
				c.b += db;
				drawPoint(buff, new Point2D(x, y, c));
				dy--;
			}
			return;
		}
		
		else if (p1.y == p2.y) {
			float dr = r_change/dx;
			float dg = g_change/dx;
			float db = b_change/dx;
			ColorType c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
			while (dx > 0) {
				if (p1.x < p2.x) {
					x++;
				}
				else {
					x--;
				}
				c.r += dr;
				c.g += dg;
				c.b += db;
				drawPoint(buff, new Point2D(x, y, c));
				dx--;
			}
			return;
		}
			
		// when 0<=|slope|<=1
		if (dy/dx < 1) {
			
			// initialize the decision parameters for choosing  y_k or y_k+1
			int p = 2 * dy - dx;
			int twoDy = 2 * dy;
			int twoDyMinusDx = 2 * (dy - dx);
			
			// initialize variables for color change value for each pixel
			float dr = r_change/dx;
			float dg = g_change/dx;
			float db = b_change/dx;
			ColorType c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
			
			while (dx > 0) {
				
				// calculate color of the point
				c.r += dr;
				c.g += dg;
				c.b += db;
				
				// when p1p2 goes right
				if (p1.x < p2.x) {
					x++;
				}
				// when p1p2 goes left
				else {
					x--;
				}
				if (p < 0) {
					p += twoDy;
				}
				else {
					// when p1p2 goes up
					if (p1.y < p2.y) {
						y++;
					}
					// when p1p2 goes down
					else {
						y--;
					}
					p += twoDyMinusDx;
				}
				drawPoint(buff, new Point2D(x, y, c));
				dx--;
			}
			
		}
		

		// when |slope|>=1
		else {
			
			// initialize the decision parameters for choosing  x_k or x_k+1
			int p = 2 * dx - dy;
			int twoDx = 2 * dx;
			int twoDxMinusDy = 2 * (dx - dy);
			
			// initialize variables for color change value for each pixel
			float dr = r_change/dy;
			float dg = g_change/dy;
			float db = b_change/dy;
			ColorType c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
			
			while (dy > 0) {
				
				// calculate color of the point
				c.r += dr;
				c.g += dg;
				c.b += db;
				
				// when p1p2 goes up
				if (p1.y < p2.y) {
					y++;
				}
				// when p1p2 goes downs
				else {
					y--;
				}
				if (p < 0) {
					p += twoDx;
				}
				else {
					// when p1p2 goes right
					if (p1.x < p2.x) {
						x++;
					}
					// when p1p2 goes left
					else {
						x--;
					}
					p += twoDxMinusDy;
				}
				drawPoint(buff, new Point2D(x, y, c));
				dy--;
			} 
			
		} 

	}
	
	// draw a triangle
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
	{
		
		// special case where some vertices are the same point
		if ((p1.x == p2.x) && (p1.y == p2.y)) {
			drawLine(buff,p1,p3);
			return;
		}
		if ((p1.x == p3.x) && (p1.y == p3.y)) {
			drawLine(buff,p1,p2);
			return;
		}
		if ((p2.x == p3.x) && (p2.y == p3.y)) {
			drawLine(buff,p1,p2);
			return;
		}
		
		// sort vertices where v1 at bottom, v2 in middle, and v3 on top
		List<Point2D> vertices = Arrays.asList(p1,p2,p3);
		Collections.sort(vertices);
		Point2D v1 = vertices.get(0);
		Point2D v2 = vertices.get(1);
		Point2D v3 = vertices.get(2); 
	    
		// initialize the drawing start point and end point to v1, the lowest point on y axis
		Point2D startP = new Point2D(v1);
		Point2D endP = new Point2D(v1);
		
		// use first vertex color for flat shading
		if (!do_smooth) {
			startP.c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
			endP.c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
		}
		
		// initialize variables for storing color change value
		float dr_start = ((float)(v2.c.r - v1.c.r))/((float)(v2.y - v1.y));
		float dg_start = ((float)(v2.c.g - v1.c.g))/((float)(v2.y - v1.y));
		float db_start = ((float)(v2.c.b - v1.c.b))/((float)(v2.y - v1.y));
		float dr_end = ((float)(v3.c.r - v1.c.r))/((float)(v3.y - v1.y));
		float dg_end = ((float)(v3.c.g - v1.c.g))/((float)(v3.y - v1.y));
		float db_end = ((float)(v3.c.b - v1.c.b))/((float)(v3.y - v1.y));
		
		// initialize the start point of drawing to the lowest point
		// and it increments through the inverse of slope v1v2
		float x_start = v1.x;
		float dx_start = ((float)(v2.x - v1.x))/((float)(v2.y - v1.y));
		
		// initialize the end point of drawing to the lowest point
		// and it increments through the inverse of slope v1v3
		float x_end = v1.x;
		float dx_end = ((float)(v3.x - v1.x))/((float)(v3.y - v1.y));
		
		// initialize the position counter
		int y = v1.y;
		
		// when triangle has a flat-top bottom half
		if (v1.y != v2.y) {
			
			while (y < v2.y) {
				drawLine(buff, startP, endP);
				
				if (do_smooth) {
					startP.c.r += dr_start;
					startP.c.g += dg_start;
					startP.c.b += db_start;
					endP.c.r += dr_end;
					endP.c.g += dg_end;
					endP.c.b += db_end;
				}
				
				x_start += dx_start;
				x_end += dx_end;
				startP.x = Math.round(x_start);
				endP.x = Math.round(x_end);
				startP.y++;
				endP.y++;
				y++;				
			}
		}
		
		// when triangle only has a flat-top bottom half
		if (v2.y == v3.y) {
			return;
		}
		
		// transit the start to middle point
		startP = new Point2D(v2);
		x_start = v2.x;
		dx_start = ((float)(v3.x - v2.x))/((float)(v3.y - v2.y));
		
		if (!do_smooth) {
			startP.c = new ColorType(p1.c.r, p1.c.g, p1.c.b);
		}
		
		else {
			dr_start = ((float)(v3.c.r - v2.c.r))/((float)(v3.y - v2.y));
			dg_start = ((float)(v3.c.g - v2.c.g))/((float)(v3.y - v2.y));
			db_start = ((float)(v3.c.b - v2.c.b))/((float)(v3.y - v2.y));
		}
		
		// when triangle has a flat-bottom top half
		while (y <= v3.y) {
			drawLine(buff, startP, endP);
			
			if (do_smooth) {
				startP.c.r += dr_start;
				startP.c.g += dg_start;
				startP.c.b += db_start;
				endP.c.r += dr_end;
				endP.c.g += dg_end;
				endP.c.b += db_end;
			}
			
			x_start += dx_start;
			x_end += dx_end;
			startP.x = Math.round(x_start);
			endP.x = Math.round(x_end);
			startP.y++;
			endP.y++;
			y++;			
		}
		
	}
	
	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// replace the following line with your implementation
		drawPoint(buff, p3);
	}
	
	
}

