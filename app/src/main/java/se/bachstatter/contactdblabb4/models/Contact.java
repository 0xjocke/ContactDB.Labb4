package se.bachstatter.contactdblabb4.models;

/**
 * Created by Jocek on 2014-09-17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Contact {
    /**
     * Class variables
     */
    private String imgUrl;
    private String description;
    private String name;
    private int age;

    /**
     * Setter for imgUrl
     * @param imgUrl
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    /**
     * Setter for Description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Setter for Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Setter for Age
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }


    /**
     * Constructor that sets all class variables.
     *
     * @param imgUrl
     * @param description
     * @param name
     * @param age
     */
    public Contact(String imgUrl, String name, String description, int age) {

        this.imgUrl = imgUrl;
        this.description = description;
        this.name = name;
        this.age = age;
    }

    /**
     * Getter for retruning imgUrl
     * @return
     */
    public String getImgUrl() {
        return imgUrl;
    }
    /**
     * Getter for retruning description
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     * Getter for retruning name
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for retruning age
     * @return
     */
    public int getAge() {
        return age;
    }

}