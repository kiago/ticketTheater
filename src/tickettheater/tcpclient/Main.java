/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tickettheater.tcpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 3306);
        //Socket socket = new Socket("127.0.0.1", 4444);
        //to get the ip address
        System.out.println((java.net.InetAddress.getLocalHost()).toString());

        //true: it will flush the output buffer
        PrintWriter outSocket = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       // Thread.sleep(1000);


        System.out.println("Devinez le numero entre 0 et 5 :");
        boolean repeat = true;
        while (repeat){
            Scanner sc = new Scanner(System.in);
            int resp = sc.nextInt();
            outSocket.println(resp);
            String result = inSocket.readLine();
            System.out.println(result);
            if (result.equals("Bravo !")){
                repeat = false;
            }
        }
    }
}

