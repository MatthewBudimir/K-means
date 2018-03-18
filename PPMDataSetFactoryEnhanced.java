import java.util.*;
import java.io.*;

public class PPMDataSetFactoryEnhanced implements DataSetFactory {
  private int height;
  private int width;
  private BufferedReader textReader;
  private Scanner parser;
  private String line;
  private DataPoint[] completeDataPoints;
  public PPMDataSetFactoryEnhanced(){}
  public double getNextDouble()
  {
    double out = 0;
    try
    {
      if(parser.hasNextDouble())
      {
        return parser.nextDouble();
      }
      else
      {
        //line must be finished. Get a new one.
        if((line=textReader.readLine()) != null)
        {
          // if(line.getChar(0) =='#')
          // {
          //   line = "";
          //   return getNextDouble();
          // }
          parser = new Scanner(line);
          if(parser.hasNextDouble())
          {
            return parser.nextDouble();
          }
          else
          {
            System.out.println("PPM is corrupted or damaged.Bad format");
            System.exit(1);
          }
        }
        else
        {
          //The user has asked for more things than the file has, meaning the ppm is broken
          System.out.println("PPM is corrupted or damaged. Not enough points defined.");
          System.exit(1);
        }
      }
    }
    catch(IOException e)
    {

    }
    return out;
  }
  public DataPoint[] loadData(String file)
  {
    double[] coordinates;
    //initialize so compiler doesn't complain
    DataPoint[] datapoints= new DataPoint[1];
    //Primative hashmap
    //what do we need. We need a hashmap (3D array) for quickly finding duplicates (points to things in compressed version)
    //We need a full image datapoint with duplicates map.
    //We need a compressed version that gets classified
    DataPoint[][][] usedMap = new DataPoint[256][256][256];
    try
    {
      FileReader fr = new FileReader(file);
      textReader = new BufferedReader(fr);
      //Read first line and ignore because it's just P3.
      line=textReader.readLine();
      //Read second line for dimensions:
      line=textReader.readLine();
      while(line.charAt(0) == '#')
      {
        //if the line is a comment skip it and try the next line
        line = textReader.readLine();
      }
      //Now that we have a line that isn't a comment, read it into the scanner
      parser = new Scanner(line);
      height = parser.nextInt();
      width = parser.nextInt();
      line = textReader.readLine();
      while(line.charAt(0) == '#')
      {
        //if the line is a comment skip it and try the next line.
        line = textReader.readLine();
      }
      //read next line and ignore because it's just RGB max
      line=textReader.readLine();
      //skip all comments after RGBMax
      while(line.charAt(0) == '#')
      {
        //if the line is a comment skip it and try the next line.
        line = textReader.readLine();
      }
      //The line you have now is the first line of pixels.
      //prep parser
      parser = new Scanner(line);
      completeDataPoints = new DataPoint[height*width];
      for(int i = 0; i<height*width;i++)
      {
        coordinates = new double[3];
        for(int n = 0; n<3;n++)
        {
          coordinates[n] = getNextDouble();
        }
        //check if this colour has already been added to our set
        if(usedMap[(int)coordinates[0]][(int)coordinates[1]][(int)coordinates[2]]!=null)
        {
          //if it has then add it to our complete set but don't add it to our set for K-means
          completeDataPoints[i] = usedMap[(int)coordinates[0]][(int)coordinates[1]][(int)coordinates[2]];
          //Increase this point's weighting to record that this one datapoints represents 2 datapoints.
          usedMap[(int)coordinates[0]][(int)coordinates[1]][(int)coordinates[2]].setWeight(usedMap[(int)coordinates[0]][(int)coordinates[1]][(int)coordinates[2]].getWeight()+1);
        }
        else
        {
          usedMap[(int)coordinates[0]][(int)coordinates[1]][(int)coordinates[2]] = new DataPoint(coordinates);
          completeDataPoints[i] = usedMap[(int)coordinates[0]][(int)coordinates[1]][(int)coordinates[2]];
          //add new pixel to map, add that pixel to the complete set...
        }
      }
      //Find out how many unique colours we found by iterating through the usedMap
      int unique =0;
      for(DataPoint[][] a : usedMap)
      {
        for(DataPoint[] b : a)
        {
          for(DataPoint c : b)
          {
            if(c != null)
            {
              unique++;
            }
          }
        }
      }
      //Add all unique pixels to datapoints so we can output it.
      datapoints = new DataPoint[unique];
      int current = 0;
      for(DataPoint[][] a : usedMap)
      {
        for(DataPoint[] b : a)
        {
          for(DataPoint c : b)
          {
            if(c != null)
            {
              datapoints[current] = c;
              current++;
            }
          }
        }
      }
    }
    catch(IOException e)
    {
      System.out.println("Failed to read file");
    }
    return datapoints;
  }
  public void printData(DataPoint[] datapoints, DataPoint[] kPoints, String filename)
  {
    try
    {
      PrintWriter writer = new PrintWriter(filename);
      int count = 0;
      writer.println("P3");
      writer.println(height + " " + width);
      writer.println(255);
      //completeDataPoints was modified by k-means because it points to the same thing as the datapoints we gave them.
      for(DataPoint datapoint : completeDataPoints)
      {
        count ++;
        int label = datapoint.getLabel();
        datapoint = kPoints[label];
        //Print out your pixel value based on your mean's value.
        writer.print((int)datapoint.getCoordinate(0) + " ");
        writer.print((int)datapoint.getCoordinate(1) + " ");
        writer.print((int)datapoint.getCoordinate(2) + " ");
        if(count % 6 == 0)
        {
          writer.println();
        }
      }
      writer.println();
      writer.close();
    }
    catch (IOException e)
    {
     // do something
    }
  }
}
