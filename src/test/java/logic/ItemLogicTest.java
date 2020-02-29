package logic;

import common.TomcatStartUp;
import common.ValidationException;
import entity.Category;
import entity.Image;
import static org.junit.jupiter.api.Assertions.*;

import entity.Item;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.IntFunction;
import static logic.ItemLogic.PRICE;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Shariar
 */
class ItemLogicTest {

    private ItemLogic logic;
    private Map<String, String[]> sampleMap;
    private Item testItem;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {
        logic = new ItemLogic();

        sampleMap = new HashMap<>();
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{"kijijiItem"});
        sampleMap.put(ItemLogic.LOCATION, new String[]{"kijijiItem"});
        sampleMap.put(ItemLogic.PRICE, new String[]{"500.40"});
        sampleMap.put(ItemLogic.TITLE, new String[]{"itemTitle"});
        sampleMap.put(ItemLogic.DATE, new String[]{"10/01/20"});
        sampleMap.put(ItemLogic.URL, new String[]{"itemUrl"});
        sampleMap.put(ItemLogic.ID, new String[]{"10"});
        testItem = logic.createEntity(sampleMap);
        setGenericCategory(testItem);
        setGenericImage(testItem);
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (logic.getWithUrl(testItem.getUrl()) != null) {
            logic.delete(testItem);
        }
    }

    @Test
    
    final void testGetAll() {

        List<Item> list = logic.getAll();
        int originalSize = list.size();
        logic.add(testItem);
        list = logic.getAll();
        assertEquals(originalSize + 1, list.size());
        logic.delete(testItem);
        list = logic.getAll();
        assertEquals(originalSize, list.size());
    }

    @Test
    final void testGetWithId() {
        
        logic.add(testItem);
        List<Item> list = logic.getAll();
        Item sampleItem = list.get(list.size() - 1);
        Item returnedItem = logic.getWithId(sampleItem.getId());
        assertEquals(sampleItem.getId(), returnedItem.getId());
        assertEquals(sampleItem.getPrice(), returnedItem.getPrice());
        assertEquals(sampleItem.getTitle(), returnedItem.getTitle());
        assertEquals(sampleItem.getDate(), returnedItem.getDate());
        assertEquals(sampleItem.getLocation(), returnedItem.getLocation());
        assertEquals(sampleItem.getDescription(), returnedItem.getDescription());
        assertEquals(sampleItem.getUrl(), returnedItem.getUrl());
        assertEquals(sampleItem.getCategory(), returnedItem.getCategory());
        assertEquals(sampleItem.getImage(), returnedItem.getImage());


    }

    @Test
    final void testGetWithPrice() {
     
        logic.add(testItem);
        List<Item> items = logic.getWithPrice(testItem.getPrice());
        for (Item item : items) {
            assertEquals(item.getPrice(), testItem.getPrice());
        }


    }

    @Test
    final void testGetWithTitle() {
        
        logic.add(testItem);
        List<Item> items = logic.getWithTitle(testItem.getTitle());

        for (Item item : items) {
            assertEquals(item.getTitle(), testItem.getTitle());
        }

    }

    @Test
    final void testGetWithDate() {
        
        logic.add(testItem);
        List<Item> items = logic.getWithDate(testItem.getDate().toString());

        for (Item item : items) {
            assertEquals(testItem.getDate(), item.getDate());
        }

    }

    @Test
    final void testGetWithLocation() {
        
        logic.add(testItem);
        List<Item> items = logic.getWithLocation(testItem.getLocation());

        for (Item item : items) {
            assertEquals(testItem.getLocation(), item.getLocation());
        }

    }

    @Test
    final void testGetWithDescription() {

        logic.add(testItem);
        List<Item> items = logic.getWithDescription(testItem.getDescription());

        for (Item item : items) {
            assertEquals(testItem.getDescription(), item.getDescription());
        }

    }

    @Test
    final void testCreateEntity() {

        Map<String, String[]> testMap1 = new HashMap<>();
        testMap1.put(ItemLogic.DESCRIPTION, new String[]{"newItem"});
        testMap1.put(ItemLogic.LOCATION, new String[]{"Kanata"});
        testMap1.put(ItemLogic.PRICE, new String[]{"600.40"});
        testMap1.put(ItemLogic.TITLE, new String[]{"itemTitle"});
        testMap1.put(ItemLogic.DATE, new String[]{"10/01/2020"});
        testMap1.put(ItemLogic.URL, new String[]{"itemUrl"});
        testMap1.put(ItemLogic.ID, new String[]{"15"});
        
        Item testItem1 = logic.createEntity(testMap1);
        setGenericImage(testItem1);
        setGenericCategory(testItem1);
        
        assertFalse(testItem1.equals(testItem));

        assertEquals(testItem1.getId(), Integer.parseInt(testMap1.get(ItemLogic.ID)[0]));
        assertEquals(testItem1.getPrice().compareTo(BigDecimal.valueOf(Double.parseDouble(testMap1.get(ItemLogic.PRICE)[0]))), 0);
        assertEquals(testItem1.getTitle(), (testMap1.get(ItemLogic.TITLE)[0]));
        assertEquals(testItem1.getDescription(), testMap1.get(ItemLogic.DESCRIPTION)[0]);
        assertEquals(testItem1.getLocation(), testMap1.get(ItemLogic.LOCATION)[0]);
        assertEquals(testItem1.getUrl(), testMap1.get(ItemLogic.URL)[0]);
        

    }

    private void setGenericImage(Item item){
        if(new ImageLogic().getAll()!=null)
            item.setImage(new ImageLogic().getAll().get(0));
        else{
            Map<String, String[]> sampleImageMap = new HashMap<>();
            sampleImageMap.put(ImageLogic.PATH, new String[]{"pc_images.jpeg"});
            sampleImageMap.put(ImageLogic.NAME, new String[]{"pc_images"});
            sampleImageMap.put(ImageLogic.URL, new String[]{"newimages.com"});
            item.setImage(new ImageLogic().createEntity(sampleImageMap));
        }
    }    
    
    private void setGenericCategory(Item item){
        if(new CategoryLogic().getAll()!=null)
            item.setCategory(new CategoryLogic().getAll().get(0));
        else{
            Map<String, String[]> sampleCategoryMap = new HashMap<>();
            sampleCategoryMap.put(CategoryLogic.TITLE, new String[]{"keyboards"});
            sampleCategoryMap.put(CategoryLogic.URL, new String[]{"keyboards.com"});
            item.setCategory(new CategoryLogic().createEntity(sampleCategoryMap));
        }
    } 
    
    @Test
    final void testGetColumnNames() {
        //"Id", "Url", "Title", "Date", "Description", "Price", "Location", "Category Id", "Image Id"
        List<String> list1 = logic.getColumnNames();
        List<String> list2 = Arrays.asList("Id", "Url", "Title", "Date", "Description", "Price", "Location", "Category Id", "Image Id");
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list1 = logic.getColumnCodes();
        List<String> list2 = Arrays.asList(logic.ID, logic.URL, logic.TITLE, logic.DATE, logic.DESCRIPTION, logic.PRICE, logic.LOCATION, logic.CATEGORY_ID, logic.IMAGE_ID);
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testExtractDataAsList() {

        
        //Use the extractDataAsList method to create a List of "unknown" types
        List<?> list = logic.extractDataAsList(testItem);
        //compare that they both have the same data
        
        //(e.getId(), e.getUrl(), e.getTitle(), e.getDate(), e.getDescription(), e.getPrice(),
        //        e.getLocation(), e.getCategory().getId(), e.getImage().getId());
        assertEquals(testItem.getId(), list.get(0));
        assertEquals(testItem.getUrl(),list.get(1));
        assertEquals(testItem.getTitle(), list.get(2));
        assertEquals(testItem.getDescription(),list.get(4));
        assertEquals(testItem.getPrice().compareTo((BigDecimal)list.get(5)),0 );
        assertEquals(testItem.getLocation(), list.get(6));
        assertEquals(testItem.getCategory().getId(), list.get(7));
        assertEquals(testItem.getImage().getId(), list.get(8));
        
    }

    @Test
    final void testCreateEntityWithNullValues(){
        //ID, URL, TITLE, DATE, DESCRIPTION, PRICE, LOCATION, CATEGORY_ID, IMAGE_ID
        Map<String, String[]> errorMap = sampleMap;
        errorMap.put(ItemLogic.ID, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.ID, new String[]{"5"});
        errorMap.replace(ItemLogic.URL, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.URL, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.TITLE, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.TITLE, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.DATE, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.DATE, new String[]{"05/05/2021"});
        errorMap.replace(ItemLogic.PRICE, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.PRICE, new String[]{"$99.99"});
        errorMap.replace(ItemLogic.LOCATION, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.LOCATION, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.DESCRIPTION, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        
    }
    
    @Test
    final void testCreateEntityWithEmptyOrBadValues (){
        //ID, URL, TITLE, DESCRIPTION, PRICE, 
        Map<String, String[]> errorMap = sampleMap;
        
        errorMap.put(ItemLogic.ID, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.ID, new String[]{"5i"});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.ID, new String[]{"5"});
        errorMap.replace(ItemLogic.URL, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.URL, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.TITLE, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.TITLE, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.DESCRIPTION, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.DESCRIPTION, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.PRICE, new String[]{"USD 60.00"});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.PRICE, new String[]{"$60.00"});
        /*errorMap.replace(ItemLogic.DATE, new String[]{"06-jan-2020"});
        assertThrows(ParseException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.DATE, new String[]{"06-01-2020"});
        assertThrows(ParseException.class,()-> logic.createEntity(errorMap));*/
    }
    
    @Test
    final void testCreateEntityWithValuesTooLong (){
        //ID, URL, TITLE, LOCATION, PRICE
        Map<String, String[]> errorMap = sampleMap;
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };
        
       
        errorMap.replace(ItemLogic.URL, new String[]{generateString.apply(260)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.URL, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.TITLE, new String[]{generateString.apply(260)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.TITLE, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.LOCATION, new String[]{generateString.apply(46)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ItemLogic.LOCATION, new String[]{"catUrl"});
        errorMap.replace(ItemLogic.PRICE, new String[]{generateString.apply(16)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
    }
    
    @Test
    final void testCreateEntityWithEdgeValues(){
        
        Map<String, String[]> testMap = new HashMap<>();
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };
        //ID, URL, TITLE, LOCATION, PRICE
        testMap.put(ItemLogic.ID, new String[]{"1"});
        testMap.put(ItemLogic.URL, new String[]{generateString.apply(1)});
        testMap.put(ItemLogic.TITLE, new String[]{generateString.apply(1)});
        testMap.put(ItemLogic.LOCATION, new String[]{generateString.apply(1)});
        testMap.put(ItemLogic.PRICE, new String[]{"0.00"});
        testMap.put(ItemLogic.DATE, new String[]{"01/01/2100"});
        testMap.put(ItemLogic.DESCRIPTION, new String[]{generateString.apply(1)});
        Item _item = logic.createEntity(testMap);
        
        
        assertEquals(Integer.parseInt(testMap.get(ItemLogic.ID)[0]),_item.getId());
        assertEquals((testMap.get(ItemLogic.URL)[0]),_item.getUrl());
        assertEquals((testMap.get(ItemLogic.TITLE)[0]),_item.getTitle());
        assertEquals((testMap.get(ItemLogic.LOCATION)[0]),_item.getLocation());
        assertEquals(String.valueOf(_item.getPrice()),testMap.get(ItemLogic.PRICE)[0]);
        assertEquals((testMap.get(ItemLogic.DESCRIPTION)[0]),_item.getDescription());
        
        testMap.replace(ItemLogic.URL, new String[]{generateString.apply(255)});
        testMap.replace(ItemLogic.TITLE, new String[]{generateString.apply(255)});
        testMap.replace(ItemLogic.LOCATION, new String[]{generateString.apply(45)});
        testMap.replace(ItemLogic.PRICE, new String[]{"823231234523.89"});
        String descriptionStr = generateString.apply(255);
        testMap.replace(ItemLogic.DESCRIPTION, new String[]{descriptionStr});
        _item = logic.createEntity(testMap);
        
        assertEquals(Integer.parseInt(testMap.get(ItemLogic.ID)[0]),_item.getId());
        assertEquals((testMap.get(ItemLogic.URL)[0]),_item.getUrl());
        assertEquals((testMap.get(ItemLogic.TITLE)[0]),_item.getTitle());
        assertEquals((testMap.get(ItemLogic.LOCATION)[0]),_item.getLocation());
        assertEquals(String.valueOf(_item.getPrice()),testMap.get(ItemLogic.PRICE)[0]);
        assertEquals((testMap.get(ItemLogic.DESCRIPTION)[0]),_item.getDescription());
    }

    @Test
    final boolean checkDuplicatedItem(Item item) {
        Item returnItem = logic.getWithUrl(item.getUrl());

        return (returnItem != null);
    }

    @Test
    final void TestDuplicatedItem() {
        
        logic.add(testItem);
        Item duplItem = logic.createEntity(sampleMap);
        setGenericCategory(testItem);
        setGenericImage(testItem);

        assertThrows(javax.persistence.PersistenceException.class, () -> logic.add(testItem));

    }
}
