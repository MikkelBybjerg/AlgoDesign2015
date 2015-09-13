import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IP
{
	public static void main(String[] args)
	{
		//long startTime = System.currentTimeMillis();

		String filename = args[0];
		
		try {
			readFromFile(filename);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		//long endTime   = System.currentTimeMillis();
		//long totalTime = endTime - startTime;
		//System.out.println(totalTime);
	}
	
	public static void readFromFile(String filename) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(filename));
		int n = Integer.parseInt(scanner.nextLine());
		int d = -1;
		int[] latestD = new int[n];
		
		scanner.nextLine();
		//System.out.println(n);
		int[][] req = new int[4][n];
		for(int i=0; i<n; i++)
		{
  		    String nextL = scanner.nextLine();
			String[] splited = nextL.split("\\s+");
			req[0][i] = Integer.parseInt(splited[0]);
			req[1][i] = Integer.parseInt(splited[1]);
			req[2][i] = i;
			req[3][i] = -1;
		}
		
		quickSort(0, n-1, req[0], req[1], req[2], req[3]);

		for(int i=0;i<n;i++)
		{
			if(d==-1)
			{
				d++;
				req[3][i] = d;
				latestD[d] = req[1][i];
			}
			
			else if (d!=-1)
			{
				boolean found = false;
				for(int j=0; j<=d;j++)
				{
					if(latestD[j]<=req[0][i])
						{
						req[3][i] = j;
						latestD[j] = req[1][i];
						found=true;
						}
					if(found == true)
						break;
				}
				if(found == false)
				{
					d++;
					req[3][i] = d;
					latestD[d] = req[1][i];
				}
			}
		}
		System.out.println(d+1);
		System.out.println();
		
		quickSort(0, n-1, req[2], req[0], req[1], req[3]);
		for(int i=0;i<n;i++)
		{
			System.out.println(req[0][i] + " " + req[1][i] + " " + req[3][i]);
		}

		scanner.close();
	}
	
	private static void quickSort(int lowerIndex, int higherIndex, int[] array1, int[] array2, int[] array3, int[] array4) {
		int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = array1[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (array1[i] < pivot) {
                i++;
            }
            while (array1[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j, array1);
                exchangeNumbers(i, j, array2);
                exchangeNumbers(i, j, array3);
                exchangeNumbers(i, j, array4);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j, array1, array2, array3, array4);
        if (i < higherIndex)
            quickSort(i, higherIndex, array1, array2, array3, array4);
	}
	
	private static void exchangeNumbers(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}