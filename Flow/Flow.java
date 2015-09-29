import java.util.Map;

public class Flow
{
	class Edge
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
	
	class FlowAndCut
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
}