package logic;

import common.TomcatStartUp;
import common.ValidationException;
import static org.junit.jupiter.api.Assertions.*;

import entity.Image;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.IntFunction;
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
class ImageLogicTest {

   
    private ImageLogic logic;
    private Map<String, String[]> sampleMap;
    private Image testImage;

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
        logic = new ImageLogic();
       
        sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.PATH, new String[]{"pc_images.jpeg"});
        sampleMap.put(ImageLogic.NAME, new String[]{"pc_images"});
        sampleMap.put(ImageLogic.URL, new String[]{"newimages.com"});
        testImage = logic.createEntity(sampleMap);
       
    }

    @AfterEach
    final void tearDown() throws Exception {
        if(logic.getWithPath(testImage.getPath())!=null)
            logic.delete(testImage);
    }

    @Test
    final void testGetAll() {
       
        List<Image> list = logic.getAll();
        int originalSize = list.size();
        
        logic.add(testImage);
        list = logic.getAll();
        assertEquals(originalSize+1, list.size());
        logic.delete(testImage);
        list = logic.getAll();
        assertEquals(originalSize, list.size());
    }

    @Test
    final void testGetWithId() {
        
        logic.add(testImage);
        
        List<Image> list = logic.getAll();
        Image sampleImage = list.get(list.size()-1);
        Image returnedImage = logic.getWithId(sampleImage.getId());
        assertEquals(sampleImage.getId(), returnedImage.getId());
        assertEquals(sampleImage.getPath(), returnedImage.getPath());
        assertEquals(sampleImage.getUrl(), returnedImage.getUrl());
        assertEquals(sampleImage.getName(), returnedImage.getName());
        
    }

    @Test
    final void testGetWithUrl() {
        
       logic.add(testImage);
       List<Image> images = logic.getWithUrl(testImage.getUrl());
       for (Image img:images)
           assertEquals(img.getUrl(),testImage.getUrl());
       

    }

    @Test
    final void testGetWithPath() {
        
        logic.add(testImage);
        Image returnedImage = logic.getWithPath(testImage.getPath());
        
        assertEquals(testImage.getPath(), returnedImage.getPath());
        assertEquals(testImage.getName(), returnedImage.getName());
        assertEquals(testImage.getUrl(), returnedImage.getUrl());
        
    }
    
    @Test
    final void testGetWithName() {
        
        logic.add(testImage);
        List<Image> images = logic.getWithName(testImage.getName());
        
        for(Image img:images){
            assertEquals(testImage.getName(), img.getName());
        }
        
    }

    
    @Test
    final void testCreateEntity() {
       
        Map<String, String[]> testMap1 = new HashMap<>();
        testMap1.put(ImageLogic.ID, new String[]{"100"});
        testMap1.put(ImageLogic.PATH, new String[]{"Entity test"});
        testMap1.put(ImageLogic.URL, new String[]{"Test_user"});
        testMap1.put(ImageLogic.NAME, new String[]{"image"});
        Image testImage_1 = logic.createEntity(testMap1);
        
        Map<String, String[]> testMap2 = new HashMap<>();
        testMap2.put(ImageLogic.ID, new String[]{"101"});
        testMap2.put(ImageLogic.PATH, new String[]{"Entity2 test"});
        testMap2.put(ImageLogic.URL, new String[]{"Test_user2"});
        testMap2.put(ImageLogic.NAME, new String[]{"image2"});
        Image testImage_2 = logic.createEntity(testMap2);
        
        
        assertFalse(testImage_1.equals(testImage_2));
       
        assertEquals(testImage_2.getId(),(Integer.parseInt(testMap2.get(ImageLogic.ID)[0])));
        assertEquals(testImage_2.getUrl(),testMap2.get(ImageLogic.URL)[0]);
        assertEquals(testImage_2.getPath(),testMap2.get(ImageLogic.PATH)[0]);
        assertEquals(testImage_2.getName(),testMap2.get(ImageLogic.NAME)[0]);
        
    }
    
    

    @Test
    final void testGetColumnNames() {
        
        List<String> list1 = logic.getColumnNames();
        List<String> list2 = Arrays.asList("Id","Url","Path","Name");
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testGetColumnCodes() {
        
        List<String> list1 = logic.getColumnCodes();
        List<String> list2 = Arrays.asList(logic.ID,logic.URL,logic.PATH,logic.NAME);
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test
    final void testExtractDataAsList() {
        //Add an Id to sample Map and create an Entity with it.
        sampleMap.put(ImageLogic.ID, new String[]{"50"});
        Image testImageWithId = logic.createEntity(sampleMap);
        //Use the extractDataAsList method to create a List of "unknown" types
        List<?> list = logic.extractDataAsList(testImageWithId);
        //compare that they both have the same data
        assertEquals(testImageWithId.getId(), list.get(0));
        assertEquals(testImageWithId.getUrl(), list.get(1));
        assertEquals(testImageWithId.getPath(), list.get(2));
        assertEquals(testImageWithId.getName(), list.get(3));
        
    }
    
    @Test
    final void testCreateEntityWithNullValues(){
        
        Map<String, String[]> errorMap = sampleMap;
        errorMap.put(ImageLogic.ID, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.ID, new String[]{"5"});
        errorMap.replace(ImageLogic.URL, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.URL, new String[]{"catUrl"});
        errorMap.replace(ImageLogic.PATH, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.PATH, new String[]{"catUrl"});
        errorMap.replace(ImageLogic.NAME, null);
        assertThrows(NullPointerException.class,()-> logic.createEntity(errorMap));
       
        
    }
    
    @Test
    final void testCreateEntityWithEmptyValues (){
        
        Map<String, String[]> errorMap = sampleMap;
        
        errorMap.replace(ImageLogic.URL, new String[]{""});
        errorMap.replace(ImageLogic.PATH, new String[]{""});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.PATH, new String[]{"catUrl"});
        errorMap.replace(ImageLogic.NAME, new String[]{""});
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
        errorMap.put(ImageLogic.ID, new String[]{"5o"});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.ID, new String[]{"5"});
        errorMap.replace(ImageLogic.URL, new String[]{generateString.apply(260)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.URL, new String[]{"catUrl"});
        errorMap.replace(ImageLogic.PATH, new String[]{generateString.apply(260)});
        assertThrows(ValidationException.class,()-> logic.createEntity(errorMap));
        
        errorMap.replace(ImageLogic.PATH, new String[]{"catUrl"});
        errorMap.replace(ImageLogic.NAME, new String[]{generateString.apply(260)});
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
        
        testMap.put(ImageLogic.ID, new String[]{"1"});
        testMap.put(ImageLogic.URL, new String[]{generateString.apply(1)});
        testMap.put(ImageLogic.PATH, new String[]{generateString.apply(1)});
        testMap.put(ImageLogic.NAME, new String[]{generateString.apply(1)});
        Image img = logic.createEntity(testMap);
        
        assertEquals(Integer.parseInt(testMap.get(ImageLogic.ID)[0]),img.getId());
        assertEquals((testMap.get(ImageLogic.URL)[0]),img.getUrl());
        assertEquals((testMap.get(ImageLogic.PATH)[0]),img.getPath());
        assertEquals((testMap.get(ImageLogic.NAME)[0]),img.getName());
        
        testMap.replace(ImageLogic.URL, new String[]{generateString.apply(255)});
        testMap.replace(ImageLogic.PATH, new String[]{generateString.apply(255)});
        testMap.replace(ImageLogic.NAME, new String[]{generateString.apply(255)});
        img = logic.createEntity(testMap);
        
        assertEquals(Integer.parseInt(testMap.get(ImageLogic.ID)[0]),img.getId());
        assertEquals((testMap.get(ImageLogic.URL)[0]),img.getUrl());
        assertEquals((testMap.get(ImageLogic.PATH)[0]),img.getPath());
        assertEquals((testMap.get(ImageLogic.NAME)[0]),img.getName());
        
    }
    
        
    @Test
    final void TestDuplicatedImage (){
        
        logic.add(testImage);
        Image duplImage = logic.createEntity(sampleMap);
        
        assertThrows(javax.persistence.PersistenceException.class,()-> logic.add(duplImage));
       
        
        
    }
}
