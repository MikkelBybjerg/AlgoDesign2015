import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Flow
{
	static class Edge
	{
		public int to, from;
		public int capacity;
		
		public Edge(int to, int from, int capacity)
		{
			this.to = to;
			this.from = from;
			this.capacity = capacity;
		}
	}
	
	class Graph
	{
		public int vertexCount;
		public Map<Integer, Edge[]> edges;
		
		public Graph(int vertexCount, Map<Integer, Edge[]> edges)
		{
			this.vertexCount = vertexCount;
			this.edges = edges;
		}
	}
	
	static class FlowAndCut
	{
		public int overAllFlow;
		public Graph flow;
		public Edge[] cut;
		
		public FlowAndCut(int overAllFlow, Graph flow, Edge[] cut)
		{
			this.overAllFlow = overAllFlow;
			this.flow = flow;
			this.cut = cut;
		}
		
		public FlowAndCut(int overAllFlow, Edge[] cut)
		{
			this.overAllFlow = overAllFlow;
			this.cut = cut;
		}
		
	}
	
	public static void main(String[] args)
	{
		
	}
	
	public static FlowAndCut FordFulkerson(Graph g)
	{
		return null;
	}
	
	public static Graph ParseGraphFile(String filename)
	{
		return null;
	}
	
	
	public static boolean test (FlowAndCut result, Graph g)   throws FileNotFoundException
	{
		Edge[] edge = new Edge[9];
		Scanner scanner = new Scanner(new File("src/flow_data/result.txt"));
		int i = 0;
		while(scanner.hasNextLine())
		{
			String[] line = new String[3];
			line = scanner.nextLine().trim().split(" ");
			edge[i] = new Edge(Integer.parseInt(line[0]),Integer.parseInt(line[1]),Integer.parseInt(line[2]));
			i++;
		}
		
		FlowAndCut test = new FlowAndCut(163, edge);
		
		return compareFlows(test, result);
	}
	
	public static boolean compareFlows(FlowAndCut a, FlowAndCut b)
	{	
		if(a.overAllFlow != b.overAllFlow)
			return false;
		
		if(a.cut.length != b.cut.length)
			return false;
		
		for(int i=0; i<a.cut.length; i++)
		{
			if(a.cut[i].from != b.cut[i].from)
				return false;
			else if (a.cut[i].to != b.cut[i].to)
				return false;
			else if(a.cut[i].capacity != b.cut[i].capacity)
				return false;
		}
		
		return true;
	}
}