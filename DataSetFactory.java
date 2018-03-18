
public interface DataSetFactory {
  public DataPoint[] loadData(String file);
  public void printData(DataPoint[] datapoints,DataPoint[] kpoints,String filename);
}
