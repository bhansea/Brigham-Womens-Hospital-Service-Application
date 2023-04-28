package edu.wpi.punchy_pegasi.backend;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class AppSearch {

    public static void initialize() throws Exception {

        // Create an index
        Directory index = createIndex();

        // Define the search criteria
        String[] fields = {"functionName", "functionDescription"};
        Query query = createQuery(fields, "functionName");

        // Search the index
        TopDocs results = searchIndex(index, query);

        // Display the search results
        Map<String, String> searchResults = displayResults(results, index);
    }

    private static Directory createIndex() throws IOException {
        Path indexPath = Paths.get("index");
        Directory index = new MMapDirectory(indexPath);
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Specify the similarity algorithm to use
        Similarity similarity = new BooleanSimilarity();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(similarity);

        IndexWriter writer = new IndexWriter(index, config);

        // Add the functions in your app to the index
        addFunctionToIndex(writer, "functionName1", "functionDescription1");
        addFunctionToIndex(writer, "functionName2", "functionDescription2");
        addFunctionToIndex(writer, "functionName3", "functionDescription3");

        writer.close();
        return index;
    }

    private static void addFunctionToIndex(IndexWriter writer, String functionName, String functionDescription) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("functionName", functionName, Field.Store.YES));
        doc.add(new TextField("functionDescription", functionDescription, Field.Store.YES));
        writer.addDocument(doc);
    }

    private static Query createQuery(String[] fields, String text) throws ParseException {
        Map<String, Float> boost = new HashMap<>();
        boost.put(fields[0], 2.0f);
        boost.put(fields[1], 1.0f);

        QueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer(), boost);
        parser.setAllowLeadingWildcard(true); // allow wildcard at the beginning
        parser.setFuzzyMinSim(4f); // set minimum similarity for fuzzy search

        return parser.parse("*" + text + "*"); // search for all documents containing the text
    }

    private static TopDocs searchIndex(Directory index, Query query) throws IOException {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher.search(query, 10);
    }

    private static Map<String, String> displayResults(TopDocs results, Directory index) throws IOException {
        Map<String, String> searchResults = new HashMap<>();
        IndexReader reader = DirectoryReader.open(index);
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = reader.document(scoreDoc.doc);
            searchResults.put(doc.get("functionName"), doc.get("functionDescription"));
        }
        return searchResults;
    }
}
