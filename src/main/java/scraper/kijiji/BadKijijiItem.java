package scraper.kijiji;

import java.util.Objects;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public final class BadKijijiItem {
    
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

    private final Element element;

    BadKijijiItem( final Element element) {
        this.element = element;
    }

    public String getUrl() {
        return URL_BASE+element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim();
    }

    public String getId() {
        return element.attr(ATTRIBUTE_ID).trim();
    }

    public String getImageUrl() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            return "";
        }
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).attr("src").trim();
            if (image.isEmpty()) {
                image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
            }
        }
        return image;
    }

    public String getImageName() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            return "";
        }
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim();

        }
        return image;
    }

    public String getPrice() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }

    public String getTitle() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_TITLE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).child(0).text().trim();
    }

    public String getDate() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_DATE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }

    public String getLocation() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_LOCATION);
        if(elements.isEmpty())
            return "";
        return elements.get(0).childNode(0).outerHtml().trim();
    }

    public String getDescription() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BadKijijiItem other = (BadKijijiItem) obj;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public String toString() {
        return String.format("[id:%s, image_url:%s, image_name:%s, price:%s, title:%s, date:%s, location:%s, description:%s]",
                getId(), getImageUrl(), getImageName(), getPrice(), getTitle(), getDate(), getLocation(), getDescription());
    }
}
