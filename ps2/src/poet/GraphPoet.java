package poet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import graph.Graph;

/**
 * A class that generates poetry using a word relationship graph.
 * 
 * <p>The GraphPoet class builds a word graph based on a given corpus.
 * Each word in the corpus is represented as a vertex in the graph.
 * An edge exists between two words if one follows the other in the corpus,
 * and the weight of the edge indicates the frequency of this occurrence.
 * 
 * <p>Example corpus:
 * <pre>    Hello, hello, farewell!    </pre>
 * <p>Resulting graph:
 * <ul><li> ("hello,") -> ("hello,")   with weight 1
 *     <li> ("hello,") -> ("farewell!") with weight 1 </ul>
 * 
 * <p>The `poem` method takes an input text and tries to insert "bridge words"
 * between consecutive words in the input, based on the word graph. A bridge
 * word is chosen to maximize the total edge weight along the two-step path
 * from word1 -> bridge -> word2.
 */
public class GraphPoet {
    
    private final Graph<String> wordGraph = Graph.empty();
    
    // Abstraction function:
    //   wordGraph represents the relationships between words in the corpus.
    // Representation invariant:
    //   wordGraph is immutable and represents a valid directed graph.
    // Safety from rep exposure:
    //   The graph is encapsulated as private final, and copies are returned
    //   where necessary.

    /**
     * Constructs a GraphPoet instance using the provided text corpus.
     * 
     * @param corpus the file containing the text corpus
     * @throws IOException if the file cannot be read
     */
    public GraphPoet(File corpus) throws IOException {
        try (Scanner fileScanner = new Scanner(corpus)) {
            if (fileScanner.hasNext()) {
                String prevWord = cleanWord(fileScanner.next().toLowerCase());  // Normalize to lowercase
                wordGraph.add(prevWord);
                
                while (fileScanner.hasNext()) {
                    String currWord = cleanWord(fileScanner.next().toLowerCase());  // Normalize to lowercase
                    wordGraph.add(currWord);
                    int existingWeight = wordGraph.set(prevWord, currWord, 1);
                    if (existingWeight > 0) {
                        wordGraph.set(prevWord, currWord, existingWeight + 1);
                    }
                    prevWord = currWord;
                }
            }
        }
    }

    // Helper method to clean punctuation from words
    private String cleanWord(String word) {
        return word.replaceAll("[^a-zA-Z]", "");  // Remove all non-alphabet characters
    }



    /**
     * Creates and returns a copy of the word graph.
     * 
     * @return a deep copy of the word graph
     */
    public Graph<String> getGraph() {
        Graph<String> copy = Graph.empty();
        for (String vertex : wordGraph.vertices()) {
            copy.add(vertex);
            Map<String, Integer> edges = wordGraph.targets(vertex);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                copy.set(vertex, edge.getKey(), edge.getValue());
            }
        }
        return copy;
    }

    /**
     * Finds a potential bridge word between two given words.
     * 
     * @param word1 the first word
     * @param word2 the second word
     * @return the bridge word if found, or an empty string otherwise
     */
    private String findBridgeWord(String word1, String word2) {
        Set<String> candidates = wordGraph.targets(word1).keySet();
        int maxWeight = 0;
        String bridge = "";
        
        for (String candidate : candidates) {
            // Check if the candidate can link word1 and word2
            if (wordGraph.targets(candidate).containsKey(word2)) {
                int weight = wordGraph.targets(word1).get(candidate) +
                             wordGraph.targets(candidate).get(word2);
                if (weight > maxWeight) {
                    maxWeight = weight;
                    bridge = candidate;
                }
            }
        }
        return bridge;  // return an empty string if no bridge found
    }

    /**
     * Generates a poem based on the given input text.
     * 
     * @param input the input text
     * @return a new poem with inserted bridge words
     */
    public String poem(String input) {
        List<String> poemWords = new ArrayList<>();
        try (Scanner inputScanner = new Scanner(input)) {
            if (inputScanner.hasNext()) {
                String prevWord = inputScanner.next();
                poemWords.add(prevWord);
                
                while (inputScanner.hasNext()) {
                    String currWord = inputScanner.next();
                    String bridge = findBridgeWord(prevWord, currWord);
                    if (!bridge.isEmpty()) {
                        poemWords.add(bridge);
                    }
                    poemWords.add(currWord);
                    prevWord = currWord;
                }
            }
        }
        return String.join(" ", poemWords);
    }

    /**
     * @return a string representation of the word graph
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String vertex : wordGraph.vertices()) {
            Map<String, Integer> edges = wordGraph.targets(vertex);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                sb.append("(")
                  .append(vertex)
                  .append(" -> ")
                  .append(edge.getKey())
                  .append(", ")
                  .append(edge.getValue())
                  .append(")\n");
            }
        }
        return sb.toString().trim(); // Remove trailing newline
    }


}
