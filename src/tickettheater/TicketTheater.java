/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tickettheater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tickettheater.tcpserver.Server;

/**
 *
 * @author kiago
 */
public class TicketTheater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {

        Socket socket = null;
        try {
            socket = new Socket("localhost", 4444);
        } catch (IOException ex) {
            Logger.getLogger(TicketTheater.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Socket socket = new Socket("127.0.0.1", 4444);
        //to get the ip address
        System.out.println((java.net.InetAddress.getLocalHost()).toString());

        //true: it will flush the output buffer
        PrintWriter outSocket = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Thread.sleep(1000);

        //System.out.println("Sending Hello to server");
        outSocket.println("Hello");
        //System.out.println("Waiting answer from server");
        int i=0;
        for(i=0;i<3;i++){
        System.out.println(inSocket.readLine());
        }
        System.out.println("End.");


        resaForm ticketing = new resaForm();
        ticketing.setVisible(true);
        //System.out.println(Server.testTitle);
        // TODO code application logic here
    }
}
