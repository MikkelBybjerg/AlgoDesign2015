import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;
import java.io.File;
import java.io.FileNotFoundException;

public class Flow
{
	static class Edge
	{
		public int from, to;
		public int capacity;
		
		public Edge(int from, int to, int capacity)
		{
			this.from = from;
			this.to = to;
			this.capacity = capacity;
		}
		
		public String toString()
		{
			return String.format("(%d, %d)  %d", from, to, capacity);
		}
	}
	
	static class Graph
	{
		public int vertexCount;
		public Map<Integer, List<Edge>> edges;
		
		public Graph(int vertexCount, List<Edge> edges)
		{
			this.vertexCount = vertexCount;
			this.edges = new HashMap<Integer, List<Edge>>();
			for(Edge e : edges)
			{
				if(!this.edges.containsKey(e.from))
					this.edges.put(e.from, new ArrayList<Edge>());
				this.edges.get(e.from).add(e);
			}
		}
		
		public List<Edge> getEdges()
		{
			ArrayList<Edge> ret = new ArrayList<Edge>();
			for(List<Edge> es : edges.values())
				ret.addAll(es);
			return ret;
		}
	}
	
	static class FlowAndCut
	{
		public int overAllFlow;
		public Graph flow;
		public List<Edge> cut;
		
		public FlowAndCut(int overAllFlow, Graph flow, List<Edge> cut)
		{
			this.overAllFlow = overAllFlow;
			this.flow = flow;
			this.cut = cut;
		}
	}
	
	static class PathFound
	{
		public boolean found;
		public List<Integer> searched;
		public List<Edge> path;
		
		public PathFound(boolean found, List<Integer> searched, List<Edge> path)
		{
			this.found = found;
			this.searched = searched;
			this.path = path;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Graph G = ParseGraphFile("flow_data/rail.txt");
		
		FlowAndCut flow = FordFulkerson(G, 0, G.vertexCount-1);
		
		System.out.printf("Maximum flow: %d\n", flow.overAllFlow);
		System.out.println("Minimum cut:");
		for(Edge e : flow.cut)
			System.out.println(e);
		
	}
	
	public static FlowAndCut FordFulkerson(Graph g, int source, int sink)
	{
		Graph resi = Residual(g);
		PathFound path;
		
		while(true)
		{
			path = BFSearch(resi, source, sink, (e) -> ResiEdge(resi, e).capacity != 0);
			if(!path.found)
				break;
			Augment(resi, path.path);
		}
		
		List<Edge> cut = new ArrayList<Edge>();
		for(Edge e : resi.getEdges())
			if(path.searched.contains(e.from) && !path.searched.contains(e.to))
				cut.add(new Edge(e.from, e.to, (e.capacity - ResiEdge(resi, e).capacity) / 2));
		
		
		int flow = 0;
		for(Edge e : cut)
			flow += e.capacity;
		
		return new FlowAndCut(flow, g, cut);
	}
	
	public static Graph ParseGraphFile(String filename) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(filename));
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		int verts = Integer.parseInt(scanner.nextLine());
		for(int i=0;i<verts;i++)
			scanner.nextLine();
		
		int arcs = Integer.parseInt(scanner.nextLine());
		for(int i=0;i<arcs;i++)
		{
			String[] line = scanner.nextLine().split(" ");
			edges.add(new Edge(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2])));
		}
		
		return new Graph(verts, edges);
	}
	
	public static Graph Residual(Graph G)
	{
		ArrayList<Edge> resi = new ArrayList<Edge>();
		for(Edge e : G.getEdges())
		{
			resi.add(new Edge(e.from, e.to, e.capacity));
			resi.add(new Edge(e.to, e.from, e.capacity));
		}
		return new Graph(G.vertexCount, resi);
	}
	
	public static Edge ResiEdge(Graph g, Edge e)
	{
		for(Edge r : g.edges.get(e.to))
			if(r.to == e.from)
				return r;
		return null;
	}
	
	public static void Augment(Graph g, List<Edge> path)
	{
		List<Edge> resiPath = new ArrayList<Edge>();
		for(Edge e : path)
			resiPath.add(ResiEdge(g, e));
		
		int bottleneck = Integer.MAX_VALUE;
		for(Edge e : resiPath)
			if(e.capacity != -1)
				bottleneck = Math.min(bottleneck, e.capacity);
		
		for(int i=0; i<path.size(); i++)
		{
			if(path.get(i).capacity != -1)
				path.get(i).capacity += bottleneck;
			if(resiPath.get(i).capacity != -1)
				resiPath.get(i).capacity -= bottleneck;
		}
	}
	
	public static PathFound BFSearch(Graph G, int start, int goal, Predicate<Edge> criteria)
	{
		ArrayList<Integer> toSearch = new ArrayList<Integer>();
		toSearch.add(start);
		
		ArrayList<Integer> searched = new ArrayList<Integer>();
		
		Map<Integer, List<Edge>> paths = new HashMap<Integer, List<Edge>>();
		paths.put(start, new ArrayList<Edge>());
		
		outer:
		while(true)
		{
			if(toSearch.size() == 0)
				return new PathFound(false, searched, null);
			int next = toSearch.remove(0);
			searched.add(next);
			for(Edge e : G.edges.get(next))
			{
				if(!paths.keySet().contains(e.to) && criteria.test(e))
				{
					ArrayList<Edge> newPath = new ArrayList<Edge>(paths.get(next));
					newPath.add(e);
					paths.put(e.to, newPath);
					toSearch.add(e.to);
					if(e.to == goal)
						break outer;
				}
			}
		}
		return new PathFound(true, searched, paths.get(goal));
	}
}