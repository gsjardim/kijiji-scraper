package logic;

import common.TomcatStartUp;
import static org.junit.jupiter.api.Assertions.*;

import entity.Account;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static logic.AccountLogic.DISPLAY_NAME;
import static logic.AccountLogic.ID;
import static logic.AccountLogic.PASSWORD;
import static logic.AccountLogic.USER;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Shariar
 */
class AccountLogicTest {

   
    private AccountLogic logic;
    private Map<String, String[]> sampleMap;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        /*System.out.println(new File("src\\main\\webapp\\").getAbsolutePath());
        tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(8080);
        Context context = tomcat.addWebapp("/WebScraper", new File("src\\main\\webapp").getAbsolutePath());
        context.addApplicationListener("dal.EMFactory");
        tomcat.init();
        tomcat.start();*/
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {
        logic = new AccountLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        sampleMap = new HashMap<>();

        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        sampleMap.put(AccountLogic.DISPLAY_NAME, new String[]{"Junit 5 Test"});
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(AccountLogic.USER, new String[]{"junit"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(AccountLogic.PASSWORD, new String[]{"junit5"});
        /*This account has Display Name: "Junit 5 Test", User: "junit", and Password: "junit5"*/
    }

    @AfterEach
    final void tearDown() throws Exception {

    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Account> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        
        
        String auxName = "n";
        while(checkDuplicatedAccount(testAccount)){
            testAccount.setDisplayName(testAccount.getDisplayName()+auxName);
            auxName+="n";
        }
        
        logic.add(testAccount);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testAccount);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }

    @Test
    final void testGetWithId() {
        
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        
        //make sure test account is not in there already, if it is change it up until its unique 
        String auxName = "n";
        while(checkDuplicatedAccount(testAccount)){
            testAccount.setDisplayName(testAccount.getDisplayName()+auxName);
            auxName+="n";
        }
        
        logic.add(testAccount);
       //get all accounts
        List<Account> list = logic.getAll();

        //use the first account in the list as test Account
        Account sampleAccount = list.get(list.size()-1);
        //using the id of test account get another account from logic
        Account returnedAccount = logic.getWithId(sampleAccount.getId());

        //the two accounts (testAcounts and returnedAccounts) must be the same
        //assert all field to guarantee they are the same
        assertEquals(sampleAccount.getId(), returnedAccount.getId());
        assertEquals(sampleAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(sampleAccount.getUser(), returnedAccount.getUser());
        assertEquals(sampleAccount.getPassword(), returnedAccount.getPassword());
        logic.delete(testAccount);
        
    }

    @Test
    final void testGetWithUser() {
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);
        Account returnedAc = logic.getAccountWithUser(testAccount.getUser());
        
        assertEquals(testAccount.getUser(), returnedAc.getUser());
        assertEquals(testAccount.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAccount.getPassword(), returnedAc.getPassword());
        logic.delete(testAccount);

    }

    @Test
    final void testGetWithDisplayName() {
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);
        Account returnedAc = logic.getAccountWithDisplayName(testAccount.getDisplayName());
        
        assertEquals(testAccount.getUser(), returnedAc.getUser());
        assertEquals(testAccount.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAccount.getPassword(), returnedAc.getPassword());
        
        logic.delete(testAccount);
    }

    @Test
    final void testGetWithPassword() {
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);

