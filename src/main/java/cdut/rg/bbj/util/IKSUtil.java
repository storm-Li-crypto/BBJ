package cdut.rg.bbj.util;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;

public class IKSUtil {

    public static String getStringList(String text) throws Exception{
        //独立Lucene实现
        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        while ((lex = ik.next()) != null) {
            if (flag) {
                result.append(" ");
            } else {
                flag = true;
            }
            result.append(lex.getLexemeText());
        }
        return result.toString();
    }

}
