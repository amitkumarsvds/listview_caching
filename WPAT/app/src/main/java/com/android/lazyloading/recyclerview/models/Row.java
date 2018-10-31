package com.android.lazyloading.recyclerview.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Row implements Serializable {

    private static final long serialVersionUID = 2L;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imageHref")
    @Expose
    private String imageHref;

    /**
     * get the title
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the title
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get the description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the image Url
     *
     * @return imageHref
     */
    public String getImageHref() {
        return imageHref;
    }

    /**
     * set the image URL
     *
     * @param imageHref imageHref
     */
    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    @Override
    public String toString() {
        return "Row{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageHref=" + imageHref +
                '}';
    }
}
