package com;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<String> list = FileUtils.readLines(new File(Test.class.getClassLoader().getResource("stockactive.txt").toURI()), Charset.forName("utf-8"));
        List<Map<String,String>> ll = new ArrayList<>();
        list.forEach(e ->{
            String[] split = e.split(",");
            Map<String,String> map= Maps.newHashMap();
            map.put("code",split[0]);
            map.put("name",split[1]);
            map.put("articls",split[2]);
            map.put("redcount",split[3]);
            ll.add(map);
        });

        ll.sort((e1,e2)->e1.get("articls").compareTo(e2.get("articls")));
        ll.forEach(e -> System.out.println(e));
    }
}
