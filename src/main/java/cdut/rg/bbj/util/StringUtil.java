package cdut.rg.bbj.util;

public class StringUtil {


    public static float getSimilarityRatio(String str, String target) {
        int d[][]; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0 || m == 0) {  // 空字符串
            return 0;
        }
        d = new int[n + 1][m + 1];  // 产生矩阵

        // 初始化
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        /*
        str start
        0 1 2 3 4 5
        1
        2
        3
         */

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);  // s t r
            // 去匹配target
            for (j = 1; j <= m; j++) {  // s t a r t
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 ) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + temp);
            }
        }

        return (1 - (float) d[n][m] / Math.max(str.length(), target.length())) * 1F;
    }

}
