/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import fontys.util.InvalidSessionException;
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
public class BalieTest {
    
    private IBank bank;
    private Balie balie;
    
    private String naam;
    private String plaats;
    private String wachtwoord;
    
    private String accountNaam;
    
    public BalieTest() {
    }

    @Before
    public void setUp() throws RemoteException {
        bank = new Bank("ING");
        balie = new Balie(bank);
        naam = "Joep";
        plaats = "Eindhoven";
        wachtwoord = "appelsap";
        
        accountNaam = balie.openRekening(naam, plaats, wachtwoord);
    }

    /**
     * Test of openRekening method, of class Balie.
     */
    @Test
    public void testOpenRekeningCorrect() throws RemoteException {
        String result = balie.openRekening(naam, plaats, wachtwoord);
        int length = result.length();
        
        assertNotNull(result);
        assertEquals(8,length);
    }
    
     /**
     * Test of openRekening method with incorrect passwords, of class Balie.
     */
    @Test
    public void testOpenRekeningIncorrectWachtwoord() throws RemoteException {
        String wachtwoordTeKort = "aa";
        String wachtwoordTeLang = "ditIsEchtEenBelachelijkeRequirement";
      
        String resultKort = balie.openRekening(naam, plaats, wachtwoordTeKort);
        String resultLang = balie.openRekening(naam, plaats, wachtwoordTeLang);
        
        assertNull(resultKort);
        assertNull(resultLang);
    }
    
     /**
     * Test of openRekening method with empty inputs, of class Balie.
     */
    @Test
    public void testOpenRekeningEmptyInput() throws RemoteException {
        String leeg = "";
      
        String wachtwoordLeeg = balie.openRekening(naam, plaats, leeg);
        String naamLeeg = balie.openRekening(leeg, plaats, wachtwoord);
        String plaatsLeeg = balie.openRekening(naam, leeg, wachtwoord);
        
        assertNull(wachtwoordLeeg);
        assertNull(naamLeeg);
        assertNull(plaatsLeeg);
    }

      /**
   * er wordt een sessie opgestart voor het login-account met de naam
   * accountnaam mits het wachtwoord correct is
   * @param accountnaam
   * @param wachtwoord
   * @return de gegenereerde sessie waarbinnen de gebruiker 
   * toegang krijgt tot de bankrekening die hoort bij het betreffende login-
   * account mits accountnaam en wachtwoord matchen, anders null
   */
    
    /**
     * Test of logIn method, of class Balie.
     * @throws java.rmi.RemoteException
     * @throws fontys.util.InvalidSessionException
     */  
    @Test 
    public void testLogInCorrect() throws RemoteException, InvalidSessionException {
        IBankiersessie bankierSessie = balie.logIn(accountNaam, wachtwoord);
        String rekeningHouder = bankierSessie.getRekening().getEigenaar().getNaam();
        assertEquals(naam, rekeningHouder);
        assertNotNull(bankierSessie);
    }
    
    /**
     * Test of logIn method, of class Balie.
     * @throws java.rmi.RemoteException
     */
    @Test 
    public void testLogInOnbekendAccount() throws RemoteException {
        IBankiersessie result = balie.logIn("hoi", wachtwoord);
        assertNull(result);
    }
    
        /**
     * Test of logIn method, of class Balie.
     * @throws java.rmi.RemoteException
     */
    @Test 
    public void testLogInFoutWachtwoord() throws RemoteException {
        IBankiersessie result = balie.logIn(accountNaam, "password");
        assertNull(result);
    }
    
    
}
