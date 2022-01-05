// Gleb Zvonkov
// April 14, 2021

import java.util.Iterator;
import java.util.Vector;

public class Graph implements GraphADT { //This class implements an undirected graph using an adjacency matrix. 
	
	private Edge[][] matrix; //the adjacency matrix 
	private Node[] nodes;  //the nodes 
	private int n;  //the number of nodes

	
	Graph(int n){  // Creates a graph with n nodes and no edges. This is the constructor for the class.
	
		this.n = n; 
		matrix = new Edge[n][n]; 
		nodes = new Node[n];

		for (int i=0; i<n; i++)    //Initialize the nodes
			nodes[i] = new Node (i);  
		
		for (int i=0; i<n; i++) {  //Initialize the matrix with null values
			for (int j=0; j<n; j++) {
				matrix[i][j] = null;
			}
		}
	}
	
	
	public void addEdge(Node nodeu, Node nodev, String busLine) throws GraphException { //Adds an edge connecting vertices u and v
		
		if (nodeu == null || nodev==null) // throws exception if either node does not exist 
			throw new GraphException("error, node does not exist");
		
		if (nodeu.getName() > n-1 || nodev.getName() > n-1) //throw exception if nodes are out of bounds
			throw new GraphException("error, node does not exist");
		
		if (matrix[nodeu.getName()][nodev.getName()]!=null) //throw exception if in the graph there is already an edge connecting the given nodes.
			throw new GraphException("error, edge between the nodes already exists");
			
		
		matrix[nodeu.getName()][nodev.getName()] = new Edge (nodeu, nodev, busLine);  //add edge 
		matrix[nodev.getName()][nodeu.getName()] = new Edge (nodeu, nodev, busLine); // since undirected add same edge in both positions
		 
	}

	
	public Node getNode(int name) throws GraphException { //Returns the node with the specified name.
		
		if (name> n) //throws exception if name is out of bounds
			throw new GraphException("error, no node with this name exists");
		
		if (nodes[name]== null) // throws excpetion if node is null
			throw new GraphException("error, no node with this name exists");
		
		return nodes[name]; //return node with name
	}

	
	public Iterator<Edge> incidentEdges(Node u) throws GraphException { //Returns a Java Iterator storing all the edges incident on node u.
		
		if (u.getName() > n-1 ) //throw exception if node is out of bounds 
			throw new GraphException("error, node does not exist");
		
		Vector<Edge> edgesVector = new Vector<Edge>(); //vector for incideint edges
		boolean empty=true;   //boolean that signifies the the node has no incident edges
		
		for (int i=0; i<n; i++) { //look through matrix for edges
			if ( matrix[u.getName()][i] != null ) { // if matrix is not null then edges exist
				edgesVector.add(matrix[u.getName()][i]); // add edge to vector
				empty = false; //set empty to false
			}
		}
		
		if (empty) // if no edges then return null
			return null; 
		
		return edgesVector.iterator(); //return the vector storing the incident edges
	}

	
	public Edge getEdge(Node u, Node v) throws GraphException {    //Returns the edge connecting nodes u and v
		
		if (u.getName()> n-1 || v.getName()> n-1 ) //This method throws a GraphException if either of the nodes are out of bounds
			throw new GraphException("error, no node with this name exists");
		
		if (matrix[u.getName()][v.getName()] == null) //This method throws a GraphException if there is no edge between u and v
			throw new GraphException("error, no edge exists between the two node");
		
		return matrix[u.getName()][v.getName()] ; //return the edge corresponding with the two nodes
	}

	
	public boolean areAdjacent(Node u, Node v) throws GraphException { //Returns true if nodes u and v are adjacent; it returns false otherwise.
		
		if (u.getName()> n-1 || v.getName()> n-1 ) //This method throws a GraphException if either of the nodes are out of bounds
			throw new GraphException("error, no node with this name exists");
		
		if (matrix[u.getName()][v.getName()]!=null)  //returns true if there is an edge connecting the two nodes
			return true;
		else        //returns false otherwise
			return false; 
	}
	

}
