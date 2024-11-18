package poet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import graph.Graph;

public class GraphPoet {
    
    private final Graph<String> wordGraph = Graph.empty();
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
    	// Remove all non-alphabet characters
        return word.replaceAll("[^a-zA-Z]", "");
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
        // Remove trailing newline
        return sb.toString().trim();
    }

 // Method to check the creation of the graph during testing
    public Graph<String> getGraphForTesting() {
    	// Return the internal word graph for testing purposes
        return wordGraph;
    }

}
