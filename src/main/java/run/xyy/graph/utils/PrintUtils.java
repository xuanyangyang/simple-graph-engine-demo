package run.xyy.graph.utils;

import java.time.LocalDateTime;

public class PrintUtils {

    public static void print(String msg) {
        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " " + msg);
    }
}
