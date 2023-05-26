import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> wordsInPdfs;

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        wordsInPdfs = new HashMap<>();
        Map<String, Integer> freqs = new HashMap<>();

        List<File> listOfPDFFiles = List.of(Objects.requireNonNull(pdfsDir.listFiles()));
        for (File filePdf : listOfPDFFiles) {
            var doc = new PdfDocument(new PdfReader(filePdf));

            for (int i = 1; i <= doc.getNumberOfPages(); i++) {

                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));

                var words = text.split("\\P{IsAlphabetic}+");

                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }
                int count;
                for (var w : freqs.keySet()) {
                    String wordToLowerCase = w.toLowerCase();
                    if (freqs.get(wordToLowerCase) != null) {
                        count = freqs.get(wordToLowerCase);
                        wordsInPdfs.computeIfAbsent(wordToLowerCase, k -> new ArrayList<>()).add(
                                new PageEntry(filePdf.getName(), i, count));
                    }
                }
                freqs.clear();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> resultSearch = new ArrayList<>();
        String wordToLowerCase = word.toLowerCase();
        if (wordsInPdfs.get(wordToLowerCase) != null) {
            for (PageEntry pageEntry : wordsInPdfs.get(wordToLowerCase)) {
                resultSearch.add(pageEntry);
            }
        }
        Collections.sort(resultSearch);
        return resultSearch;
    }
}