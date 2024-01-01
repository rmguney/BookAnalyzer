import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Node {
    String word;
    int count;
    Node left, right;

    // Node constructor to initialize word, count, left, and right
    public Node(String word) {
        this.word = word;
        this.count = 1;
        this.left = this.right = null;
    }
}

public class BookAnalyzer {
    private Node root;

    // BookAnalyzer constructor to initialize the root
    public BookAnalyzer() {
        this.root = null;
    }

    // Insert a word into the binary search tree
    public void insert(String word) {
        root = insertRec(root, word.toLowerCase());
    }

    // Recursive helper function to insert a word into the binary search tree
    private Node insertRec(Node root, String word) {
        if (root == null) {
            return new Node(word);
        }

        // Compare the word with the root's word and insert accordingly
        int compare = word.compareToIgnoreCase(root.word);
        if (compare < 0) {
            root.left = insertRec(root.left, word);
        } else if (compare > 0) {
            root.right = insertRec(root.right, word);
        } else {
            // If the word already exists, increment the count
            root.count++;
        }

        return root;
    }

    // Perform inorder traversal of the binary search tree
    private void inorderTraversal(Node root, List<String> result) {
        if (root != null) {
            inorderTraversal(root.left, result);
            result.add(root.word);
            inorderTraversal(root.right, result);
        }
    }

    // Get the inorder traversal of the binary search tree
    public List<String> getInorderTraversal() {
        List<String> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    // Analyze a book by processing lines from a file
    public void analyzeBook(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Process a line by splitting it into words, cleaning and inserting each word
    private void processLine(String line) {
        String[] words = line.split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z']+", "").toLowerCase();
            if (!word.isEmpty() && !isStopgapWord(word)) {
                insert(word);
            }
        }
    }

    // Check if a word is a stopgap word
    private boolean isStopgapWord(String word) {
        String[] stopgapWords = {"a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", "to", "she", "was", "where", "will", "with"};
        for (String stopgap : stopgapWords) {
            if (word.equals(stopgap)) {
                return true;
            }
        }
        return false;
    }

    // Calculate the number of unique words in the binary search tree
    private int uniqueWordCount(Node root) {
        if (root == null) {
            return 0;
        }
        return 1 + uniqueWordCount(root.left) + uniqueWordCount(root.right);
    }

    // Calculate the total count of words in the binary search tree
    private int totalWordCount(Node root) {
        if (root == null) {
            return 0;
        }
        return root.count + totalWordCount(root.left) + totalWordCount(root.right);
    }

    // Calculate the maximum count of a word in the binary search tree
    private int maxWordCount(Node root) {
        if (root == null) {
            return 0;
        }
        return Math.max(root.count, Math.max(maxWordCount(root.left), maxWordCount(root.right)));
    }

    // Calculate the Hapax Legomena value in the binary search tree
    private double hapaxValue(Node root) {
        int hapaxWords = countHapaxWords(root);
        int totalWords = totalWordCount(root);
        return totalWords != 0 ? (double) hapaxWords / totalWords : 0.0;
    }

    // Count the number of Hapax Legomena (words with count 1) in the binary search tree
    private int countHapaxWords(Node root) {
        if (root == null) {
            return 0;
        }
        if (root.count == 1) {
            return 1 + countHapaxWords(root.left) + countHapaxWords(root.right);
        }
        return countHapaxWords(root.left) + countHapaxWords(root.right);
    }

    // Calculate the TTR (Type-Token Ratio) value in the binary search tree
    private double ttrValue(Node root) {
        int uniqueWords = uniqueWordCount(root);
        int totalWords = totalWordCount(root);
        return totalWords != 0 ? (double) uniqueWords / totalWords : 0.0;
    }

    // Calculate the sum of words with prefixes 'o' and 'e' in the binary search tree
    private int sumOEWords(Node root) {
        int sumO = countWordsWithPrefix(root, "o");
        int sumE = countWordsWithPrefix(root, "e");
        return sumO + sumE;
    }

    // Count the number of words with a given prefix in the binary search tree
    private int countWordsWithPrefix(Node root, String prefix) {
        if (root == null) {
            return 0;
        }
        int sum = countWordsWithPrefix(root.left, prefix) + countWordsWithPrefix(root.right, prefix);
        if (root.word.startsWith(prefix)) {
            sum += root.count;
        }
        return sum;
    }

    // Calculate the mean word length in the binary search tree
    private double meanWordLength(Node root) {
        int totalLength = totalWordLength(root);
        int totalWords = totalWordCount(root);
        return totalWords != 0 ? (double) totalLength / totalWords : 0.0;
    }

    // Calculate the total length of words in the binary search tree
    private int totalWordLength(Node root) {
        if (root == null) {
            return 0;
        }
        return root.word.length() * root.count + totalWordLength(root.left) + totalWordLength(root.right);
    }

    // Display various statistics based on the analyzed book
    public void displayStatistics() {
        System.out.println("Unique Word Count: " + uniqueWordCount(root));
        System.out.println("Total Word Count: " + totalWordCount(root));
        System.out.println("Max Word Count: " + maxWordCount(root));
        System.out.println("Hapax Value: " + hapaxValue(root));
        System.out.println("TTR Value: " + ttrValue(root));
        System.out.println("Sum of Words with Prefix 'o' and 'e': " + sumOEWords(root));
        System.out.println("Mean Word Length: " + meanWordLength(root));

        // Display the words in the binary search tree using inorder traversal
        System.out.println("\nWords in Inorder Traversal:");
        List<String> inorderTraversal = getInorderTraversal();
        for (String word : inorderTraversal) {
            System.out.println(word);
        }
    }

    // Main method to create a BookAnalyzer instance, analyze a book, and display statistics
    public static void main(String[] args) {
        BookAnalyzer analyzer = new BookAnalyzer();
        analyzer.analyzeBook("inputs/activeInput.txt");
        analyzer.displayStatistics();
    }
}
