import java.util.*;
import java.io.*;
public class DataSet implements DataSetFactory {
  public DataSet(){}
  public DataPoint[] loadData(String file)
  {
    String line;
    Scanner parser;
    double[] coordinates;
    DataPoint[] datapoints = new DataPoint[1];
    try
    {
      FileReader fr = new FileReader(file);
      BufferedReader textReader = new BufferedReader(fr);
      //Read first line
      line=textReader.readLine();
      parser = new Scanner(line);
      int size = parser.nextInt();
      datapoints = new DataPoint[size];
      int i=0;
      while((line=textReader.readLine()) != null)
      {
        System.out.println(line);
        parser = new Scanner(line);
        coordinates = new double[2];
        coordinates[0] = parser.nextDouble();
        coordinates[1] = parser.nextDouble();
        datapoints[i] = new DataPoint(coordinates);
        i++;
      }
    }
    catch(IOException e)
    {
      System.out.println("Failed to read file");
    }
    return datapoints;
  }
  public void printData(DataPoint[] a,DataPoint[] b,String fn)
  {
    try
    {
      PrintWriter writer = new PrintWriter(fn);
      //completeDataPoints was modified by k-means because it points to the same thing as the datapoints we gave them.
      for (DataPoint d : a )
      {
        writer.println(d.filePrint());
      }
      writer.close();
    }
    catch (IOException e)
    {
     // do something
    }

  }
}
