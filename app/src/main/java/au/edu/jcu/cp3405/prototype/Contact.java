package au.edu.jcu.cp3405.prototype;

public class Contact {
    String name;
    String mobileNum;
    String email;
    String id;

    public Contact() {
        name = "";
        mobileNum = "";
        email = "";
        id = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() { return mobileNum; }
}
