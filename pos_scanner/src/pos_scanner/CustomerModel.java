package pos_scanner;

public class CustomerModel extends Model
{

    private String firstname;
    private String surname;
    private String phone;
    private String card;
    private String email;

    private int scans = 0;

    public CustomerModel(
            String firstname,
            String surName,
            String phone,
            String card,
            String email
    )
    {
        this.firstname = firstname;
        this.surname = surName;
        this.phone = phone;
        this.card = card;
        this.email = email;
    }

    public String getFirstname()
    {
        return this.firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getSurname()
    {
        return this.surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getCard()
    {
        return this.card;
    }

    public void setCard(String card)
    {
        this.card = card;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setScans(int scans)
    {
        this.scans = scans;
    }

    public int getScans()
    {
        return scans;
    }

}
