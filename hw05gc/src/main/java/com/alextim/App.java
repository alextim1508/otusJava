package com.alextim;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static List<String> list = new ArrayList<>();

    @SuppressWarnings({"InfiniteLoopStatement"})
    public static void startApp()  {
        while (true) {
            for (int i = 0; i < 10_000; i++) {
                list.add(String.valueOf(i));
            }
            for (int i = 0; i < 1000; i++) {
                list.remove(0);
            }
        }
    }

    public static int getSizeList() {
        return list.size();
    }
}

