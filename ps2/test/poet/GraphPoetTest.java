package poet;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import graph.Graph;

import org.junit.Test;
public class GraphPoetTest {

    @Test(expected = AssertionError.class)
    public void checkAssertionsEnabled() {
        assert false; // ensure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testCorpusNotFound() {
        try {
            File corpusFile = new File("test/poet/missing_corpus.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            assert false; // this line should not execute
        } catch (IOException e) {
            assert true; // exception expected
        }
    }

    @Test
    public void testEmptyCorpus() {
        try {
            File corpusFile = new File("test/poet/blank_corpus.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            Graph<String> graphData = graphPoet.getGraph();
            assertEquals("expected an empty graph",
                    Collections.emptySet(), graphData.vertices());
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

    @Test
    public void testSingleWordCorpus() {
        try {
            File corpusFile = new File("test/poet/singleWord.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            Graph<String> graphData = graphPoet.getGraph();
            assertEquals("expected one vertex in the graph",
                    Set.of("greetings"), graphData.vertices());
            assertEquals("expected no outgoing edges",
                    Collections.emptyMap(), graphData.targets("greetings"));
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

    @Test
    public void testMultipleWordsCorpus() {
        try {
            // Ensure the file path is correct for your setup
            File corpusFile = new File("test/poet/multipleWords.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            Graph<String> graphData = graphPoet.getGraphForTesting();

            // Verify the correct words are in the graph
            assertEquals("expected three vertices in the graph",  // Adjust the words if needed
                    Set.of("welcome", "traveler", "explorer"), graphData.vertices());

            // Check the edges from the "welcome" word
            assertEquals("expected edges from 'welcome'",
                    Map.of("traveler", 1), graphData.targets("welcome"));

            // Check that "explorer" has no outgoing edges
            assertEquals("expected no outgoing edges from 'explorer'",
                    Collections.emptyMap(), graphData.targets("explorer"));

        } catch (IOException e) {
            assert false; // this should not occur
        }
    }


    @Test
    public void testGeneratePoemWithEmptyInput() {
        try {
            File corpusFile = new File("test/poet/bold_exploration.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            String inputText = "";
            assertEquals("expected empty output",
                    inputText, graphPoet.poem(inputText));
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

    @Test
    public void testGeneratePoemWithSingleWord() {
        try {
            File corpusFile = new File("test/poet/bold_exploration.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            String inputText = "hello";
            assertEquals("expected poem with a single word",
                    "hello", graphPoet.poem(inputText));
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

    @Test
    public void testGeneratePoemWithMultipleWords() {
        try {
            // Ensure the file path is correct for your setup
            File corpusFile = new File("test/poet/bold_exploration.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            String inputText = "hello traveler world";

            // Ensure the expected output aligns with the graph's connections
            assertEquals("expected poem with bridge words",
                    "hello traveler world", graphPoet.poem(inputText)); // Adjust the expected output

        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

}
