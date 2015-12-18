/*
* Hieu Trung Nguyen
*/

/*
* This is a TreeNode class that stores a character and its frequency
*/
public class TreeNode implements Comparable<TreeNode> {
    public String wordValue;
    public int frequency;
    public TreeNode left;
    public TreeNode right;
    
    /*
    * Construct a new node given a character, frequency, left child and right child
    */
    public TreeNode(String wordValue, int frequency, TreeNode left, TreeNode right) {
        this.wordValue = wordValue;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    
    /*
    * Construct a new node given a frequency, left child and right child
    */
    public TreeNode(int frequency, TreeNode left, TreeNode right) {
        this("", frequency, left, right);
    }
    
    /*
    * Construct a new node given a character and its frequency
    */
    public TreeNode(String wordValue, int frequency) {
        this(wordValue, frequency, null, null);
    }
    
    /*
    * Construct a new node given a frequency
    */
    public TreeNode(int frequency) {
        this("", frequency, null, null);
    }
    
    /*
    * Construct a node with a frequency of 0
    */
    public TreeNode() {
        this("", 0, null, null);
    }
    
    /*
    * Return a string representation of the TreeNode
    * It only display the frequency since this method
    * is mainly for testing purpose
    */
    public String toString() {
        return "Frequency: " + frequency;
    }
    
    /*
    * Return the comparing value of this TreeNode with
    * the other TreeNode base on their frequencies.
    */
    public int compareTo(TreeNode other) {
        return this.frequency - other.frequency;
    }
}