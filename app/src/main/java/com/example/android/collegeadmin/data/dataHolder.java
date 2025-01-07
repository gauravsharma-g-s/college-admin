package com.example.android.collegeadmin.data;

public class dataHolder {
    private String Name;
    private String Email;
    private String Qualification;
    private int Gender;
    private String ImageUrl;
private String nameImage;
    public dataHolder(){}

    public dataHolder(String nameImage,String Name, String Email, String Qualification, int Gender, String ImageUrl) {
        this.nameImage = nameImage;
        this.Name = Name;
        this.Email = Email;
        this.Qualification = Qualification;
        this.Gender = Gender;
        this.ImageUrl = ImageUrl;
    }

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String NameImage) {
        nameImage = NameImage;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String Qualification) {
        this.Qualification = Qualification;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }
}
