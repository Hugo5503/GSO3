/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centrale;

import bank.server.BalieServer;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import bank.server.BalieServer;
import javafx.application.Application;
/**
 *
 * @author joep
 */
public class CentraleServer{

    private static boolean interrupted = false;
    
    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.net.MalformedURLException
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, Exception {
        LocateRegistry.createRegistry(1099);
        ICentrale centrale = new Centrale();
        Naming.rebind("Centrale", centrale);
        System.out.println("Centrale server started!");
        
        Application.launch(BalieServer.class);
    }

    
}
