package cdut.rg.bbj.util;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;

import java.util.List;

public class WordUtil {

    public static String getStringList(String text) {
        List<Word> words = WordSegmenter.seg(text, SegmentationAlgorithm.MinimalWordCount);
        StringBuffer sb = new StringBuffer();
        for (Word word : words) {
            sb.append(word.getText());
            sb.append(" ");
        }
        return sb.toString();
    }

}
