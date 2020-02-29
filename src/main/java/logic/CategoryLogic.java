package logic;


import common.ValidationException;
import dal.CategoryDAL;
import entity.Category;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Guilherme Jardim
 */
public class CategoryLogic extends GenericLogic<Category,CategoryDAL>{
    
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ID = "id";
    
    public CategoryLogic() {
        super(new CategoryDAL());
    }
    
    @Override
    public List<Category> getAll(){
        return get(()->dao().findAll());
    }
        
    @Override
    public Category getWithId( int id){
        return get(()->dao().findById(id));
    }
    
    public Category getWithUrl ( String url) {
        
         return get(()->dao().findByURL(url));
        
        
    }
    
    public Category getWithTitle( String title){
        return get(()->dao().findByTitle(title));
    }
    
   
    /**
     * Creates a new Category object.
     * Validates each field based on the constraints given in the database ERD.
     * @param parameterMap holds the parameters from server request, if any.
     * @throws ValidationException 
     */
    @Override
    public Category createEntity(Map<String, String[]> parameterMap) {
        Category category = new Category();
        if(parameterMap.containsKey(ID)){
            if(parameterMap.get(ID)[0]!=null){
                try{
                    category.setId(Integer.parseInt(parameterMap.get(ID)[0]));
                }
                catch(NumberFormatException ex){
                    throw new ValidationException("Id must be an integer", new NumberFormatException());
                }
            }
            else throw new ValidationException("Category Id: null");
        }
        if(parameterMap.containsKey(URL)){
            if((parameterMap.get(URL)[0]!=null)&&(parameterMap.get(URL)[0].length()<256))
                if(!parameterMap.get(URL)[0].equals(""))
                    category.setUrl(parameterMap.get(URL)[0]);
                else throw new ValidationException("Category Title: invalid or null");
            else throw new ValidationException("Category URL: invalid or null");
        }
        if(parameterMap.containsKey(TITLE)){
            if((parameterMap.get(TITLE)[0]!=null)&&(parameterMap.get(TITLE)[0].length()<256))
                if(!parameterMap.get(TITLE)[0].equals(""))
                    category.setTitle(parameterMap.get(TITLE)[0]);
                else throw new ValidationException("Category Title: invalid or null");
            else throw new ValidationException("Category Title: invalid or null");
        }
        return category;
    } 

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("id", "url", "title");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, URL, TITLE );
    }

    @Override
    public List<?> extractDataAsList( Category e) {
        return Arrays.asList(e.getId(), e.getUrl(), e.getTitle());
    }
}
