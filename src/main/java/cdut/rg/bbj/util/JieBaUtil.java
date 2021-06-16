package cdut.rg.bbj.util;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.List;

public class JieBaUtil {

    public static List<String> getStringList(String text) throws Exception{
        //独立Lucene实现
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> stringList = segmenter.sentenceProcess(text);
        if (stringList == null) {
            return null;
        }
//        StringBuilder result = new StringBuilder();
//        boolean flag = false;
//        for (String string : stringList) {
//            if (string.length() == 1) continue;
//            if (flag) {
//                result.append(" ");
//            } else {
//                flag = true;
//            }
//            result.append(string);
//        }
        return stringList;
    }

}
