package geoleaf.tools;

import java.util.*;
import java.io.*;
import java.net.*;

import org.xml.sax.*;
import org.xml.sax.ContentHandler;

import geoleaf.gfx.*;
import geoleaf.nnetwork.*;

/**
* special class for loading/saving and administrating
* the project relevant data
*/
public class XMLCfgReader implements ContentHandler
{
  private ProjectEnv projectEnv;

  private boolean correctVersion = false;

  private ArrayList leafSpecies;

  private LeafSpecies actSpecies;
  private double[] actSpeciesID;
  private double actID;
  private int numID = -1;
  private LeafImage actImage;
  private LeafToken actToken;
  private BackProp actNetwork;

  private int numSpeciesID = 0;
  private int numHidden   = -1;
  private int numHiddenW  = -1;
  private int numOutput   = -1;
  private int numOutputW  = -1;

  private double actHiddenW;
  private double actBiasH;
  private double actOutputW;
  private double actBiasO;

  /**
  * Constructor
  */
  public XMLCfgReader(ProjectEnv prjEnv)
  {
    this.projectEnv = prjEnv;
    leafSpecies = new ArrayList();
  }

  public void setDocumentLocator(Locator locator)
  {
    //System.out.println("setDocumentLocator(" + locator + ")");
  }

  public void startDocument() throws SAXException
  {
    //System.out.println("startDocument()");
  }

  public void endDocument() throws SAXException
  {
    //System.out.println("endDocument()");
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
  {
    //System.out.println("startElement("+namespaceURI+", "+localName+", "+qName+")");

    if(qName.compareToIgnoreCase("geoleaf") == 0)
    {
      if(atts.getValue("version").compareToIgnoreCase("1.0") == 0)
      {
        correctVersion = true;
      }
    }
    else if(correctVersion)
    {
      if(qName.compareToIgnoreCase("leafSpecies") == 0)
      {
        actSpecies = new LeafSpecies(atts.getValue("name"));
        if(atts.getValue("IDlen") != null)
        {
          actSpeciesID = new double[Integer.parseInt(atts.getValue("IDlen"))];
        }
        numID = -1;
      }
      else if(qName.compareToIgnoreCase("ID") == 0)
      {
        numID++;
        actID = Double.parseDouble(atts.getValue("value"));
      }
      else if(qName.compareToIgnoreCase("leafImage") == 0)
      {
        if(projectEnv.getCodeBase() == null)
        {
          actImage = new LeafImage(new File(atts.getValue("file")));
        }
        else
        {
          // If the Application was started as a Applet we have to
          // get the image from the ImageChooser object
          actImage = projectEnv.getLeavesRecognition().getImageChooser().getLeafImage(atts.getValue("file"));
        }
      }
      else if(qName.compareToIgnoreCase("leafToken") == 0)
      {
        int x1 = Integer.parseInt(atts.getValue("x1"));
        int y1 = Integer.parseInt(atts.getValue("y1"));
        int x2 = Integer.parseInt(atts.getValue("x2"));
        int y2 = Integer.parseInt(atts.getValue("y2"));

        actToken = new LeafToken(x1, y1, x2, y2);
      }
      else if(qName.compareToIgnoreCase("backProp") == 0)
      {
        int input       = Integer.parseInt(atts.getValue("input"));
        int hidden      = Integer.parseInt(atts.getValue("hidden"));
        int output      = Integer.parseInt(atts.getValue("output"));
        double alpha    = Double.parseDouble(atts.getValue("alpha"));
        double momentum = Double.parseDouble(atts.getValue("momentum"));

        actNetwork = new BackProp(input, hidden, output, alpha, momentum);
        numHidden = numHiddenW = -1;
        numOutput = numOutputW = -1;
      }
      else if(qName.compareToIgnoreCase("hidden") == 0)
      {
        numHidden++;
        numHiddenW = -1;
      }
      else if(qName.compareToIgnoreCase("hiddenW") == 0)
      {
        numHiddenW++;
        actHiddenW = Double.parseDouble(atts.getValue("H"));
      }
      else if(qName.compareToIgnoreCase("biasH") == 0)
      {
        actBiasH = Double.parseDouble(atts.getValue("H"));
      }
      else if(qName.compareToIgnoreCase("output") == 0)
      {
        numOutput++;
        numOutputW = -1;
      }
      else if(qName.compareToIgnoreCase("outputW") == 0)
      {
        numOutputW++;
        actOutputW = Double.parseDouble(atts.getValue("O"));
      }
      else if(qName.compareToIgnoreCase("biasO") == 0)
      {
        actBiasO = Double.parseDouble(atts.getValue("O"));
      }
    }
  }

  public void endElement(String namespaceURI, String localName, String qName) throws SAXException
  {
    //System.out.println("endElement()");
    if(correctVersion)
    {
      if(qName.compareToIgnoreCase("leafSpecies") == 0 && actSpecies != null)
      {
        actSpecies.setID(actSpeciesID);
        leafSpecies.add(actSpecies);
        actSpecies = null;
      }
      else if(qName.compareToIgnoreCase("ID") == 0)
      {
        actSpeciesID[numID] = actID;
      }
      else if(qName.compareToIgnoreCase("leafImage") == 0 && actImage != null && actSpecies != null)
      {
        actSpecies.addImage(actImage);
        actImage = null;
      }
      else if(qName.compareToIgnoreCase("leafToken") == 0 && actToken != null && actImage != null)
      {
        actImage.addToken(actToken);
        actToken = null;
      }
      else if(qName.compareToIgnoreCase("backProp") == 0 && actNetwork != null)
      {
        projectEnv.setNetwork(actNetwork);
        actNetwork = null;
        numHidden = numHiddenW = -1;
        numOutput = numOutputW = -1;
      }
      else if(qName.compareToIgnoreCase("hiddenW") == 0 && actNetwork != null)
      {
        actNetwork.setHiddenW(actHiddenW, numHidden, numHiddenW);
      }
      else if(qName.compareToIgnoreCase("biasH") == 0 && actNetwork != null)
      {
        actNetwork.setBiasH(actBiasH, numHidden);
      }
      else if(qName.compareToIgnoreCase("outputW") == 0 && actNetwork != null)
      {
        actNetwork.setOutputW(actOutputW, numOutput, numOutputW);
      }
      else if(qName.compareToIgnoreCase("biasO") == 0 && actNetwork != null)
      {
        actNetwork.setBiasO(actBiasO, numOutput);
      }
    }
  }

  public void characters(char[] text, int start, int length) throws SAXException
  {
    //System.out.println("characters()");
  }

  public void ignorableWhitespace(char[] text, int start, int length) throws SAXException
  {
    //System.out.println("ignorableWhitespace()");
  }

  public void processingInstruction(String target, String data) throws SAXException
  {
    //System.out.println("processingInstruction(" + target + ", " + data + ")");
  }

  public void startPrefixMapping(String prefix, String uri) throws SAXException
  {
    //System.out.println("startPrefixMapping(\"" + prefix + "\", \"" + uri + "\")");
  }

  public void endPrefixMapping(String prefix) throws SAXException
  {
    //System.out.println("endPrefixMapping(\"" + prefix + "\")");
  }

  public void skippedEntity(String name) throws SAXException
  {
    //System.out.println("skippedEntity(" + name + ")");
  }

  public ArrayList getLeafSpecies()
  {
    return leafSpecies;
  }
}
