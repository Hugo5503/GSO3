package bank.internettoegang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.beans.PropertyChangeEvent;

public class Bankiersessie extends UnicastRemoteObject implements
        IBankiersessie, IRemotePropertyListener {

    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;

    public Bankiersessie(int reknr, IBank bank) throws RemoteException {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;
        bank.addListener(this, String.valueOf(reknr)); 

    }

    public boolean isGeldig() {
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag)
            throws NumberDoesntExistException, InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        if (reknr == bestemming) {
            throw new RuntimeException(
                    "source and destination must be different");
        }
        if (!bedrag.isPositive()) {
            throw new RuntimeException("amount must be positive");
        }

        return bank.maakOver(reknr, bestemming, bedrag);
    }

    private void updateLaatsteAanroep() throws InvalidSessionException {
        if (!isGeldig()) {
            throw new InvalidSessionException("session has been expired");
        }

        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();
 
        return bank.getRekening(reknr);
    }

    @Override
    public void logUit() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        System.out.println("got this: " + reknr + " " + evt.getNewValue());
    }

}
