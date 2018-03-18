public class DataPoint{
  private double[] coordinates;
  private int dimension;
  private int label; //The class K of this datapoint
  private int weight; //Using weighted points means we can have smaller datasets that would have had duplicate sets.
  public DataPoint(double[] coordinates)
  {
    this.dimension = coordinates.length;
    this.coordinates = coordinates;
    label = 0;
    weight = 1;
  }
  //Create a new point that is a duplicate of another point
  public DataPoint(DataPoint original)
  {
    dimension = original.getDimension();
    coordinates = new double[dimension];
    for(int i=0;i<dimension;i++)
    {
      coordinates[i] = original.getCoordinate(i);
    }
    label=0;
  }
  public int getDimension()
  {
    return dimension;
  }
  public double[] getCoordinates()
  {
    return coordinates;
  }
  public double getCoordinate(int n)
  {
      return coordinates[n];
  }
  public void setCoordinate(int n, int val)
  {
    coordinates[n] = val;
  }
  public int getLabel()
  {
    return label;
  }
  public void setLabel(int n)
  {
    this.label = n;
  }
  public int getWeight()
  {
    return weight;
  }
  public void setWeight(int n)
  {
    weight = n;
  }
  public void print()
  {
    System.out.print("Label: " + label + " | ");
    int i = 0;
    System.out.print("Coordinates: ");
    for(double x : coordinates)
    {
      System.out.print(x + ",");
    }
    System.out.println();
  }
  //print it in format for excel to graph:
  public String filePrint()
  {
    String out = "";
    out += label + " ";
    for(double n : coordinates)
    {
      out+= n+" ";
    }
    return out;
  }

}
