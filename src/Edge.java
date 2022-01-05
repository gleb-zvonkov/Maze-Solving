// Gleb Zvonkov
// April 14, 2021

//This class implements an edge of the graph
public class Edge {
	
	private Node u;
	private Node b;
	private String busline;
	
	Edge(Node u, Node b, String busline){ // The constructor for the class. The first two parameters are the endpoints of the edge.  
		this.u = u;
		this.b = b;
		this.busline = busline;
	}
	
	Node firstEndpoint () { //Returns the first endpoint of the edge
		return this.u;
	}
	
	Node secondEndpoint () { // Returns the second endpoint of the edge.
		return this.b;
	}
	
	String getBusLine() { //Returns the name of the busLine to which the edge belongs
		return this.busline; 
	}
	

}
