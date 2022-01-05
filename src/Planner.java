// Gleb Zvonkov
// April 14, 2021

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;


public class Planner { ////This method throws a GraphException if there is no edge between u and v
	
	private Graph graph;   // the graph 
	private int start;    // the start of the bus
	private int destination;   //the destination of the bus
	private Stack<Node> stack; //stack to contain the nodes of the bus route
	private Vector<Character> busLines;  // vector containing that contains buslines that can only be used once
	
	
	
	Planner(String inputFile) throws GraphException, FileNotFoundException{ //Creates the graph that represents the city map and bus lines
		
    	int linenum = 0;   //current linenum in the graph 
    	int counter = 0;  // counter for the nodes 
    	int width = 0;    //width of the graph
    	int height = 0;  //height of the graph
    	int n = 0;   // number of nodes
    	stack = new Stack<Node>();  // Initialize stack
    	busLines = new Vector<Character>(); //Initialize the buslines to be used only once
    	graph = null; //Initialize graph to null
    	start = 0; //Initialize start to 0 
    	destination = 0; //Initialize destination to 0
    	
        File myObj = new File(inputFile);  // file to be read in 
        Scanner myReader = new Scanner(myObj);  // scanner
        
        while (myReader.hasNextLine()) {   //continue to read in until no nextline
        	
          String str = myReader.nextLine(); // the line currently read in 
          
          if (linenum== 0)  //if first line do nothing since its the scale factor C
        	  linenum = 0;  
          
          else if (linenum== 1) //if second line read in width
        	  width=Integer.valueOf(str); 
          
          else if (linenum== 2) { // if third line read in height
        	  height=Integer.valueOf(str);
        	  n = height* width; //set the number of nodes
        	  graph = new Graph (n); //intialize graph with n nodeswith names 0 to n-1
          }
          
          else if (linenum==3) { //set the bus lines only to be used one at a time
        	  for (int i=0;i<str.length();i+=2) { //skip a character since space between character 
        		  this.busLines.add(str.charAt(i));  //add character to busline vector
        	  }  		
          }
          else { //otherwise read in data for edges and nodes
        	  
        	  if (linenum%2==0) { //even number lines, horizontal
    	          for (int i=0;i<str.length();i++) { // read in each character
    	        	
    	        	  char c = str.charAt(i); //current character being read in 
    	        	  
    	        	  boolean uppercase = 64<(int)c && (int)c<91;   //set to true if character is uppercase letter
    	        	  boolean lowercase = 96<(int)c && (int)c<123 ; //set to true if character is lowercase character
    	        	  
    	        	  if (i!=0 && i%2== 0)  //if odd character then increase counter
    	        		  counter++;
    	  
    	        	  if ( i%2 != 0 ) { // if even character then it may be an edge
	    	        	  if (uppercase || lowercase ) // if uppercase or lowercase letter then add corresponding edge 
	    	        		  graph.addEdge( graph.getNode(counter), graph.getNode(counter+1), String.valueOf(c) );
    	        	  }
    	        	  
    	        	  if(c=='S')   // if character is S then set start
    	        		  start = counter;
    	        	  
    	        	  if (c=='D') // if character is D then set destination
    	        		  destination = counter;    
    	  
    	          }  
        	  }
        	  
        	  else { //odd number lines, vertical
    	          for (int i=0;i<str.length();i+=2) { //increment by two since only even character matter,
    	        	  counter++;
    	        	  char c = str.charAt(i); // get character 
    	        	  boolean uppercase = 64<(int)c && (int)c<91; //true if uppercase
    	        	  boolean lowercase = 96<(int)c && (int)c<123 ; //true if lowercase
    	        	  
    	        	  if (uppercase || lowercase )  // if uppercase or lowercase then add corresponding edge 
    	        		  graph.addEdge( graph.getNode(counter), graph.getNode(counter-width), String.valueOf(c) ); 
    	          }
    	          
    	          counter= counter - width+1;  //return counter to point to first node of row
        	  }
          } //end else  
          linenum++;  //increase line number since next line will be read in
        } // while loop bracket to continue reading in nextline

        myReader.close();  //close scanner
	}
	
	
	
	
	public Graph getGraph() throws MapException{  // Returns the graph representing the map
		if (graph != null) //
			return graph;
		else
			throw new MapException("error, the graph could not be created by the constructor");
	}
	
	
	
	
	public Iterator<Node> planTrip() throws GraphException{ // Returns a Java Iterator containing the nodes along the path from the starting point to the destination
		
			Iterator<Edge> incident = graph.incidentEdges(graph.getNode(start)); //iterator containg the incident edges
			Edge check; // edge to be checked
			int edges = 0; // counts the number of times an edge from set B has been used
			String line;  // stores one of the edge types from set B 

			while(incident.hasNext()){  //loop while iterator of incident edges is not empty
				
				check = incident.next(); //set check to be next element in iterator
				boolean notinset= true; // set to false if edge type is in set B
				Iterator<Character> bl = busLines.iterator(); // create iterator for set B
				line = " ";   // set line to empty string
				
				if (busLines.isEmpty() )  // if not element in set B then set line to empty string
					line = " ";
				else {
					while( bl.hasNext() ) { // run while element exists in set B
						if (bl.next().equals( check.getBusLine().charAt(0)  )  ) { //if already exists in set B 
							notinset = false;  //set boolean false
							line = check.getBusLine(); // set line to edge type of edge to be checked
						}
					}
					if (notinset) { // if check edge is not in set B 
						Iterator<Character> bl2 = busLines.iterator();  //create new iterator
						line = bl2.next().toString();  //set line to first element of iterator
					}
				} // end else
				
				path(graph.getNode(start), graph.getNode(destination), check, edges, line ); // call path method to check edge "check" 

				if(!stack.empty()){   // stack is not empty after path is called return iterator to stack
					return stack.iterator();
				}
			}
		 
		return null; // return null if the path does not exist
	}
	
	
	
	
	private boolean path(Node start, Node end, Edge check, int edges, String line) throws GraphException{ //method to find path from start to destination if it exists

		stack.push(start); //push starting node onto the stack

		if(start == end) // if node start is end then path is found so retrun true
			return true;
		
		else{
				start.setMark(true); //set current node to true
				Iterator<Edge> incident = graph.incidentEdges(start); // iterator to incident nodes of current node start

				while(incident.hasNext()){  // run while iterator has next edge
			
					Edge discovery = incident.next(); // set next incident edge to discovery

					Node u; // node that is connected to discovery edge
					if (start == discovery.firstEndpoint() ) // if first endpoint of edge is same as start
						u= discovery.secondEndpoint(); //then set node u to second endpoint
					else //otherwise set to first endpoint
						u= discovery.firstEndpoint();

					if(u.getMark() == false){ //if mark on node is false then
						
						boolean notinset= true;  //set to false if discovery edge is in set B
						Iterator<Character> bl = busLines.iterator(); //iterator of set B
						while( bl.hasNext() ) { //check if disvoery edge is in set B
							if (bl.next().equals( discovery.getBusLine().charAt(0)  )  ) {
								notinset = false; 
							}
						}
						
						if (notinset || line.equals(" ") || discovery.getBusLine().equals(line) ) { // if edge is not in set B or set B is empty  or edge is same as line
							
							if (discovery.getBusLine().equals(line)) // if discovery edge is equal to line edge then increment edge
								++edges;    
							
							check = discovery; //set check edge to discovery edge
							if(path(u, end, check, edges, line)) // recursivel call that stops when it returns true
								return true;
						}
						
						else{   //otherwise  check if edges equal zero

							if(edges == 0 ){ //if edges equals 0 then 
								check = discovery; //set check edge to discvoery edge
								
								bl = busLines.iterator(); // set bl to new iterator of set B
								while( bl.hasNext() ) {
									Character x = bl.next(); // get next element in set B
									if ( !x.equals( line.charAt(0)  )  )  // if next element in set B is not equla to line
										line =  Character.toString(x);   // set line to new element in set B
								}
								
								if(path(u,end,check, ++edges, line))  //recursive call with new line
									return true; 
							}
						} // end else
					}  // end if 
				} // end while

				start.setMark(false); //set node mark to false
				edges--;  //decrement edges
				stack.pop(); // pop last element of the stack
 
		} // end else

		return false; // return false if not reurned true via recursive calls
	}
	
	
	
		
} // end class


	



