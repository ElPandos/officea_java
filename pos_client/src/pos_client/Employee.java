/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

/**
 *
 * @author Laptop
 */
public class Employee
{

    private int id;
    private String firstname;
    private String lastname;
    private String adress;
    private String postalcode;
    private String city;
    private String phone;
    private String mobile;
    private String email;
    private String gender;

    public Employee()
    {
    }

    public Employee(
            int perId,
            String perFirstname,
            String perLastname,
            String perAdress,
            String perPostalcode,
            String perCity,
            String perPhone,
            String perMobile,
            String perEmail,
            String perGender)
    {

        this.id = perId;
        this.firstname = perFirstname;
        this.lastname = perLastname;
        this.adress = perAdress;
        this.postalcode = perPostalcode;
        this.city = perCity;
        this.phone = perPhone;
        this.mobile = perMobile;
        this.email = perEmail;
        this.gender = perGender;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(int newId)
    {
        this.id = newId;
    }

    public String getFullname()
    {
        return this.firstname + " " + this.lastname;
    }

    public String getFirstname()
    {
        return this.firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return this.lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getAdress()
    {
        return this.adress;
    }

    public void setAdress(String adress)
    {
        this.adress = adress;
    }

    public String getPostalcode()
    {
        return this.postalcode;
    }

    public void setPostalcode(String postalcode)
    {
        this.postalcode = postalcode;
    }

    public String getCity()
    {
        return this.city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getMobile()
    {
        return this.mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getGender()
    {
        return this.gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
}
