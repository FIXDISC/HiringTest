import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;

public class Echo {

    static Logger log;
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);

        System.out.println("Enter URL ");
        String url = in.nextLine();
        System.out.println("Enter message to be echoed ");
        String msg = in.nextLine();

        in.close();
        if(isValidUrl(url) && isValidMethod(url)){
            
            connect(url, msg);
        }
        
    }


    static String connect(String url,String msg) throws Exception{
        log = Logger.getLogger("com.variacode.echo");
        log.setLevel(Level.ALL);
        
        var postRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(msg))
                .build();
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        var client = HttpClient.newBuilder().executor(executor).build();
        var responseFuture = client.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

        //System.out.println("M:   "+responseFuture);

        responseFuture.thenApply(res -> {
            log.log(Level.INFO, "StatusCode: {0}", res.statusCode());        
            log.info(res.body());
            return res;
        })
                .thenApply(HttpResponse::body)
                .join();
                
        
        log.info("DONE");
        executor.shutdownNow();
        return "DONE";
        
    }
    
    static boolean isValidUrl(String url){
        try {
            URL obj = new URL(url);
            obj.toURI();
            return true;
        } catch (MalformedURLException e) {
            System.out.println( "ERROR: Malformed URL" );
            return false;
        } catch (URISyntaxException e) {
            System.out.println( "ERROR: Malformed URL" );
            return false;
        }
    }
    
    static boolean isValidMethod(String url){
        try {
            String[] method = url.split("/");
            if("post".equals(method[method.length-1].toLowerCase())){
                return true;
            } else {
                System.out.println( "ERROR: Wrong method. Only POST method are available" );
                return false;
            }
        } catch (Exception e) {
            System.out.println( "ERROR: Wrong method. Only POST method are available" );
            return false;
        }
    }
}
