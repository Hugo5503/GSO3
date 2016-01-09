package bank.bankieren;

import bank.internettoegang.BasicPublisher;
import bank.internettoegang.IRemotePropertyListener;
import bank.internettoegang.IRemotePublisher;
import fontys.util.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Bank extends UnicastRemoteObject implements IBank {

    /**
     *
     */
    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;
    private final BasicPublisher publisher;

    public Bank(String name) throws RemoteException {
        accounts = new HashMap<Integer, IRekeningTbvBank>();
        clients = new ArrayList<IKlant>();
        nieuwReknr = 100000000;
        this.name = name;
        publisher = new BasicPublisher(new String[]{});
    }

    @Override
    public int openRekening(String name, String city) {
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        IKlant klant = getKlant(name, city);
        IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
        accounts.put(nieuwReknr, account);
        publisher.addProperty(String.valueOf(nieuwReknr));
        nieuwReknr++;
        return nieuwReknr - 1;
    }

    private IKlant getKlant(String name, String city) {
        for (IKlant k : clients) {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    @Override
    public IRekening getRekening(int nr) {
        return accounts.get(nr);
    }

    @Override
    public boolean maakOver(int source, int destination, Money money)
            throws NumberDoesntExistException {
        if (source == destination) {
            throw new RuntimeException(
                    "cannot transfer money to your own account");
        }
        if (!money.isPositive()) {
            throw new RuntimeException("money must be positive");
        }

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        if (source_account == null) {
            throw new NumberDoesntExistException("account " + source
                    + " unknown at " + name);
        }

        Money negative = Money.difference(new Money(0, money.getCurrency()),
                money);
        boolean success = source_account.muteer(negative);
        if (!success) {
            return false;
        }

        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
        if (dest_account == null) {
            throw new NumberDoesntExistException("account " + destination
                    + " unknown at " + name);
        }
        success = dest_account.muteer(money);

        if (!success) // rollback
        {
            source_account.muteer(money);
        }
        else
        {
            publisher.inform(this, String.valueOf(source), null, source_account.getSaldo().getValue());
            publisher.inform(this, String.valueOf(destination), null, dest_account.getSaldo().getValue());
        }
        return success;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addListener(IRemotePropertyListener listener, String property) throws RemoteException {
        publisher.addListener(listener, property);
    }

    @Override
    public void removeListener(IRemotePropertyListener listener, String property) throws RemoteException {
        publisher.removeListener(listener, property);
    }

}
