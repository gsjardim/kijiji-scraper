/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper.kijiji;

import java.util.Objects;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author gsoar
 */
public class KijijiItem {
    /*This class is called a POJO - Plain Old Java Object*/
    
    private String id;
    private String url;
    private String imageUrl;
    private String imageName;
    private String price;
    private String title;
    private String location;
    private String date;
    private String description;
    
    KijijiItem( ) {
       
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPrice() {
        return price;
    }

    void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    

}