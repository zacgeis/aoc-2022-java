package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils {
    public static <E> List<E> copyOf(List<E> list) {
        List<E> new_list = new ArrayList<>();
        new_list.addAll(list);
        return new_list;
    }

    public static <E> List<E> of(E... es) {
        List<E> new_list = new ArrayList<>();
        new_list.addAll(Arrays.asList(es));
        return new_list;
    }
}

