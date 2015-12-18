/*
* Hieu Trung Nguyen
*/

import java.util.*;

/*
* This is a Huffman Tree class. It takes a string of message and encode it
* into binary of 0's and 1's. It is also able to decode back to the original
* text given the codes.
*/
public class CodingTree {
    public String bits;
    public MyHashTable<String, String> codes;
    private MyHashTable<String, Integer> frequencies;
    private TreeNode overallRoot;
    
    /*
    * Construct a Huffman Tree given the string message and execute
    * the Huffman algorithm to encode the message.
    */
    public CodingTree(String message) {
        codes = new MyHashTable<String, String>(32768);
        frequencies = new MyHashTable<String, Integer>(32768);
        countFrequency(message);
        overallRoot = buildTree();
        createCodeMap();
        bits = createStringBits(message);
        codes.stats();
    }
    
    /*
    * Count the frequency of each unique word
    * in the message
    */
    private void countFrequency(String message) {
    	StringBuilder longWord = new StringBuilder();
    	StringBuilder lengthOne = new StringBuilder();
    	for (int i = 0; i < message.length(); i++) {
    		char currentChar = message.charAt(i);
    		if (Character.isLetterOrDigit(currentChar) || (int) currentChar == 39 
    				|| (int) currentChar == 45) {
    			longWord.append(currentChar);
    		} else {
    			lengthOne.append(currentChar);
    			checkMap(longWord.toString());
    			checkMap(lengthOne.toString());
    			longWord.setLength(0);
    			lengthOne.setLength(0);
    		}
    	}
    }
    
    /*
     * Helper method to count frequency by putting
     * the word into the map or increment their frequency
     */
    private void checkMap(String currentWord) {
    	if (frequencies.containsKey((currentWord))) {
    		Integer frequency = frequencies.get(currentWord);
    		frequency++;
    		frequencies.put(currentWord, frequency);
    	} else {
    		frequencies.put(currentWord, 1);
    	}
    }
    
    /*
    * Build the Huffman Tree with the words
    * and its frequency then return the new tree
    */
    private TreeNode buildTree() {
    	PriorityQueue<TreeNode> queue = new PriorityQueue<TreeNode>();
        for (String currentWord : frequencies.keySet()) {
            queue.offer(new TreeNode(currentWord, frequencies.get(currentWord)));
        }
        TreeNode combinedFrequency = null;
        while (!queue.isEmpty()) {
            TreeNode firstNode = queue.poll();
            if (queue.peek() != null) {
                TreeNode secondNode = queue.poll();
                combinedFrequency = new TreeNode(firstNode.frequency + secondNode.frequency,
                firstNode, secondNode);
                queue.offer(combinedFrequency);
            }
        }
        return combinedFrequency;
    }
    
    /*
    * Create a map of code of characters with code of 0's and 1's as its value
    */
    private void createCodeMap() {
        createCodeMap(overallRoot, "");
    }
    
    /*
    * Helper method to create the map of code
    */
    private void createCodeMap(TreeNode root, String code) {
        if (root.left == null && root.right == null) {
            codes.put(root.wordValue, code);
        } else {
            createCodeMap(root.left, code + "0");
            createCodeMap(root.right, code + "1");
        }
    }
    
    
    /*
    * Create a string of 0's and 1's given the original
    * String message
    */
    private String createStringBits(String message) {
    	StringBuilder toReturn = new StringBuilder();
    	StringBuilder longWord = new StringBuilder();
    	StringBuilder lengthOne = new StringBuilder();
    	for (int i = 0; i < message.length(); i++) {
    		char currentChar = message.charAt(i);
    		if (Character.isLetterOrDigit(currentChar) || (int) currentChar == 39 
    				|| (int) currentChar == 45) {
    			longWord.append(currentChar);
    		} else {
    	    	lengthOne.append(currentChar);
    			toReturn.append(codes.get(longWord.toString()));
    			toReturn.append(codes.get(lengthOne.toString()));
    			longWord.setLength(0);
    			lengthOne.setLength(0);
    		}
    	}
    	return toReturn.toString();
    }
 
    
    /*
    * Extra Credit
    * Given a string of 0's and 1's, and a map of codes, reconstruct
    * the tree and decode the code back into the original message then
    * return that message
    */
    public String decode(String bits, MyHashTable<String, String> codes) {
        overallRoot = null;
        reconstructTree(codes);
        return createText(bits);
    }
    
    /*
    * Reconstruct the tree given a map of codes
    */
    private void reconstructTree(MyHashTable<String, String >codes) {
        for (String currentWord : codes.keySet()) {
            String code = codes.get(currentWord);
            overallRoot = reconstructTree(currentWord, code, overallRoot);
        }
    }
    
    /*
    * Helper method to reconstruct the tree
    */
    private TreeNode reconstructTree(String currentWord, String code, TreeNode root) {
        if (root == null)
            // expand by creating a new node if the path for the code does not exist
        root = new TreeNode();
        if (code.length() == 0) {
            root.wordValue = currentWord;
        } else if (code.charAt(0) == '0') {
            root.left = reconstructTree(currentWord, code.substring(1), root.left);
        } else {
            root.right = reconstructTree(currentWord, code.substring(1), root.right);
        }
        return root;
    }
    
    /*
    * Create the original String message from the bits of 0's and 1's
    * then return that message
    */
    private String createText(String bits) {
        StringBuilder myStringBuilder = new StringBuilder();
        int index = 0;
        TreeNode root = null;
        while (index < bits.length() && overallRoot != null) {
            root = overallRoot;
            while (index < bits.length() && (root.left != null && root.right != null)) {
                if (bits.charAt(index) == '0') {
                    root = root.left;
                } else if (bits.charAt(index) == '1') {
                    root = root.right;
                }
                index++;
            }
            myStringBuilder.append(root.wordValue);
        }
        return myStringBuilder.toString();
    }
    
    /*
    * Getter method for main to test and debug calculating frequencies
    * of characters
    */
    public MyHashTable<String, Integer> getMap() {
        return frequencies;
    }
    
    /*
     * Getter method for main to test and debug the layout of the current tree
     */
    public TreeNode getTree() {
    	return overallRoot;
    }
}
