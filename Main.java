import java.util.*;
import java.io.*;
public class Main{
  public static void main(String[] args) {
    DataPoint[] list;
    DataSetFactory dataset;
    DataPoint[] ks;
    KMeans kmeans = new KMeans();
    if(args.length < 3)
    {
      System.out.println("Not enough arguments provided. Usage: java Main filename k ExecutionType. See readme for more");
      System.exit(0);
    }
    else
    {
      String filename = args[0];
      Scanner scan = new Scanner(args[1]);
      int kSize = scan.nextInt();
      String executionType = args[2];
      if(filename.lastIndexOf('.') == -1)
      {
        System.out.println("Filetype extension required on " + filename);
        System.exit(0);
      }
      String filetype = filename.substring(filename.lastIndexOf('.'));
      System.out.println(filetype + "#");
      switch (executionType) {
        case "2D":
          dataset = new DataSet();
          System.out.println("Reading file");
          list = dataset.loadData(args[0]);
          System.out.println("File read complete");
          System.out.println("Performing K-means");
          ks = kmeans.findKPoints(list,kSize,100);
          System.out.println("Writing to file out.txt");
          dataset.printData(list,ks,"out.txt");
        break;
        case "PPM":
          if(!filetype.equals(".ppm"))
          {
            System.out.println("PPM mode requires input of PPM file");
            System.exit(0);
          }
          dataset = new PPMDataSetFactory();
          System.out.println("Reading file");
          list = dataset.loadData(args[0]);
          System.out.println("File read complete");
          System.out.println("Performing K-means");
          ks = kmeans.findKPoints(list,kSize,100);
          System.out.println("Writing to file");
          dataset.printData(list,ks,"out.ppm");
          System.out.println("Done. Written to out.ppm");
        break;
        case "PPME":
          if(!filetype.equals(".ppm"))
          {
            System.out.println("PPM mode requires input of PPM file");
            System.exit(0);
          }
          dataset = new PPMDataSetFactoryEnhanced();
          System.out.println("Reading file");
          list = dataset.loadData(args[0]);
          System.out.println("File read complete");
          System.out.println("Performing K-means");
          ks = kmeans.findKPoints(list,kSize,100);
          System.out.println("Writing to file");
          dataset.printData(list,ks,"out.ppm");
          System.out.println("Done Written to out.ppm");
        break;
        case "PPMES":
          dataset = new PPMDataSetFactoryEnhanced();
          System.out.println("Reading file. This may take a while");
          System.out.println("This mode outputs 100 images into the output directory providing snapshots of kmeans as it executes. Doing this with large files could take a while and use a lot of space. Are you sure you wish to proceed? (y/n)");
          scan = new Scanner(System.in);
          String answer = scan.nextLine();
          if(answer.lastIndexOf("y") == -1)
          {
              System.exit(0);
          }
          list = dataset.loadData(args[0]);
          System.out.println("File read complete");
          System.out.println("Performing K-means");
          ks = kmeans.findKPoints(list,kSize,1);
          dataset.printData(list,ks,"output/out1.ppm");
          for(int i = 2; i<100;i++)
          {
            kmeans.step(list,ks);
            dataset.printData(list,ks,"output/out" + i + ".ppm");
            System.out.println("This is iteration: " + i);
          }
          System.out.println("Writing to file");
          System.out.println("Done. You will find your files in the output directory.");
        break;
        default:
          System.out.println("Execution type not recognised: Allowed types are: 2D, PPM, PPME, PPMES");
        break;
      }
    }
  }
}