        List<Account> returnedAc = logic.getAccountsWithPassword(testAccount.getPassword());
        for (Account ac : returnedAc) {
            assertEquals(testAccount.getPassword(), ac.getPassword());
        }
        logic.delete(testAccount);
    }

    @Test
    final void testGetAccountWith() {
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);
//        List<Account> accounts = logic.getAll();
//        Account sampleAc = accounts.get(1);
        Account returnedAc = logic.getAccountWith(testAccount.getUser(), testAccount.getPassword());
        
        assertEquals(testAccount.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAc.getUser());
        assertEquals(testAccount.getPassword(), returnedAc.getPassword());
        
        logic.delete(testAccount);

    }

    @Test
    final void testSearch() {
        //searching the database for the value associated with the Key = USER from AccountLogic
        List<Account> accounts = logic.search("82");
        for (Account a : accounts) {
            assertTrue(a.getUser().contains("82") || a.getDisplayName().contains("82"));
        }
    }

    @Test
    final void testCreateEntity() {
        //Create first sample HashMap
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put(AccountLogic.ID, new String[]{"100"});
        testMap.put(AccountLogic.DISPLAY_NAME, new String[]{"Entity test"});
        testMap.put(AccountLogic.USER, new String[]{"Test_user"});
        testMap.put(AccountLogic.PASSWORD, new String[]{"test1"});
        Account testAC = logic.createEntity(testMap);
        
        //Create second sample HashMap
        Map<String, String[]> testMap2 = new HashMap<>();
        testMap2.put(AccountLogic.ID, new String[]{"101"});
        testMap2.put(AccountLogic.DISPLAY_NAME, new String[]{"Entity2 test"});
        testMap2.put(AccountLogic.USER, new String[]{"Test_user2"});
        testMap2.put(AccountLogic.PASSWORD, new String[]{"test2"});
        Account test2AC = logic.createEntity(testMap2);
        
        //Test that the two accounts are different
        assertFalse(testAC.equals(test2AC));
        //Test that the map's fields and the account's fields are equal
        assertEquals(testAC.getId(),(Integer.parseInt(testMap.get(AccountLogic.ID)[0])));
        assertEquals(testAC.getUser(),testMap.get(AccountLogic.USER)[0]);
        assertEquals(testAC.getDisplayName(),testMap.get(AccountLogic.DISPLAY_NAME)[0]);
        assertEquals(testAC.getPassword(), testMap.get(AccountLogic.PASSWORD)[0]);
        
    }
    
    

    @Test
    final void testGetColumnNames() {
        List<String> list1 = logic.getColumnNames();
        List<String> list2 = Arrays.asList("ID", "Display Name", "User", "Password");
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list1 = logic.getColumnCodes();
        List<String> list2 = Arrays.asList(logic.ID,logic.DISPLAY_NAME, logic.USER, logic.PASSWORD);
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testExtractDataAsList() {
        //Add an Id to sample Map and create an Entity with it.
        sampleMap.put(AccountLogic.ID, new String[]{"50"});
        Account testAc = logic.createEntity(sampleMap);
        //Use the extractDataAsList method to create a List of "unknown" types
        List<?> list = logic.extractDataAsList(testAc);
        //compare that they both have the same data
        assertEquals(testAc.getId(), list.get(0));
        assertEquals(testAc.getDisplayName(), list.get(1));
        assertEquals(testAc.getUser(), list.get(2));
        assertEquals(testAc.getPassword(), list.get(3));
    }
    
    @Test
    final void testNullPointerException(){
        //Error check 2: Test for NullPointerException.
        
        Map<String, String[]> nullMap = null;
        assertThrows(NullPointerException.class,()-> logic.createEntity(nullMap));
        
        
    }
    
    @Test
    final boolean checkDuplicatedAccount(Account ac){
        Account returnAccount = logic.getAccountWithDisplayName(ac.getDisplayName());
        
        return (returnAccount!=null);
     }
    
    @Test
    final void TestDuplicatedAccount (){
        
        List<Account> list = logic.getAll();
        Account duplAccount = logic.createEntity(sampleMap);
        
        if(list.size()>0){
            Account testAccount = list.get(0);
           
            duplAccount.setDisplayName(testAccount.getDisplayName());
            duplAccount.setUser(testAccount.getUser());
            duplAccount.setPassword(testAccount.getPassword());
        }
        assertThrows(javax.persistence.PersistenceException.class,()-> logic.add(duplAccount));
       
        
        
    }
}
