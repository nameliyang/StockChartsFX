package com.zoicapital.stockchartsfx.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {


    public void doRequets(String url ) throws IOException {
        URL u = new URL(url);
        URLConnection urlConnection = u.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        int read= -1;
        while((read = inputStream.read())!=-1){
            baos.write(read);
        }
        inputStream.close();
    }

}
