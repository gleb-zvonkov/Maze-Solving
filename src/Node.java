// Gleb Zvonkov
// April 14, 2021

public class Node { //This class implements a node of the graph
	
	private int name;
	private boolean mark; 
	
	Node(int name){ //This is the constructor for the class and it creates a node with the given name.
		this.name = name; 
		this.mark = false;  //A node has an attribute called mark that initially has value false.
	}
	
	void setMark (boolean newMark){ // Changes the mark attribute to the specified value
		this.mark = newMark;	
	}
	
	boolean getMark() {   // Returns the value of the mark attribute.
		return this.mark;
	}
	
	int getName() {  //Returns the name of the node.
		return this.name;
	}
	
	boolean equal(Node otherNode){   //Returns true if this Node object has the same name as otherNode.
		if (this.getName() == otherNode.getName())
			return true;
		else
			return false; 
	}
	
	
}
