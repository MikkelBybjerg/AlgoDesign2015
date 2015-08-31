import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class StableMatching
{
	public static void main(String[] args)
	{
		//String filename = args[0];
		String filename = "sm-data/sm-illiad-in.txt";
		
		/*
		try
		{
			String[] matching = renderedMatchingFromFile(filename);
			
			for(String match : matching)
			{
				System.out.println(match);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.printf("File %s not found\n", filename);
		}
		*/
		
		testStableMatching();
	}
	
	//  matching algorithm
	
	public static String[] renderedMatchingFromFile(String filename) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(filename));
		int n = parseSize(scanner);
		
		String[][] names = parseNames(n, scanner);
		String[] manNames = names[0];
		String[] womanNames = names[1];
		
		scanner.nextLine();
		
		int[][][] weights = parseWeights(n, scanner);
		int[][] manWeights = weights[0];
		int[][] womanWeights = weights[1];
		
		scanner.close();
		return renderMatching(stableMatching(manWeights, womanWeights), manNames, womanNames);
	}
	
	public static int[][] stableMatching(int[][] manWeights, int[][] womanWeights)
	{
		int[][] matching = new int[manWeights.length][2];
		for(int i=0; i<manWeights.length; i++)
		{
			matching[i][0] = i;
			matching[i][1] = i;
		}
		return matching;
	}
	
	//  match rendering
	
	public static String[] renderMatching(int[][] matching, String[] manNames, String[] womanNames)
	{
		String[] ret = new String[matching.length];
		for(int i=0; i<matching.length; i++)
		{
			ret[i] = String.format("%s - %s", manNames[matching[i][0]], womanNames[matching[i][1]]);
		}
		return ret;
	}
	
	//  file parsing
	
	public static int parseSize(Scanner scanner)
	{
		while(true)
		{
			String line = scanner.nextLine();
			if(!line.startsWith("#"))
				return Integer.parseInt(line.split("=")[1]);
		}
	}
	
	public static String[][] parseNames(int n, Scanner scanner)
	{
		String[][] names = new String[2][n];
		
		for(int i=0; i<n; i++)
		{
			names[0][i] = scanner.nextLine().split(" ")[1];
			names[1][i] = scanner.nextLine().split(" ")[1];
		}
		
		return names;
	}
	
	public static int[][][] parseWeights(int n, Scanner scanner)
	{
		int[][][] weights = new int[2][n][n];
		for(int i=0; i<n; i++)
		{
			String[] mw = scanner.nextLine().split(": ")[1].split(" ");
			for(int m=0; m<n; m++)
				weights[0][i][m] = Integer.parseInt(mw[m]);
			
			String[] ww = scanner.nextLine().split(": ")[1].split(" ");
			for(int w=0; w<n; w++)
				weights[1][i][w] = Integer.parseInt(ww[w]);
		}
		return weights;
	}
	
	//  testing
	
	public static void testStableMatching()
	{
		File dataDir = new File("sm-data");
		for(File file : dataDir.listFiles())
		{
			String inFile = file.getPath();
			
			if(!inFile.endsWith("-in.txt"))
				continue;
			
			String outFile = inFile.replace("-in.txt", "-out.txt");
			
			String[] solution;
			try
			{
				solution = renderedMatchingFromFile(inFile);
			}
			catch(FileNotFoundException e)
			{
				System.out.printf("Test %s failed: file not found\n", inFile);
				return;
			}
			testMatchingAgainstFile(solution, outFile);
			
			System.out.println("Test finished");
		}
	}
	
	public static void testMatchingAgainstFile(String[] solution, String filename)
	{
		Scanner scanner;
		try
		{
			scanner = new Scanner(new File(filename));
		}
		catch(FileNotFoundException e)
		{
			System.out.printf("Test %s failed: file not found\n", filename);
			return;
		}
		
		String[] correct = new String[solution.length];
		for(int i=0; i<solution.length; i++)
		{
			correct[i] = scanner.nextLine();
		}
		scanner.close();
		
		Arrays.sort(solution);
		Arrays.sort(correct);
		
		for(int i=0; i<solution.length; i++)
		{
			if(! solution[i].equals(correct[i]))
			{
				System.out.printf("Test %s failed: pair \"%s\" should be \"%s\"\n", filename, solution[i], correct[i]);
			}
		}
	}
}