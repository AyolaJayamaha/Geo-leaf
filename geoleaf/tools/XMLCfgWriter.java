package geoleaf.tools;

import java.util.*;
import java.io.*;

import org.xml.sax.*;

/**
* special class for loading/saving and administrating
* the project relevant data
*/
public class XMLCfgWriter
{
    /** Print writer. */
    protected PrintWriter fOut;

    /** Canonical output. */
    protected boolean fCanonical;

    /** Element depth. */
    protected int fElementDepth;

    /** Sets the output stream for printing. */
    public void setOutput(OutputStream stream)
    {
      try
      {
        java.io.Writer writer = new OutputStreamWriter(stream, "UTF-8");
        fOut = new PrintWriter(writer);
      }
      catch(UnsupportedEncodingException e)
      {
        System.err.println(e);
      }
    }

    //
    // ContentHandler methods
    //

    /** Start document. */
    public void startDocument()
    {
      fElementDepth = 0;

      fOut.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      fOut.println("<geoleaf version=\"1.0\">");
      fOut.flush();
    }

    /** Start document. */
    public void endDocument()
    {
      if(fElementDepth != 0) System.err.println("Error: ElementDepth > 0 on end of document!");

      fOut.println("</geoleaf>");
      fOut.flush();
    }

    /** Start element. */
    public void startElement(String element, Attributes attrs)
    {
      fElementDepth++;
      fOut.print('<');
      fOut.print(element);

      if(attrs != null)
      {
        int len = attrs.getLength();

        for (int i = 0; i < len; i++)
        {
          fOut.print(' ');
          fOut.print(attrs.getQName(i));
          fOut.print("=\"");
          normalizeAndPrint(attrs.getValue(i));
          fOut.print('"');
        }
      }
      fOut.print('>');
      fOut.flush();
    }

    /** End element. */
    public void endElement(String element)
    {
      fElementDepth--;
      fOut.print("</");
      fOut.print(element);
      fOut.print('>');
      fOut.flush();
    }

    /** Characters. */
    public void characters(String s)
    {
        normalizeAndPrint(s);
        fOut.flush();
    }

    /** Normalizes and prints the given string. */
    protected void normalizeAndPrint(String s)
    {
      int len = (s != null) ? s.length() : 0;
      for(int i = 0; i < len; i++)
      {
        char c = s.charAt(i);
        normalizeAndPrint(c);
      }
    }

    /** Normalizes and print the given character. */
    protected void normalizeAndPrint(char c)
    {
      switch(c)
      {
        case '<':
        {
          fOut.print("&lt;");
        }
        break;

        case '>':
        {
          fOut.print("&gt;");
        }
        break;

        case '&':
        {
          fOut.print("&amp;");
        }
        break;

        case '"':
        {
          fOut.print("&quot;");
        }
        break;

        case '\r':
        case '\n':
        {
          fOut.print("&#");
          fOut.print(Integer.toString(c));
          fOut.print(';');
        }
        break;

        default:
        {
          fOut.print(c);
        }
      }
    }
}
