package logic;

import common.ValidationException;
import dal.ItemDAL;
import entity.Category;
import logic.CategoryLogic;
import logic.ImageLogic;
import entity.Image;
import entity.Item;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Guilherme Soares Jardim
 */
public class ItemLogic extends GenericLogic<Item, ItemDAL> {

    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_ID = "categoryId";
    public static final String IMAGE_ID = "imageId";
    public static final String LOCATION = "location";
    public static final String PRICE = "price";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String URL = "url";
    public static final String ID = "id";

    public ItemLogic() {
        super(new ItemDAL());
    }

    @Override
    public List<Item> getAll() {
        return get(() -> dao().findAll());
    }

    @Override
    public Item getWithId(int id) {
        return get(() -> dao().findById(id));
    }

    public List<Item> getWithPrice(BigDecimal price) {
        return get(() -> dao().findByPrice(price));
    }

    public List<Item> getWithTitle(String title) {
        return get(() -> dao().findByTitle(title));
    }

    public List<Item> getWithDate(String date) {
        return get(() -> dao().findByDate(date));
    }

    public List<Item> getWithLocation(String location) {
        return get(() -> dao().findByLocation(location));
    }

    public List<Item> getWithDescription(String description) {
        return get(() -> dao().findByDescription(description));
    }

    public Item getWithUrl(String url) {

        return get(() -> dao().findByUrl(url));
    }

    public List<Item> getWithCategory(int categoryId) {
        return get(() -> dao().findByCategory(categoryId));
    }
    
    public List<Item> getWithImage(int imageId) {
        return get(() -> dao().findByImage(imageId));
    }

    /**
     * Creates a new Item object.
     * Validates each field based on the constraints given in the database ERD.
     * @param parameterMap holds the parameters from server request, if any.
     * @throws ValidationException 
     */
    @Override
    public Item createEntity(Map<String, String[]> parameterMap) {
        Item item = new Item();

        if (parameterMap.containsKey(ID)) {
            if(parameterMap.get(ID)[0] != null){
                if (!parameterMap.get(ID)[0].equals(""))
                    try{
                        item.setId(Integer.parseInt(parameterMap.get(ID)[0]));
                    }
                    catch(NumberFormatException ex){
                        throw new ValidationException("Invalid Item id.", new NumberFormatException());
                    }
                else throw new ValidationException("Invalid Item id.");
            }
            else throw new ValidationException("Item id: null.");
        }

        if (parameterMap.containsKey(PRICE)) {
            if (parameterMap.get(PRICE)[0].length() < 16) {
                if (!parameterMap.get(PRICE)[0].equals("")&&parameterMap.get(PRICE)[0]!=null) {
                    String numPrice = parameterMap.get(PRICE)[0].replace('$', ' ').trim();

                    try {
                        BigDecimal price = new BigDecimal(numPrice);
                        item.setPrice(price);
                    } catch (NumberFormatException ex) {
                        item.setPrice(BigDecimal.valueOf(0.00));
                        throw new ValidationException("Item price: invalid value");
                    }
                } else {
                    item.setPrice(BigDecimal.valueOf(0.00));
                }
            } else throw new ValidationException("Item price must have maximum of 15 digits.");
            
        }

        if (parameterMap.containsKey(TITLE)) {
            if (((parameterMap.get(TITLE)[0]).length() < 256) && (parameterMap.get(TITLE)[0] != null)) {
                if(!parameterMap.get(TITLE)[0].equals(""))
                    item.setTitle(parameterMap.get(TITLE)[0]);
                else throw new ValidationException("Item title: invalid or null");
            }
            else throw new ValidationException("Item title must have maximum of 255 characters.");
        }
        Date createDate;
        if (parameterMap.containsKey(DATE)) {
            
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat dateFormater = new SimpleDateFormat(pattern);
            try {
                createDate = dateFormater.parse(parameterMap.get(DATE)[0]);
                item.setDate(createDate);
            } catch (ParseException ex) {
                createDate = new Date();
                item.setDate(createDate);
            }
        }

        if (parameterMap.containsKey(LOCATION)) {
            if (((parameterMap.get(LOCATION)[0]).length() < 46) && (parameterMap.get(LOCATION)[0] != null)) {
                item.setLocation(parameterMap.get(LOCATION)[0]);
            }
            else throw new ValidationException("Item location must have maximum of 45 characters.");
        }

        if (parameterMap.containsKey(DESCRIPTION)) {
            if (((parameterMap.get(DESCRIPTION)[0]).length() < 256) && (parameterMap.get(DESCRIPTION)[0] != null)) {
                if(!parameterMap.get(DESCRIPTION)[0].equals(""))
                    item.setDescription(parameterMap.get(DESCRIPTION)[0]);
                else throw new ValidationException("Item description cannot be empty.");
            } else if (parameterMap.get(DESCRIPTION)[0].length() >= 256) {
                item.setDescription(parameterMap.get(DESCRIPTION)[0].substring(0, 255));
            }
        }

        if (parameterMap.containsKey(URL)) {
            if (((parameterMap.get(URL)[0]).length() < 256) && (parameterMap.get(URL)[0] != null)) {
                if(!parameterMap.get(URL)[0].equals(""))
                    item.setUrl(parameterMap.get(URL)[0]);
                else throw new ValidationException("Item Url cannot be empty.");
            }
            else throw new ValidationException("Item url must have maximum of 255 characters.");
        }

        return item;
    }

    @Override
    public List<String> getColumnNames() {
        //return Arrays.asList("description", "categoryId", "imageId", "location", "price", "title", "date", "url", "id");
        return Arrays.asList("Id", "Url", "Title", "Date", "Description", "Price", "Location", "Category Id", "Image Id");
    }

    @Override
    public List<String> getColumnCodes() {
        //return Arrays.asList(DESCRIPTION, CATEGORY_ID, IMAGE_ID, LOCATION, PRICE, TITLE, DATE, URL, ID);
        return Arrays.asList(ID, URL, TITLE, DATE, DESCRIPTION, PRICE, LOCATION, CATEGORY_ID, IMAGE_ID);
    }

    @Override
    public List<?> extractDataAsList(Item e) {
        return Arrays.asList(e.getId(), e.getUrl(), e.getTitle(), e.getDate(), e.getDescription(), e.getPrice(),
                e.getLocation(), e.getCategory().getId(), e.getImage().getId());
    }

}
