import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DP
{
    public String name1, name2;
    public int result;
    public String seq1, seq2;
    
    public DP(String name_1, String name_2, int res, String seq_1, String seq_2) {
        name1 = name_1;
        name2 = name_2;
        result = res;
        seq1 = seq_1;
        seq2 = seq_2;
    }
    
    public DP()
    {
    	name1 = "name1";
        name2 = "name2";
        result = 0;
        seq1 = "seq1";
        seq2 = "seq2";
    }
    
    public DP getOutput(DP x)
    {
    	return x;
    }
    
    public void printOutput()
    {
    	System.out.println(name1);
    	System.out.println(name2);
    	System.out.println(result);
    	System.out.println(seq1);
    	System.out.println(seq2);
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
	
	public static void readFromFile(String filename)  throws FileNotFoundException
	{
		
	}
	
	public static List<DP> alignSequences(String filename)
	{
		try {
			readFromFile(filename);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		
		List<DP> result= new ArrayList<DP>();
		DP elem1 = new DP("name1", "name2", 0, "seq1", "seq2");
		result.add(elem1);
		//elem1.printOutput();
		return result;
	}
}