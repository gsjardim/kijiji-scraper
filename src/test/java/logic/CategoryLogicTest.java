package logic;

import common.TomcatStartUp;
import common.ValidationException;
import static org.junit.jupiter.api.Assertions.*;

import entity.Category;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.Random;
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
class CategoryLogicTest {

    
    private CategoryLogic logic;
    private Map<String, String[]> sampleMap;
    Category testCategory;

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
        logic = new CategoryLogic();
       
        sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.TITLE, new String[]{"keyboards"});
        sampleMap.put(CategoryLogic.URL, new String[]{"keyboards.com"});
        
        testCategory = logic.createEntity(sampleMap);
       
    }

    @AfterEach
    final void tearDown() throws Exception {
        if(logic.getWithTitle(testCategory.getTitle())!=null)
            logic.delete(testCategory);
    }

    @Test
    final void testGetAll() {
       
        List<Category> list = logic.getAll();
        int originalSize = list.size();
        logic.add(testCategory);
        list = logic.getAll();
        assertEquals(originalSize+1, list.size());
        logic.delete(testCategory);
        list = logic.getAll();
        assertEquals(originalSize, list.size());
    }

    @Test
    final void testGetWithId() {
        
        logic.add(testCategory);
        List<Category> list = logic.getAll();
        Category sampleCategory = list.get(list.size()-1);
        Category returnedCategory = logic.getWithId(sampleCategory.getId());
        assertEquals(sampleCategory.getId(), returnedCategory.getId());
        assertEquals(sampleCategory.getTitle(), returnedCategory.getTitle());
        assertEquals(sampleCategory.getUrl(), returnedCategory.getUrl());
       
    }

    @Test
    final void testGetWithUrl() {
        
        logic.add(testCategory);
        Category returnedCategory = logic.getWithUrl(testCategory.getUrl());
        
        assertEquals(testCategory.getTitle(), returnedCategory.getTitle());
        assertEquals(testCategory.getUrl(), returnedCategory.getUrl());
        

    }

    @Test
    final void testGetWithTitle() {
        
        logic.add(testCategory);
        Category returnedCategory = logic.getWithTitle(testCategory.getTitle());
        
        assertEquals(testCategory.getTitle(), returnedCategory.getTitle());
        assertEquals(testCategory.getUrl(), returnedCategory.getUrl());
        
    }

    
    @Test
    final void testCreateEntity() {
       
        Map<String, String[]> testMap1 = new HashMap<>();
        testMap1.put(CategoryLogic.ID, new String[]{"100"});
        testMap1.put(CategoryLogic.TITLE, new String[]{"Entity test"});
        testMap1.put(CategoryLogic.URL, new String[]{"Test_user"});
        Category testCategory_1 = logic.createEntity(testMap1);
        
        Map<String, String[]> testMap2 = new HashMap<>();
        testMap2.put(CategoryLogic.ID, new String[]{"101"});
        testMap2.put(CategoryLogic.TITLE, new String[]{"Entity2 test"});
        testMap2.put(CategoryLogic.URL, new String[]{"Test_user2"});
        Category testCategory_2 = logic.createEntity(testMap2);
        
        
        assertFalse(testCategory_1.equals(testCategory_2));
        assertEquals(testCategory_2.getId(),(Integer.parseInt(testMap2.get(CategoryLogic.ID)[0])));
        assertEquals(testCategory_2.getUrl(),testMap2.get(CategoryLogic.URL)[0]);
        assertEquals(testCategory_2.getTitle(),testMap2.get(CategoryLogic.TITLE)[0]);
        
        
    }
    
    

    @Test
    final void testGetColumnNames() {
        
        List<String> list1 = logic.getColumnNames();
        List<String> list2 = Arrays.asList("id", "url", "title");
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list1 = logic.getColumnCodes();
        List<String> list2 = Arrays.asList(logic.ID,logic.URL, logic.TITLE);
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testExtractDataAsList() {
        
        sampleMap.put(CategoryLogic.ID, new String[]{"50"});
        Category testCategoryWithId = logic.createEntity(sampleMap);
        List<?> list = logic.extractDataAsList(testCategoryWithId);
        assertEquals(testCategoryWithId.getId(), list.get(0));
        assertEquals(testCategoryWithId.getUrl(), list.get(1));
        assertEquals(testCategoryWithId.getTitle(), list.get(2));
        
    }
    
    @Test
    final void testCreateEntityWithNullValues(){
        
        Map<String, String[]> errorMap = sampleMap;
        errorMap.put(CategoryLogic.ID, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(CategoryLogic.ID, new String[]{"5"});
        errorMap.replace(CategoryLogic.URL, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(CategoryLogic.URL, new String[]{"catUrl"});
        errorMap.replace(CategoryLogic.TITLE, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
       
        
    }
    
    @Test
    final void testCreateEntityWithEmptyValues (){
        
        Map<String, String[]> errorMap = sampleMap;
        
        errorMap.replace(CategoryLogic.URL, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(CategoryLogic.URL, new String[]{"catUrl"});
        errorMap.replace(CategoryLogic.TITLE, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
    }
    
    @Test
    final void testCreateEntityWithValuesTooLong (){
        
        Map<String, String[]> errorMap = sampleMap;
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };
        errorMap.put(CategoryLogic.ID, new String[]{"5o"});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(CategoryLogic.ID, new String[]{"5"});
        errorMap.replace(CategoryLogic.URL, new String[]{generateString.apply(260)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(CategoryLogic.URL, new String[]{"catUrl"});
        errorMap.replace(CategoryLogic.TITLE, new String[]{generateString.apply(260)});
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
        
        testMap.put(CategoryLogic.ID, new String[]{"1"});
        testMap.put(CategoryLogic.URL, new String[]{generateString.apply(1)});
        testMap.put(CategoryLogic.TITLE, new String[]{generateString.apply(1)});
        Category cat = logic.createEntity(testMap);
        
        assertEquals(Integer.parseInt(testMap.get(CategoryLogic.ID)[0]),cat.getId());
        assertEquals((testMap.get(CategoryLogic.URL)[0]),cat.getUrl());
        assertEquals((testMap.get(CategoryLogic.TITLE)[0]),cat.getTitle());
        
        testMap.replace(CategoryLogic.URL, new String[]{generateString.apply(255)});
        testMap.replace(CategoryLogic.TITLE, new String[]{generateString.apply(255)});
        cat = logic.createEntity(testMap);
        
        assertEquals(Integer.parseInt(testMap.get(CategoryLogic.ID)[0]),cat.getId());
        assertEquals((testMap.get(CategoryLogic.URL)[0]),cat.getUrl());
        assertEquals((testMap.get(CategoryLogic.TITLE)[0]),cat.getTitle());
        
    }
        
    @Test
    final void TestDuplicatedCategory (){
        
        logic.add(testCategory);
        Category duplCategory = logic.createEntity(sampleMap);
        
        assertThrows(javax.persistence.PersistenceException.class,()-> logic.add(duplCategory));
       
        
        
    }
}
