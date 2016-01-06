/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.util.NumberDoesntExistException;
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
public class BankTest {
    private Bank bank;
    private IRekening r1;
    private IRekening r2;
    
    private String EURO = "\u20AC";
    
    public BankTest() {
    }
    @Before
    public void setUp() {
        bank = new Bank("ING");
        r1 = bank.getRekening(bank.openRekening("ds", "Ds"));
        r2 = bank.getRekening(bank.openRekening("aa", "bb"));
    }
    
    @After
    public void tearDown() {
        
    }

    /**
     * Test correct usage of openRekening method, of class Bank.
     */
    @Test
    public void testOpenRekeningMetCorrectGegevens() {
        String name = "joep";
        String city = "Eindhoven";
        Bank instance = bank;
        int expResult = 100000002;
        int result = instance.openRekening(name, city);
        assertEquals(expResult, result);
    }
    
     /**
     * Test incorrect usage of openRekening method, of class Bank.
     */
    @Test
    public void testOpenRekeningMetLegeInvoerVelden() {
        String name = "";
        String city = "";
        Bank instance = bank;
        int expResult = -1;
        int result = instance.openRekening(name, city);
        assertEquals(expResult, result);
    }
    
     /**
     * Test incorrect usage of name field, of openRekening method, of class Bank.
     */
    @Test
    public void testOpenRekeningMetLegeNameInvoerVeld() {
        String name = "";
        String city = "Eindhoven";
        Bank instance = bank;
        int expResult = -1;
        int result = instance.openRekening(name, city);
        assertEquals(expResult, result);
    }
    
     /**
     * Test incorrect usage of name field, of openRekening method, of class Bank.
     */
    @Test
    public void testOpenRekeningMetLegeCityInvoerVeld() {
        String name = "Joep";
        String city = "";
        Bank instance = bank;
        int expResult = -1;
        int result = instance.openRekening(name, city);
        assertEquals(expResult, result);
    }
       
    /**
     * Test correct usage of getRekening method, of class Bank.
     * Doesn't use a IRekening class as expectedResult because
     * there is no way without using the getRekening method to get the
     * created Rekening.
     */
    @Test
    public void testGetRekeningMetBestaandeRekening() {
        Bank instance = bank;
        int rekeningNr = bank.openRekening("test", "test");
        IRekening result = instance.getRekening(rekeningNr);
        assertNotNull(result);
    }

    /**
     * Test incorrect usage of getRekening method, of class Bank.
     */
    @Test
    public void testGetRekeningMetOnbekendeRekening() {
        Bank instance = bank;
        bank.openRekening("test", "test");
        IRekening result = instance.getRekening(1);
        assertNull(result);
    }

    /**
     * Test of getName method, of class Bank.
     */
    @Test
    public void testGetName() {
        Bank instance = bank;
        String expResult = "ING";
        String result = instance.getName();
        assertEquals(expResult, result);
 
    }
    
     /**
     * Test of maakOver method, of class Bank.
     * @throws java.lang.Exception
     */
    @Test
    public void testMaakOverCorrecteGegevens() throws Exception {
        Bank instance = bank;
        Rekening re1 = (Rekening) r1;
        re1.muteer(new Money(1000, EURO));
        
        int source = r1.getNr();
        int destination = r2.getNr();
        Money money = new Money(500, EURO);
        
        boolean expResult = true;
        boolean result = instance.maakOver(source, destination, money);
        assertEquals(expResult, result);
        assertEquals(500,r1.getSaldo().getCents());
        assertEquals(500,r2.getSaldo().getCents());
    }
    
    
     /**
     * Test of maakOver method, of class Bank.
     * @throws fontys.util.NumberDoesntExistException
     */
    @Test(expected = RuntimeException.class)
    public void testMaakOverBestemmingGelijkAanBron() throws NumberDoesntExistException {
        Bank instance = bank;
        
        int source = r1.getNr();
        Money money = new Money(500, EURO);
        
        instance.maakOver(source, source, money);
    }
    
    
     /**
     * Test of maakOver method, of class Bank.
     * @throws fontys.util.NumberDoesntExistException
     */
    @Test(expected = RuntimeException.class)
    public void testMaakOverBedragKleinerDanNul() throws NumberDoesntExistException {
        Bank instance = bank;
        Rekening re1 = (Rekening) r1;
        re1.muteer(new Money(1000, EURO));
        
        int source = r1.getNr();
        int destination = r2.getNr();
        Money money = new Money(-500, EURO);
        
        instance.maakOver(source, destination, money);
    }
    
     /**
     * Test of maakOver method, of class Bank.
     * @throws fontys.util.NumberDoesntExistException
     */
    @Test
    public void testMaakOverSourceTeWeinigGeld() throws NumberDoesntExistException {
        Bank instance = bank;
        Rekening re1 = (Rekening) r1;
        re1.muteer(new Money(100, EURO));
        
        int source = r1.getNr();
        int destination = r2.getNr();
        Money money = new Money(200000, EURO);
        
        boolean result = instance.maakOver(source, destination, money);
        assertFalse("has to be false because source has invalid funds", result);
    }   
    
     /**
     * Test of maakOver method, of class Bank.
     * @throws fontys.util.NumberDoesntExistException
     */
    @Test(expected = NumberDoesntExistException.class)
    public void testMaakOverInvalideSourceRekeningNummer() throws NumberDoesntExistException {
        Bank instance = bank;
        Rekening re1 = (Rekening) r1;
        re1.muteer(new Money(100, EURO));
        
        int source = r1.getNr()-1000;
        int destination = r2.getNr();
        Money money = new Money(50, EURO);
        
        instance.maakOver(source, destination, money);
    } 
    
    
     /**
     * Test of maakOver method, of class Bank.
     * @throws fontys.util.NumberDoesntExistException
     */
    @Test(expected = NumberDoesntExistException.class)
    public void testMaakOverInvalideDestinationRekeningNummer() throws NumberDoesntExistException {
        Bank instance = bank;
        Rekening re1 = (Rekening) r1;
        re1.muteer(new Money(100, EURO));
        
        int source = r1.getNr();
        int destination = r2.getNr()-1212;
        Money money = new Money(50, EURO);
        
        instance.maakOver(source, destination, money);
    }   
    
}
