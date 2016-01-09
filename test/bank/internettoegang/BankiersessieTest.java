/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joep
 */
public class BankiersessieTest {

    private Bankiersessie bankierSessie;
    private Bank bank;
    private IRekening rekening1;
    private IRekening rekening2;

    @Before
    public void setUp() throws RemoteException, InvalidSessionException {
        bank = new Bank("ING");
        int reknr = bank.openRekening("henk", "Eindhoven");
        bankierSessie = new Bankiersessie(reknr, bank);
        rekening1 = bankierSessie.getRekening();
        int reknr2 = bank.openRekening("sjaak", "fad");
        rekening2 = new Bankiersessie(reknr2, bank).getRekening();
    }

    /**
     * Test of isGeldig method, of class Bankiersessie.
     */
    @Test
    public void testIsGeldig() throws InterruptedException {
        boolean result = bankierSessie.isGeldig();
        assertTrue(result);
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test
    public void testMaakOverCorrect() throws Exception {
        Money euros = new Money(10, "\u20AC");

        boolean result = bankierSessie.maakOver(rekening2.getNr(), euros);
        long rekening1Balance = rekening1.getSaldo().getCents();
        long rekening2Balance = rekening2.getSaldo().getCents();

        assertTrue(result);
        assertEquals(-10, rekening1Balance);
        assertEquals(10, rekening2Balance);
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test(expected = RuntimeException.class)
    public void testMaakOverZelfdeBestemming() throws Exception {
        Money euros = new Money(10, "\u20AC");

        boolean result = bankierSessie.maakOver(rekening1.getNr(), euros);
        long rekening1Balance = rekening1.getSaldo().getCents();
        
        assertFalse(result);
        assertEquals(0, rekening1Balance);
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test(expected = RuntimeException.class)
    public void testMaakOverBedragMinderDanNul() throws Exception {
        Money euros = new Money(-10, "\u20AC");

        boolean result = bankierSessie.maakOver(rekening2.getNr(), euros);
        long rekening1Balance = rekening1.getSaldo().getCents();
        long rekening2Balance = rekening2.getSaldo().getCents();
        
        assertFalse(result);
        assertEquals(0, rekening1Balance);
        assertEquals(0, rekening2Balance);
    }
    
    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test(expected = NumberDoesntExistException.class)
    public void testMaakOverBestemmingOnbekend() throws Exception {
        Money euros = new Money(10, "\u20AC");

        boolean result = bankierSessie.maakOver(2304897, euros);
        long rekening1Balance = rekening1.getSaldo().getCents();

        assertFalse(result);
        assertEquals(0, rekening1Balance);
    }
    
        /**
     * Test of loguit method, of class Bankiersessie.
     */
    @Test
    public void testLoguit() throws RemoteException {
        bankierSessie.logUit();
    }
    
    @Test
    public void testGetRekening() throws InvalidSessionException, RemoteException {
        IRekening rekening = bankierSessie.getRekening();
        assertNotNull(rekening);
        assertEquals(rekening.getEigenaar().getNaam(), "henk");
    }
}
