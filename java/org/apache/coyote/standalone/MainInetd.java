package org.apache.coyote.standalone;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.tomcat.util.net.ServerSocketFactory;


/** 
 * Used to run tomcat or coyote on demand or on port 80 ( no JNI code needed, 
 * just (x)inetd, launchd or other simple C wrappers ). This is intended for 
 * simple configurations, with a single endpoint. 
 *  
 * The target for this mode are people running tomcat on their desktop 
 * machines, either as personal web server or as interface to some applications,
 * or as developers. It avoids the need to have tomcat taking memory all the 
 * time - it starts on demand, when you need it, and stops itself when it's
 * idle too long.  
 * 
 */
public class MainInetd extends Main {
    Timer timer=new Timer(true); // daemon thread
    
    public MainInetd() {        
    }

    
    /**
     */
    public void run() {
        init();
        SelectorProvider sp=SelectorProvider.provider();
        
        // check every 5 minutes
        timer.scheduleAtFixedRate( new IdleCheck(), 300000, 300000);
        try {
            Channel ch=sp.inheritedChannel();
            if(ch!=null ) {
                System.err.println("Inherited: " + ch.getClass().getName());
                ServerSocketChannel ssc=(ServerSocketChannel)ch;
                //proto.getEndpoint().setServerSocketFactory(new InetdServerSocketFactory(ssc.socket()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        start();
    }
    
    class IdleCheck extends TimerTask {

        public void run() {
            
        }
        
    }

    // ------------------- Main ---------------------
    public static void main( String args[]) {
        MainInetd sa=new MainInetd();
        sa.run();
    }

    class InetdServerSocketFactory extends ServerSocketFactory {
        ServerSocket inetd;
        
        InetdServerSocketFactory (ServerSocket inetd) {
            /* NOTHING */
        }

        public ServerSocket createSocket (int port)
        throws IOException {
            return inetd;
        }

        public ServerSocket createSocket (int port, int backlog)
        throws IOException {
            return inetd;
        }

        public ServerSocket createSocket (int port, int backlog,
            InetAddress ifAddress)
        throws IOException {
            return inetd;
        }
     
        public Socket acceptSocket(ServerSocket socket)
        throws IOException {
            return socket.accept();
        }
     
        public void handshake(Socket sock)
        throws IOException {
            ; // NOOP
        }
            
            
     }

    
}