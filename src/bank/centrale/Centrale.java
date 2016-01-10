/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centrale;

import bank.bankieren.IBank;
import bank.bankieren.Money;
import bank.internettoegang.IBalie;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joep
 */
public class Centrale extends UnicastRemoteObject implements ICentrale {

    private int nextRekeningNummer = 100000000;
    private final HashMap<Integer, String> rekeningNummers;

    public Centrale() throws RemoteException {
        rekeningNummers = new HashMap<>();
    }

    @Override
    public synchronized int openRekening(String bankNaam) {
        int newNumber = nextRekeningNummer;
        rekeningNummers.put(newNumber, bankNaam);
        nextRekeningNummer++;
        return newNumber;
    }

    @Override
    public boolean maakOver(int destination, Money bedrag) throws RemoteException {
        String bank = rekeningNummers.get(destination);
        if (bank != null) {
            try {
                return ((IBalie) Naming.lookup(bank)).muteer(destination, bedrag);
            } catch (NotBoundException | MalformedURLException ex) {
                Logger.getLogger(Centrale.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

}
