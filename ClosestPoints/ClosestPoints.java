import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class ClosestPoints
{
	public static void main(String[] args) throws FileNotFoundException
	{
		testOutputForAllFiles();
	}
  
	// parsing
	
	private static double[][] parsePoints(String filename) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(filename));
		
		ArrayList<double[]> points = new ArrayList<double[]>();
		
		while(true)
		{
			if(!scanner.hasNextLine())
				break;
			String line = scanner.nextLine();
			if(line.length() == 0)
				continue;
			if(Character.isDigit(line.charAt(0)) || line.charAt(0) == ' ')
			{
				String[] nums = (" " + line).split(" +");
				if(nums.length < 4)
					continue;
				int index = Integer.parseInt(nums[1])-1;
				double[] point = new double[2];
				point[0] = Double.parseDouble(nums[2]);
				point[1] = Double.parseDouble(nums[3]);
				points.add(point);
			}
		}
		return points.toArray(new double[0][0]);
	}
	
	//  algorithm
	
	
	//reads input as a list of 2d-points, i.e. [[1, 2],  [3, 4],  [5, 6]]
	//returns a list with the indices of the two closest points in the input list, i.e. [2, 5]
	public static int[] closestPoints(double[][] points)
	{
		int[] minIndex = new int[]{0, 1};
		double minDist = Double.MAX_VALUE;
		
		int splitPoint = points.length / 2;
		
		// splitting
		
		double[][] xSorted = points.clone();
		Arrays.sort(xSorted, (a, b) -> {return Double.compare(a[0], b[0]);});
		
		double[][] xSortedA = Arrays.copyOfRange(xSorted, 0, splitPoint);
		double[][] xSortedB = Arrays.copyOfRange(xSorted, splitPoint, xSorted.length);
		
		// recursive calls
		
		if(xSortedA.length > 1)
		{
			int[] closestA = closestPoints(xSortedA);
			double aDist = euclid(xSortedA[closestA[0]], xSortedA[closestA[1]]);
			if(aDist < minDist)
			{
				minDist = aDist;
				minIndex = new int[]{Arrays.asList(points).indexOf(xSortedA[closestA[0]]), Arrays.asList(points).indexOf(xSortedA[closestA[1]])};
			}
		}
		if(xSortedB.length > 1)
		{
			int[] closestB = closestPoints(xSortedB);
			double bDist = euclid(xSortedB[closestB[0]], xSortedB[closestB[1]]);
			if(bDist < minDist)
			{
				minDist = bDist;
				minIndex = new int[]{Arrays.asList(points).indexOf(xSortedB[closestB[0]]), Arrays.asList(points).indexOf(xSortedB[closestB[1]])};
			}
		}
		
		// finding points within the bands
		// we assume the splitting line to be at the first point in xSortedB
		// linear search for simplicity, could have been binary
		
		double bandLowCut = xSortedB[0][0] - minDist;
		double bandHiCut = xSortedB[0][0] + minDist;
		
		int bandLow = xSortedA.length - 1;
		while(bandLow > 0 && xSortedA[bandLow][0] > bandLowCut)
			bandLow--;
		
		int bandHi = 0;
		while(bandHi < xSortedB.length && xSortedB[bandHi][0] < bandHiCut)
			bandHi++;
		
		double[][] band = new double[(xSortedA.length - bandLow) + bandHi][2];
		System.arraycopy(xSortedA, bandLow, band, 0, (xSortedA.length - bandLow));
		System.arraycopy(xSortedB, 0, band, (xSortedA.length - bandLow), bandHi);
		
		// sort on y
		
		Arrays.sort(band, (a, b) -> Double.compare(a[1], b[1]));
		
		
		// comparing points in bands
		
		for(int i=0;i<band.length;i++)
			for(int j=1;j<11;j++)
			{
				if(i+j >= band.length)
					continue;
				double dist = euclid(band[i], band[i+j]);
				if(dist < minDist)
				{
					minIndex = new int[]{Arrays.asList(points).indexOf(band[i]), Arrays.asList(points).indexOf(band[i+j])};
					minDist = dist;
				}
			}
		return minIndex;
	}
	
	private static double euclid(double[] a, double[] b)
	{
		return Math.sqrt(Math.pow(a[0]-b[0], 2) + Math.pow(a[1]-b[1], 2));
	}
	
	// testing
	
	public static void testOutputForAllFiles() throws FileNotFoundException
	{
		File dataDir = new File("data-cp");
		for(File file : dataDir.listFiles())
		{
			String inFile = file.getPath();
			
			if(inFile.equals("data-cp\\closest-pair-out.txt") || inFile.startsWith("data-cp\\close-pairs-"))
				continue;
			
			testOutput(inFile);
		}
	}
	
	public static void testOutput(String filename) throws FileNotFoundException
	{
		System.out.println(filename);
		
		double[][] points = parsePoints(filename);
	
		int[] solution = closestPoints(points);
		
		Scanner scanner = new Scanner(new File("data-cp/closest-pair-out.txt"));
		String[] testValues = new String[3];
		String line;
		boolean found=false;
		while(scanner.hasNextLine() && found==false)
		{
			line = scanner.nextLine();
			testValues = line.split("\\s+");
			testValues[0] = testValues[0].substring(0, testValues[0].length()-1);
			if(testValues[0].equals(filename.replace("data-cp\\", "../data/").replace("-tsp.txt", ".tsp")))
				found=true;
		}
		if(found==true)
		{
			if(Math.abs(Double.parseDouble(testValues[2]) - euclid(points[solution[0]], points[solution[1]])) < 0.000001) //close enough for our purposes
				System.out.println("Correct");
			else
				System.out.printf("Incorrect:\n%s:\n%d:(%f, %f)   %d:(%f, %f)\ndistance %f\nshould be %s\n\n", filename, solution[0]+1, points[solution[0]][0], points[solution[0]][1], 
			solution[1]+1, points[solution[1]][0], points[solution[1]][1], euclid(points[solution[0]], points[solution[1]]), testValues[2]);
			System.out.println();
		}
		else
		{
			System.out.println("Output data not found\n");
		}
	}
}