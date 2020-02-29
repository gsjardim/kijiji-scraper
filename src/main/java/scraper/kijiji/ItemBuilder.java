
package scraper.kijiji;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author gsoar
 */
public class ItemBuilder {

    private static final String URL_BASE = "https://www.kijiji.ca";
    private static final String ATTRIBUTE_ID = "data-listing-id";
    private static final String ATTRIBUTE_IMAGE = "image";
    private static final String ATTRIBUTE_IMAGE_SRC = "data-src";
    private static final String ATTRIBUTE_IMAGE_NAME = "alt";
    private static final String ATTRIBUTE_PRICE = "price";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_LOCATION = "location";
    private static final String ATTRIBUTE_DATE = "date-posted";
    private static final String ATTRIBUTE_DESCRIPTION = "description";

    private Element element;
    private KijijiItem item;

    ItemBuilder(){
        item = new KijijiItem();
    }
    
    public ItemBuilder setElement(Element element){
        this.element = element;
        return this;
    }
    
    public KijijiItem build(){
        //url
        item.setUrl(URL_BASE+element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim());
        //id
        item.setId(element.attr(ATTRIBUTE_ID).trim());
        
        //imageUrl
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if(elements.isEmpty())
            item.setImageUrl("");
        String imageUrl = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
        if (imageUrl.isEmpty()) {
            imageUrl = elements.get(0).child(0).attr("src").trim();
            if (imageUrl.isEmpty()) {
                imageUrl = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
            }
        }
        item.setImageUrl(imageUrl);
        
        //imageName
        if(elements.isEmpty())
            item.setImageName("");
        String imageName = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim();
        if (imageName.isEmpty()) {
            imageName = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim();

        }
        item.setImageName(imageName);
        
        //price
        elements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if(elements.isEmpty())
            item.setPrice("");
        else item.setPrice(elements.get(0).text().trim());
        //title
        elements = element.getElementsByClass(ATTRIBUTE_TITLE);
        if(elements.isEmpty())
            item.setTitle("");
        else item.setTitle(elements.get(0).child(0).text().trim());
        //location
        elements = element.getElementsByClass(ATTRIBUTE_LOCATION);
        if(elements.isEmpty())
            item.setLocation("");
        else item.setLocation(elements.get(0).childNode(0).outerHtml().trim());
        //date
        elements = element.getElementsByClass(ATTRIBUTE_DATE);
        if(elements.isEmpty())
            item.setDate("");
        else item.setDate(elements.get(0).text().trim());
        //description
        elements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
        if(elements.isEmpty())
            item.setDescription("");
        else item.setDescription(elements.get(0).text().trim());
        
        return item;
    }


}
