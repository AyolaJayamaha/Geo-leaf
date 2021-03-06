package geoleaf.tools;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
* special class for loading/saving and administrating
* the project relevant data
*/

public class XMLFilter extends FileFilter
{
    // Accept all directories and all gif, jpg, or tiff files.
    public boolean accept(File f)
    {
      if (f.isDirectory())
      {
        return true;
      }

      String extension = Utils.getExtension(f);
	    if(extension != null)
      {
        if(extension.equals(Utils.xml))
        {
          return true;
        }
        else return false;
    	}

      return false;
    }

    // The description of this filter
    public String getDescription()
    {
      return "XML Files (*.xml)";
    }
}
