package run.xyy.graph.utils;

import java.time.LocalDateTime;

/**
 * 打印工具
 *
 * @author xuanyangyang
 */
public class PrintUtils {

    public static void print(String msg) {
        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " " + msg);
    }
}
