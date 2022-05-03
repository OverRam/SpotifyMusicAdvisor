package advisor.Model;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerHttp {

    static final Integer PORT_URL = 8081;

    static HttpServer createServer() {
        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT_URL), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpServer;
    }

    static void sleepThread() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
