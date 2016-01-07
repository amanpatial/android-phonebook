package com.above_inc.amanpatial.phonebook;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amanpatial on 05/01/16.
 */
public class Contact {

    private String firstname;
    private String lastname;
    private String phoneNumber;

    public Contact(String firstName, String lastName, String phoneNumber) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Contact(JSONObject object) {
        try {
            this.firstname = object.get("firstname").toString();
            this.lastname = object.get("lastName").toString();
            this.phoneNumber = object.get("phoneNumber").toString();
        }
        catch(JSONException e){
                e.printStackTrace();
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return this.firstname + " " + this.lastname + " " + this.phoneNumber;
    }
}
