package geoleaf.gfx;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Toolkit.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
* class that represents a Species of a leaf Image
*
*/
public class LeafSpecies
{
  private String speciesName;
  private ArrayList images = null;
  private double[] ID;

  public LeafSpecies(String name)
  {
    speciesName = name;
    images = new ArrayList();
    ID = new double[0];
  }

  public void setName(String name)
  {
    speciesName = name;
  }

  public String getName()
  {
    return speciesName;
  }

  public void addImage(LeafImage limage)
  {
    images.add(limage);
  }

  public void removeImage(LeafImage limage)
  {
    images.remove(limage);
  }

  public LeafImage getImage(int idx)
  {
    return (LeafImage)images.get(idx);
  }

  public int numImages()
  {
    return images.size();
  }

  public void setID(double[] id)
  {
    this.ID = id;
  }

  public double[] getID()
  {
    return ID;
  }

  /**
  * toString() method
  *
  * this method is needed for the JTree to display the correct name
  * of this object
  */
  public String toString()
  {
    return speciesName+" ("+images.size()+")";
  }
}