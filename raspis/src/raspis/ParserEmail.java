package raspis;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.io.IOException;
import java.net.URLEncoder;
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


    private final String DOMAIN_NAME_PATTERN  = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    private Pattern patternDomainName=Pattern.compile(DOMAIN_NAME_PATTERN);
    private Matcher matcher;

    public void parse(String query) {

        Set<String> result = getDataFromGoogle(query);
        for(String link : result){
            System.out.println(link);
        }
        System.out.println(result.size());
    }

    public String getDomainName(String url){

        String domainName = "";
        matcher = patternDomainName.matcher(url);
        if (matcher.find()) {
            domainName = matcher.group(0).toLowerCase().trim();
        }
        return domainName;

    }

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
                if(temp.startsWith("/url?q=")){
                    //use regex to get domain name
                    result.add(getDomainName(temp));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Encodes a string of arguments as a URL for a Google search query.
     *
     * @param args
     *            The array of arguments to pass to Google's search engine.
     *
     * @return A URL for a Google search query based on the arguments.
     */
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

    public void parse2(String query) throws Exception {

        for (int ii =1; ii<=7; ii++) {
            URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" + URLEncoder.encode(query, "UTF-8") + "&rsz=8&start="+(ii*8));

            Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
            GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

            int total = results.getResponseData().getResults().size();
            System.out.println("total: " + total);

            // Show title and URL of each results
            for (int i = 0; i <= total - 1; i++) {
                System.out.println("Title: " + results.getResponseData().getResults().get(i).getTitle());
                System.out.println("URL: " + results.getResponseData().getResults().get(i).getUrl() + "\n");

            }
        }
    }

    class GoogleResults{

        private ResponseData responseData;
        public ResponseData getResponseData() { return responseData; }
        public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
        public String toString() { return "ResponseData[" + responseData + "]"; }

        class ResponseData {
            private List<Result> results;
            public List<Result> getResults() { return results; }
            public void setResults(List<Result> results) { this.results = results; }
            public String toString() { return "Results[" + results + "]"; }
        }

        class Result {
            private String url;
            private String title;
            public String getUrl() { return url; }
            public String getTitle() { return title; }
            public void setUrl(String url) { this.url = url; }
            public void setTitle(String title) { this.title = title; }
            public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
        }
    }
}
