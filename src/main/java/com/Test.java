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

        System.out.println(String.format("%4s","12.011"));
        System.out.println(String.format("%4s","12.0"));
        System.out.println( String.format("%4s","2.0"));
        System.out.println(String.format("%4s","2.01"));
        System.out.println(String.format("%4s","1.01"));


    }
}
