package org.russellrc.playground;

import java.util.List;
import java.util.stream.Stream;

public class Dummy {

    public static void main(String[] args) throws Exception {
        String regex1 = "v[0-9]+(\\.[0-9]+)+";
        String s1 = "v1.36.2";
        System.out.println(s1.matches(regex1));

        String regex2 = ".*\\[publish\\].*";
        String s2 = "trying to force publish 1.36 artifacts publish";
        System.out.println(s2.matches(regex2));

        List.of().
    }
}
