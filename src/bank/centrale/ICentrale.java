/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centrale;

import bank.bankieren.Money;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author joep
 */
public interface ICentrale extends Remote {
    
    /**
     * Bank has to call this method to recieve available rekeningNummer
     * @param bankNaam
     * @return the next available rekeningnummer
     */
    public int openRekening(String bankNaam) throws RemoteException;
    
    public boolean maakOver(int destination, Money bedrag) throws RemoteException;
}
