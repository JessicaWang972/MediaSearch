package com.kramphub.recruitment.mediasearch.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient Util Class
 *   Sending Get Request
 */
public class HttpClientUtil {
    public static List<String> doGet(String url, Map<String, String> param) {

        String result = null;
        List<String> resultList = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        try {

            RequestConfig requestConfig = RequestConfig.custom()
                    // Set the connection timeout (in milliseconds)
                    .setConnectTimeout(5000)
                    // Set the request timeout (in milliseconds)
                    .setConnectionRequestTimeout(5000)
                    // Socket read and write timeout (in milliseconds)
                    .setSocketTimeout(5000)
                    // Set whether to allow redirection (default is true)
                    .setRedirectsEnabled(true).build();
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("Content-Type", "application/json;charset=utf8");
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }

            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        resultList.add(result);
        resultList.add(String.valueOf(response.getStatusLine().getStatusCode()));
        return resultList;
    }

    public static List<String> doGet(String url) {
        return doGet(url, null);
    }
}
