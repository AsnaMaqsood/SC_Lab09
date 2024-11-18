package poet;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import graph.Graph;

import org.junit.Test;

/**
 * Unit tests for the GraphPoet class.
 */
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
            File corpusFile = new File("test/poet/multipleWords.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            Graph<String> graphData = graphPoet.getGraph();

            assertEquals("expected three vertices in the graph",  // Correct the expected number of vertices
                    Set.of("welcome", "traveler", "explorer"), graphData.vertices());
            
            assertEquals("expected edges from 'welcome'",
                    Map.of("traveler", 1), graphData.targets("welcome"));  // Edge from "welcome" to "traveler"
            
            assertEquals("expected no edges from 'explorer'",
                    Collections.emptyMap(), graphData.targets("explorer"));  // No edges from "explorer"
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
            String inputText = "Bold";
            assertEquals("expected output identical to input",
                    inputText, graphPoet.poem(inputText));
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

    @Test
    public void testGeneratePoemWithMultipleWords() {
        try {
            File corpusFile = new File("test/poet/bold_exploration.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            String inputText = "To venture boldly where dreams take us.";
            assertEquals("expected poem with additional words",
                    "To boldly venture boldly where bold dreams take us.", graphPoet.poem(inputText));
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }

    @Test
    public void testGraphPoetToStringOutput() {
        try {
            File corpusFile = new File("test/poet/multipleWords.txt");
            GraphPoet graphPoet = new GraphPoet(corpusFile);
            assertEquals("expected string representation of graph",
                    "(welcome, -> traveler!, 1)\n(welcome, -> explorer., 1)", graphPoet.toString());
        } catch (IOException e) {
            assert false; // this should not occur
        }
    }
}
