package org.iqmanager.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CurrencyConverter {
    public static String convert(String from, String to, long amount) throws IOException {
        HttpResponse response = Request.Post("https://api.currencylayer.com/convert?access_key=34f6489db3f5d2f30ac2d94bac45ad9a&from="+ from+"&to="+ to +"&amount="+ amount)
                    .execute()
                    .returnResponse();
        InputStream inputStream = response.getEntity().getContent();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }

}
