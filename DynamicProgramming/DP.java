import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DP
{
    public String name, seq;
    public Boolean checked;
    
    public DP(String Name, String Seq) {
        name = Name;
        seq = Seq;
        checked = false;
    }
       
    public DP()
    {
    	name = "name";
        seq = "seq";
        checked = false;
    }
    
    public DP getOutput(DP x)
    {
    	return x;
    }
    
    public void printOutput()
    {
    	System.out.println(name);
    	System.out.println(seq);
    }

	
	public static void main(String[] args)
	{
		//long startTime = System.currentTimeMillis();
		
		String filename = args[0];
		
		alignSequences(filename);
		//long endTime   = System.currentTimeMillis();
		//long totalTime = endTime - startTime;
		//System.out.println(totalTime);
	}
	
	public static List<DP> readFromFile(String filename)  throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(filename));
		List<DP> entries = new ArrayList<DP>();
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			String name;
			if(String.valueOf(line.charAt(0)).equals(">"))
				{
				name = line.substring(1, line.length()-1);
				line = scanner.nextLine();
				DP entry = new DP(name, line);
				entries.add(entry);
				}
		}
		scanner.close();
		return entries;
	}
	
	public static void readBLOSUMMatrix(String filename, String[][] blosum)  throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(filename));
		int lineIndex = 0;
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if(!String.valueOf(line.charAt(0)).equals("#"))
			{		
			blosum[lineIndex] = line.split(" +");
			if(lineIndex > 0)
				for(int j=1;j<blosum[lineIndex].length;j++)
				{
					if(String.valueOf(blosum[lineIndex][j].charAt(0)).equals("-"))
						blosum[lineIndex][j] = blosum[lineIndex][j].substring(1);
					else if(!blosum[lineIndex][j].equals("0"))
						blosum[lineIndex][j] = "-" + blosum[lineIndex][j];		
				}
			lineIndex++;
			}
		}
		scanner.close();
		//printBLOSUM(blosum);

	}
	
	public static void printBLOSUM(String[][] blosum)
	{
		for(int i=0;i<blosum[1].length;i++)
		{
			for(int j=0; j<blosum[i].length; j++)
				System.out.print(blosum[i][j] + " ");
			System.out.println();
		}
	}

	public static List<DP> alignSequences(String filename)
	{
		String[][] blosum = new String[25][25];
		String BLOSUMPath = "src/gorilla_data/BLOSUM62.txt";
		try {
			readBLOSUMMatrix(BLOSUMPath, blosum);
		} catch (FileNotFoundException e) {
			System.out.println("BLOSUM File not found");
		}
		try {
			List<DP> entries = readFromFile(filename);
			for(DP entry1: entries)
				{
					for(DP entry2: entries)
					{
						if(!entry1.equals(entry2) && entry2.checked == false)
						{
							optimalAlignment(entry1, entry2, blosum);
						}
					}
					entry1.checked = true;
				}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		List<DP> result= new ArrayList<DP>();
		DP elem1 = new DP();
		result.add(elem1);
		//elem1.printOutput();
		return result;
	}
	
	public static void optimalAlignment(DP a, DP b, String[][] blosum)
	{
		String[][] A = new String[2][Math.max(a.seq.length(), b.seq.length())];
		String aux = a.seq;
		for(int i=0; i<Math.max(a.seq.length(), b.seq.length());i++)
			{
			if(aux.length()>0)
				{
				A[0][i] = String.valueOf(aux.charAt(0));
				aux = aux.substring(1);
				}
			else
				A[0][i] = "-";
			}
		aux = b.seq;
		for(int i=0; i<Math.max(a.seq.length(), b.seq.length());i++)
		{
			if(aux.length()>0)
				{
				A[1][i] = String.valueOf(aux.charAt(0));
				aux = aux.substring(1);
				}
			else
				A[1][i] = "-";
		}
		System.out.println(a.name + "--" + b.name + ": " + calculateOptimalCost(A, blosum));
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<Math.max(a.seq.length(), b.seq.length());j++)
				System.out.print(A[i][j] + "");
			System.out.println();
		}
		
		int optimal;
		if(a.seq.length() == 0)
			optimal = a.seq.length() * 4; 
		else if (b.seq.length() == 0)
			optimal = b.seq.length() * 4;
	}
	
	public static int calculateOptimalCost(String[][] A, String[][] blosum)
	{
		int result = 0;
		for(int i=0; i<A[0].length;i++)
		{
			int score = blosumScore(A[0][i], A[1][i], blosum);
			result = result + score;
		}
		
		return result;
	}
	
	public static int blosumScore (String a, String b, String[][] blosum)
	{
		if(a.equals("-") || b.equals("-"))
			return 4;
		else if(a.toUpperCase().equals(b.toUpperCase()))
		{
			for(int i=0; i<blosum[0].length;i++)
			{
				if(blosum[0][i].equals(a.toUpperCase()))
				{
					return Integer.parseInt(blosum[i][i]);
				}
					
			}
		}
		else {
				for(int i=0; i<blosum[1].length; i++)
				{
					if(blosum[0][i].equals(a.toUpperCase()))
					{
						for(int j=0; j<blosum[1].length; j++)
						{
							if(blosum[j][0].equals(b.toUpperCase()))
								return Integer.parseInt(blosum[i][j]);
						}
					}
				}
			}
		return 1234;
	}
}