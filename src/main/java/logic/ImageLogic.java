
package logic;

import common.ValidationException;
import dal.ImageDAL;
import entity.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Guilherme Soares Jardim
 */
public class ImageLogic extends GenericLogic<Image,ImageDAL>{
    
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ID = "id";
    
    public ImageLogic(){
        super(new ImageDAL());
    }
    @Override
    public List<Image> getAll(){
        return get(()->dao().findAll());
    }
    
    @Override
    public Image getWithId(int id){
        return get(()->dao().findById(id));
    }
    
    public List<Image> getWithUrl(String url){
        
        return get(()->dao().findByUrl(url));
    }
    
    public Image getWithPath (String path){
        return get(()->dao().findByPath(path));
    }
    
    public List<Image> getWithName(String name){
        return get(()->dao().findByName(name));
        
    }
    /**
     * Creates a new Image object.
     * Validates each field based on the constraints given in the database ERD.
     * @param parameterMap holds the parameters from server request, if any.
     * @throws ValidationException 
     */
    @Override
    public Image createEntity(Map<String,String[]> parameterMap){
        Image image = new Image();
        if(parameterMap.containsKey(ID)){
            try{
                image.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            }
            catch(NumberFormatException ex){
                throw new ValidationException("Id must be an integer.", new NumberFormatException());
            }
        }
        if(parameterMap.containsKey(URL)){
            if((parameterMap.get(URL)[0]!=null)&&(parameterMap.get(URL)[0].length()<256))
                if(!parameterMap.get(URL)[0].equals(""))
                    image.setUrl(parameterMap.get(URL)[0]);
                else image.setUrl("No url");
            else throw new ValidationException("Image Url invalid or null");
        }
        if(parameterMap.containsKey(PATH)){
            if((parameterMap.get(PATH)[0]!=null)&&(parameterMap.get(PATH)[0].length()<256))
                if(!parameterMap.get(PATH)[0].equals("")){
                    image.setPath(parameterMap.get(PATH)[0]);
                }
                else throw new ValidationException("Image Path invalid or null");
            else throw new ValidationException("Image Path invalid or null");
        }
        if(parameterMap.containsKey(NAME)){
            if((parameterMap.get(NAME)[0]!=null)&&(parameterMap.get(NAME)[0].length()<256))
                if(!parameterMap.get(NAME)[0].equals(""))
                    image.setName(parameterMap.get(NAME)[0]);
                else throw new ValidationException("Image Name invalid or null");
            else throw new ValidationException("Image Name invalid or null");
        }
        return image;
    }
    
    @Override
    public List<String> getColumnNames(){
        return Arrays.asList("Id","Url","Path","Name");
    }
    @Override
    public List<String> getColumnCodes(){
        return Arrays.asList(ID,URL,PATH,NAME);
    }
    @Override
    public List<?> extractDataAsList(Image e){
        return Arrays.asList(e.getId(),e.getUrl(),e.getPath(),e.getName());
    }
    
   

}
