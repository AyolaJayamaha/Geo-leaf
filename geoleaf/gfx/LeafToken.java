package geoleaf.gfx;

import java.awt.*;
import java.awt.image.*;
import java.awt.Toolkit.*;
import java.awt.event.*;
import java.util.*;

/**
* class that represents a single token of a leaf image
*
* A token is specified by the coordinates of the line and its
* cosinus & sinus angle.
*/
public class LeafToken
{
	private int x1;		    // start X-coordinate of the line
	private int y1;		    // start Y-coordinate of the line
	private int x2;	      // end   X-coordinate of the line
  private int y2;       // end   Y-coordinate of the line
  private double cos;   // the cosinus angle of this line
  private double sin;   // the sinus angle of this line

  /**
  * Constructor
  */
	public LeafToken(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;

    // now calculate the cosinus && sinus to identify the direction
    // of this line
    calcCosinus();
    calcSinus();
	}

  /**
  * calcCosinus()
  *
  * special private method that calculates the cosinus angle
  * of the line with the specified coordinates.
  */
  private void calcCosinus()
  {
    int ax, ay;
    double hyp;

    ax = x2-x1;
    ay = y2-y1;

    // calculate the hypotenuse  c = sqrt(a2+b2)
    hyp = Math.sqrt(ax*ax + ay*ay);

    if(hyp == 0.0) cos = 0.0;
    else cos = ay/hyp;
  }

  /**
  * calcSinus()
  *
  * special private method that calculates the sinus angle
  * of the line with the specified coordinates.
  */
  private void calcSinus()
  {
    int ax, ay;
    double hyp;

    ax = x2-x1;
    ay = y2-y1;

    // calculate the hypotenuse  c = sqrt(a2+b2)
    hyp = Math.sqrt(ax*ax + ay*ay);

    if(hyp == 0.0) hyp = Math.abs(ax);

    sin = ax/hyp;
  }

  /**
  * All get methods
  */
  public int getX1()
  {
    return x1;
  }

  public int getY1()
  {
    return y1;
  }

  public int getX2()
  {
    return x2;
  }

  public int getY2()
  {
    return y2;
  }

  public double getCOS()
  {
    return cos;
  }

  public double getSIN()
  {
    return sin;
  }
}