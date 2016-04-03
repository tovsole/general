package raspis;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;

/**
 * Created by otovstiuk on 22.03.2016.
 */
public class ParserEmail {





    // trusting all certificate
    public void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }




        public void getPage() throws Exception {
            String request = "https://www.google.com/#q=" + "lenovo" + "&num=20";
            System.out.println("Sending request..." + request);



            WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
            System.out.println("new WebClient(BrowserVersion.FIREFOX_3_6);...");


           // try {  doTrustToCertificates();    }
            //catch (Exception e) {         e.printStackTrace();      }
            //System.out.println("doTrustToCertificates...");

            webClient.getOptions().setUseInsecureSSL(true);
            HtmlPage page = webClient.getPage(request);

            System.out.println("webClient.getPage");


            //List results = page.getByXPath("//ol[@id='rso']/li//span/h3[@class='r']");

            //System.out.println(results.size());

          //  for (String ii: results){
           //   System.out.println(ii);
           // }

            page.save(new File("goog1.html"));


            //url = "http://www.google.com"
            //page = webclient.getPage(url)
            //query_input = page.getByXPath("//input[@name='q']")[0]
            //query_input.text = q
            //search_button = page.getByXPath("//input[@name='btnG']")[0]
            //page = search_button.click()

            //-------------
            // results = page.getByXPath("//ol[@id='rso']/li//span/h3[@class='r']")

            //c = 0
            //for result in results:
            //title = result.asText()
            //href = result.getByXPath("./a")[0].getAttributes().getNamedItem("href").nodeValue
            //print title, href
            //c += 1

            ///print c,"Results"

            //if __name__ == '__main__':
            //query("google web search api")

        }


    private Set<String> getDataFromGoogle(String query) {

        Set<String> result = new HashSet<String>();
        String request = "https://www.google.com/#q=" + query + "&num=20";
        System.out.println("Sending request..." + request);




        //Element table = doc.getElementById("cpn-timetable");
        //Element tbody = table.select("tbody").first();
        //ArrayList<Element> rows = tbody.getElementsByTag("tr");

        try {
            try {
                doTrustToCertificates();//
            }
            catch (Exception e) {
                    e.printStackTrace();
            }

            //Document doc1 = Jsoup.connect(request).get();

            // need http protocol, set this as a Google bot agent (smiley)
            Document doc = Jsoup
                    .connect(request)
                    .userAgent( "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();
            System.out.println("Connected");
            // get all links
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                String temp = link.attr("href");
                //if(temp.startsWith("/url?q=")){
                    //use regex to get domain name
                  //  result.add(getDomainName(temp));
               // }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private static URL encodeGoogleQuery(final String[] args) {
        try {
            final StringBuilder localAddress = new StringBuilder();
            localAddress.append("/search?q=");

            for (int i = 0; i < args.length; i++) {
                final String encoding = URLEncoder.encode(args[i], "UTF-8");
                localAddress.append(encoding);
                if (i + 1 < args.length)
                    localAddress.append("+");
            }

            return new URL("http", "www.google.com", localAddress.toString());

        } catch (final IOException e) {
            // Errors should not occur under normal circumstances.
            throw new RuntimeException(
                    "An error occurred while encoding the query arguments.");
        }
    }



}
