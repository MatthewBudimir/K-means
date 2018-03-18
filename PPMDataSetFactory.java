import java.util.*;
import java.io.*;

public class PPMDataSetFactory implements DataSetFactory {
  private int height;
  private int width;
  private BufferedReader textReader;
  private Scanner parser;
  private String line;
  public PPMDataSetFactory(){}
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
    DataPoint[] datapoints = new DataPoint[1];
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
      datapoints = new DataPoint[height*width];
      for(int i = 0; i<height*width;i++)
      {
        coordinates = new double[3];
        for(int n = 0; n<3;n++)
        {
          coordinates[n] = getNextDouble();
        }
        datapoints[i] = new DataPoint(coordinates);
      }
    }
    catch(IOException e)
    {
      System.out.println("Failed to read file");
    }
    return datapoints;
  }
  public void printData(DataPoint[] datapoints, DataPoint[] kPoints,String filename)
  {
    try
    {
      PrintWriter writer = new PrintWriter(filename);
      int count = 0;
      writer.println("P3");
      writer.println(height + " " + width);
      writer.println(255);
      for(DataPoint datapoint : datapoints)
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
