package org.forgerock.http.servlet.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URL;

public class BlockChainUtils {
    HttpPost http_post; // we'll use ReST to interface with a Smart Contract already deployed in Kaleido.io
    private static String server = "https://u1mrfvlepf-u1h4uikgb5-connect.us1-azure.kaleido.io";
    private static String key = "dTFxOGRkajhhYjpXSnY1WUxOWFBJSzFhVGxvalVUa3FaWi1CendxUFNFZWZvbHRGVDZOeGhj"; // used for Basic Auth
    private static String from = "0x4FB35D7BA91023452D2B9F288d1496DEF3b43eF6"; // consortium user is published at this address
    private static String to = "0x6b64418daa953801cac7a58c294ef4ac602b010a"; // smart contract is published at this address

    public String postValue(String value) {
        String result = "";
        http_post = new HttpPost(server);
        try {
            http_post.setHeader("Authorization", "Basic " + this.key);
            StringEntity body = new StringEntity("headers:\n  type: SendTransaction\nfrom: " + from + "\nto: " + to + "\nparams:\n  - value: " + value + "\n    type: string\ngas: 1000000\nmethodName: set");
            http_post.setEntity(body);

            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(http_post);
            HttpEntity entity = response.getEntity();
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                String entity_str = EntityUtils.toString(responseEntity);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && entity_str.contains("true")) { //&& entity_str.contains("compliantStatus")
                    log("hash id: " + entity_str);
                    result = (entity_str);
                } else if (entity_str.contains("not found")) { // do not check httpStatus since srv can throw diff codes n this situation
                    result = "unknown";
                }
            } else {
                result = "connection error";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        log("stored on-chain the access timestamp? " + value + " @ " + result);
        return result;
    }



//    private void setHeaders() {
//        http_post.setHeader("Accept", "*/*");
//        http_post.setHeader("Host", this.server);
//        http_post.setHeader("Authorization", "Basic " + this.key);
//        http_post.setHeader("Accept-Encoding", "gzip, deflate");
//        http_post.setHeader("Cache-Control", "no-cache");
//        http_post.setHeader("Content-Type", "text/plain");
//        this.http.setHeader("Connection", "keep-alive");
//
//    }

    public static void log(String str) {
        System.out.println("+++   " + str);
        //debug.error("+++      " + str); // should be 'message' instead?
    }
}
