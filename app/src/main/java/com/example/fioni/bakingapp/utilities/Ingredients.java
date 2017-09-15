package com.example.fioni.bakingapp.utilities;

/**
 * Created by fioni on 9/2/2017.
 */

public class Ingredients {
    private String r_id;
    private String quantity;
    private String measure;
    private String ingr_name;


    public Ingredients() {
    }

    public Ingredients(String r_id, String quantity, String measure, String ingr_name) {
        this.r_id = r_id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingr_name = ingr_name;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngr_name() {
        return ingr_name;
    }

    public void setIngr_name(String ingr_name) {
        this.ingr_name = ingr_name;
    }
}
