package yetchina.play.trees;

import sun.reflect.generics.tree.Tree;

public class BinaryTreeWalmart {
	Node root;
	
	boolean addElement(Integer n){
		if (n!=null)
			Node nn = new Node(n);
		
		Integer tv = tree.value();
		    if(n== tree.value()) return true;
		    
		    Tree lTree = tree.left();
		    Tree rTree = tree.right();
		    
		    if(lTree.value()>n) //Add n here *****
		    else addElement(n, lTree);
		    
		     if(rTree.value()<n) //Add n here
		    else addElement(n, rTree);

		}
	
	boolean findNum(Integer n/4, Tree tree/6){
	    if(n==tree.value()) return true;
	 
	    lTree  = tree.left();
	    rTree- = tree.right();
	    if(lTree   != null) findNum(n, lTree);
	    if(rTree   != null) findNum(n, rTree);
	   return false;
	}
	
	
	public static void main(String[] args) {
		BinaryTreeWalmart bt = new BinaryTreeWalmart();
		bt
		
		bt.findNum(n)
	}

}

class Node {

	    int key;
	 
	    Node leftChild;
	    Node rightChild;
	 
	    Node(int key) {
	 
	        this.key = key;
	    }
	 
	    public String toString() {
	 
	        return " key " + key;
	    }
	}
