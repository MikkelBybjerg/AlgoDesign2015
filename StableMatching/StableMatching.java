import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class GS
{
	public static void main(String[] args)
	{
		String filename = args[0];
		//String filename = "sm-data/sm-illiad-in.txt";
		
		//testMatchingForAllFiles();
		//String[] solution;
		try
		{
			renderedMatchingFromFile(filename);
		}
		catch(FileNotFoundException e)
		{
			System.out.printf("Test %s failed: file not found\n", filename);
			return;
		}
	}
	
	//  matching algorithm
	
	public static void renderedMatchingFromFile(String filename) throws FileNotFoundException
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
		stableMatching(manWeights, womanWeights, manNames, womanNames);
	}
	
	public static void stableMatching(int[][] manWeights, int[][] womanWeights, String[]manNames, String[]womanNames)
	{
		int n = manWeights.length;
		int[][] revWomenWeights = new int[womanWeights.length][womanWeights.length];
		int[][] matching = new int[n][2];
		
		List<Integer> freeMen = new ArrayList<>();
        for(int i = 0 ; i < n ; i++)
            freeMen.add(2*i+1);
		
        int nextWomen[] = new int[n];
        for(int i = 0 ; i < n ; i++)
            nextWomen[i] = 0;
        
        int[] currentPartners = new int[n];
        for(int i = 0 ; i < n ; i++)
        	currentPartners[i] = -1;
        
        revWomenWeights = reverseWomenWeights(womanWeights);
                
        while(!freeMen.isEmpty())
        {
        	int m = freeMen.get(0);
        	int w = manWeights[(m-1)/2][nextWomen[(m-1)/2]];
            nextWomen[(m-1)/2]++;
            if(currentPartners[(w-2)/2] == -1) {
                currentPartners[(w-2)/2] = (m-1)/2;
                freeMen.remove(0);
            }
            
            else {
                if(revWomenWeights[(w-2)/2][(m-1)/2] < revWomenWeights[(w-2)/2][currentPartners[(w-2)/2]]) {
                    freeMen.add((currentPartners[(w-2)/2])*2+1);
                    freeMen.remove(0);
                    currentPartners[(w-2)/2] = (m-1)/2; 
                }
            }
           
       
        }
        int[] femalePartner = new int[n];
		for(int i=0; i<currentPartners.length; i++)
			femalePartner[(currentPartners[i])]=(i+1)*2;

		for(int i=0; i<n;i++)
			System.out.println(manNames[i] + " -- " + womanNames[femalePartner[i]/2-1]);
		
		//return matching;
	}
	
	public static int[][] reverseWomenWeights(int[][] womanWeights)
	{
		int[][] revWomenWeights = new int[womanWeights.length][womanWeights.length];
		for(int i=0; i<womanWeights.length; i++)
			for(int j=0; j<womanWeights.length; j++)
				revWomenWeights[i][(womanWeights[i][j]-1)/2] = j*2+1;
		return revWomenWeights;
	}
	
	//  match rendering
	
	/*public static String[] renderMatching(int[][] matching, String[] manNames, String[] womanNames)
	{
		String[] ret = new String[matching.length];
		for(int i=0; i<matching.length; i++)
		{
			//ret[i] = String.format("%s - %s", manNames[(matching[i][0]-1)/2], womanNames[(matching[i][1]/2)-1]);
			//System.out.println(ret[i]);
		}
		return ret;
	}
	*/
	
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
	
	public static void testMatchingForAllFiles()
	{
		File dataDir = new File("src/sm-data");
		for(File file : dataDir.listFiles())
		{
			String inFile = file.getPath();
			
			if(!inFile.endsWith("-in.txt"))
				continue;
			
			String outFile = inFile.replace("-in.txt", "-out.txt");
			
			String[] solution;
			try
			{
				renderedMatchingFromFile(inFile);
			}
			catch(FileNotFoundException e)
			{
				System.out.printf("Test %s failed: file not found\n", inFile);
				return;
			}
			//testMatchingAgainstFile(solution, outFile);
			
			//System.out.println("Test finished");
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
				//System.out.printf("Test %s failed: pair \"%s\" should be \"%s\"\n", filename, solution[i], correct[i]);
			}
		}
	}
}