import java.util.*;
import java.lang.Math;
public class KMeans{
  public KMeans()
  {

  }
  public void step(DataPoint[] datapoints,DataPoint[] kpoints)
  {
    //Recalculate K means specified times
      //################Classify points by k:###################
      for(DataPoint datapoint : datapoints)
      {
        //classify the datapoint by a kpoint
        classify(datapoint,kpoints);
      }

      //############### Move Kpoint to mean of classified points ################
      for(int n = 0; n<kpoints.length;n++)
      {
        kpoints[n] = calcMeanPoint(datapoints,kpoints[n].getLabel());
      }
  }
  public DataPoint[] findKPoints(DataPoint[] datapoints,int k,int iterations)
  {
    //##############Create initial K's#######################
    DataPoint[] kpoints = new DataPoint[k];
    //Choose initial K points randomly
    Random rng = new Random();
    for(int i =0; i<k;i++)
    {
      kpoints[i] = new DataPoint(datapoints[rng.nextInt(datapoints.length-1)]);
      kpoints[i].setLabel(i);
      //System.out.print("New label: ");
      for(int n = 0; n<kpoints[i].getDimension();n++)
      {
        //System.out.print(kpoints[i].getCoordinate(n) + ",");
      }
      //System.out.println(" with class " + kpoints[i].getLabel());
    }

    //Recalculate K means specified times
    for(int i =0; i<iterations; i++)
    {
      //################Classify points by k:###################
      for(DataPoint datapoint : datapoints)
      {
        //classify the datapoint by a kpoint
        classify(datapoint,kpoints);
      }

      //############### Move Kpoint to mean of classified points ################
      for(int n = 0; n<kpoints.length;n++)
      {
        kpoints[n] = calcMeanPoint(datapoints,kpoints[n].getLabel());
      }
    }

    return kpoints;
  }
  private void classify(DataPoint point,DataPoint[] kpoints)
  {
    double distance;
    double closest = calcDistance(kpoints[0],point);
    point.setLabel(kpoints[0].getLabel());
    for(DataPoint kpoint : kpoints)
    {
      distance = calcDistance(kpoint,point);
      if(distance < closest)
      {
        closest = distance;
        point.setLabel(kpoint.getLabel());
      }
    }
  }

  private double calcDistance(DataPoint a, DataPoint b)
  {
    double distance = 0;
    for(int i =0; i<a.getDimension();i++)
    {
      distance += java.lang.Math.pow(a.getCoordinate(i)-b.getCoordinate(i),2);
    }
    distance = java.lang.Math.sqrt(distance);
    return distance;
  }
  //Calculate the mean point of all sets labeled with class k
  private DataPoint calcMeanPoint(DataPoint[] datapoints,int label)
  {
    int count = 0;
    double runningTotal = 0;
    int dimension = datapoints[0].getDimension();
    double[] coordinates = new double[dimension];
    for(int i = 0; i<dimension;i++)
    {
      for(DataPoint point : datapoints)
      {
        if(point.getLabel() == label)
        {
          runningTotal += point.getCoordinate(i)*point.getWeight();
          count+= point.getWeight();
        }
      }
      coordinates[i] = runningTotal/count;
      runningTotal =0;
      count = 0;
    }
    DataPoint newPoint = new DataPoint(coordinates);
    newPoint.setLabel(label);
    //System.out.println("NEW MEAN:");
    //newPoint.print();
    return newPoint;
  }
}
