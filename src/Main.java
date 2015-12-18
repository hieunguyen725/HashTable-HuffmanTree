/*
* Hieu Trung Nguyen
*/

import java.io.*;

/*
* Main simulator of CodingTree.java
*/
public class Main {
    public static void main(String[] args) throws IOException {
        long beginTime = System.currentTimeMillis();
        
        String inputName = "WarAndPeace.txt"; // Change the file name to test different files
        String compressedName = "compressed.txt";
        
        // Read file to create a String message then build the Huffman tree
        // Thanks to the Java API Library for an overview of FileReader
        // and BufferedReader, BufferedWriter, BufferedOutputStream
        File inputFile = new File(inputName);
        BufferedReader input = new BufferedReader(new FileReader(inputFile));
        StringBuilder myStringBuilder = new StringBuilder();
        int charValue;
        while ((charValue = input.read()) != -1) {
            myStringBuilder.append((char) charValue);
        }
        String message = myStringBuilder.toString();
        CodingTree myTree = new CodingTree(message);
        myStringBuilder.setLength(0);
        input.close();
        
//        // Output the map of codes to a file
//        // Thanks to the JAVA API Library for an overview of FileWriter
        BufferedWriter codesOutput = new BufferedWriter(new FileWriter("codes.txt"));
        codesOutput.write(myTree.codes.toString());
        codesOutput.close();
        
        // Convert String bits to bytes and output it to a binary file
        // Thanks to the Java API library for a review of FileOutputStream
        File compressedFile = new File(compressedName);
        BufferedOutputStream binaryOutput = new BufferedOutputStream(new FileOutputStream(compressedFile));
        int remainingBits = myTree.bits.length() % 8;
        int length = myTree.bits.length() - remainingBits;
        for (int i = 0; i < length; i += 8) {
            for (int j = 0; j < 8; j++) {
                myStringBuilder.append(myTree.bits.charAt(i + j));
            }
            binaryOutput.write(Integer.parseInt(myStringBuilder.toString()));
            myStringBuilder.setLength(0);
        }
        binaryOutput.write(Integer.parseInt(myTree.bits.substring(length)));
        binaryOutput.close();
        
//        // decode the output codes back to the original text and output it to a file
        BufferedWriter decodedOutput = new BufferedWriter(new FileWriter("decoded.txt"));
        decodedOutput.write(myTree.decode(myTree.bits, myTree.codes));
        decodedOutput.close();
       
//        // Display statistics
        double ratio = (double) compressedFile.length() / inputFile.length() * 100;
        System.out.println("Original file size in bits   : " + inputFile.length() * 8 + 
        		" bits  (" + inputFile.length() + " bytes)");
        System.out.println("Compressed file size in bits : " + compressedFile.length() * 8 + 
        		" bits  (" + compressedFile.length() + " bytes)");
        System.out.println("Compressed ratio             : " + String.format("%.2f", ratio) + " %");
        long endTime = System.currentTimeMillis();
        System.out.println("Total Time                   : " + (endTime - beginTime) + " milliseconds");
        
        // Test or debug methods used, please comment out for use
//        testCountingFrequency(myTree);
//        testTreeLayout(myTree);
//        testHashTable();
    }
    
    public static void testHashTable() {
    	System.out.println("\n\n\nTesting Hash Table\n");
    	MyHashTable<String, String> table = new MyHashTable<String, String>(10);
    	table.put("Hieu", "121212");
    	System.out.println(table);
    	table.put("Trung", "454545");
    	System.out.println(table);
    	table.put("Nguyen", "989898");
    	System.out.println(table);
    	System.out.println(table.containsKey("Hieu"));
    	System.out.println(table.containsKey("Trung"));
    	System.out.println(table.containsKey("Data Structure"));
    	System.out.println(table.get("Nguyen"));
    	for (String key : table.keySet()) {
    		System.out.println("Key = " + key + ", Value = " + table.get(key));
    	}
    }
    
    public static void testCountingFrequency(CodingTree myTree) {
        System.out.println("\n\n\nTesting counting frequency\n");
        MyHashTable<String, Integer> map = myTree.getMap();
        for (String currentWord: map.keySet()) {
        	if (map.get(currentWord) > 500)
	            System.out.println("Letter = " + currentWord + "   Frequency = " + map.get(currentWord));
        }
    }
    
    public static void testTreeLayout(CodingTree myTree) {
    	// view the character and their current depth in their tree during debug
    	// to see if they makes logical sense according to Huffman's algorithm
    	System.out.println("\n\n\nCharacters in Tree and their current depth\n");
    	TreeNode overallRoot = myTree.getTree();
    	viewTree(overallRoot, 0);
    }
    
    private static void viewTree(TreeNode root, int depth) {
    	if (root != null) {
    		viewTree(root.left, depth++);
    		if (root.left == null && root.right == null) {
    			System.out.print(" (" + root.wordValue + " " + depth + ") ");
    		}
    		viewTree(root.right, depth++);
    	}
    }
}

// http://www.asciitable.com/
// http://docs.oracle.com/javase/7/docs/api/java/io/FileWriter.html
// http://docs.oracle.com/javase/7/docs/api/java/io/FileReader.html
// http://docs.oracle.com/javase/7/docs/api/java/io/FileOutputStream.html
// http://courses.cs.washington.edu/courses/cse142/13au/lectures/21-ch07-3-arrayparameters.pdf