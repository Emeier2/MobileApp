package com.example.dev;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "profileTable")
public class UserData{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    private int ID = 0;
    @NonNull
    @ColumnInfo(name = "firstName")
    private String firstName;
    @NonNull
    @ColumnInfo(name = "lastName")
    private String lastName;
    @NonNull
    @ColumnInfo(name = "age")
    private String age;
    @NonNull
    @ColumnInfo(name = "sex")
    private String sex;
    @NonNull
    @ColumnInfo(name = "city")
    private String city;
    @NonNull
    @ColumnInfo(name = "country")
    private String country;
    @NonNull
    @ColumnInfo(name = "height")
    private String height;
    @NonNull
    @ColumnInfo(name = "weight")
    private String weight;
    @NonNull
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;


    public UserData(int ID, String firstName, String lastName, String age, String sex,
                    String city, String country, String height, String weight, byte[] image)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
        this.city = city;
        this.country = country;
        this.height = height;
        this.weight = weight;
        this.image = image;
    }


//    protected UserData(Parcel in) {
//        ID = in.readInt();
//        firstName = in.readString();
//        lastName = in.readString();
//        age = in.readString();
//        sex = in.readString();
//        city = in.readString();
//        country = in.readString();
//        height = in.readString();
//        weight = in.readString();
//        image = in.readParcelable(Bitmap.class.getClassLoader());
//    }

//    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
//        @Override
//        public UserData createFromParcel(Parcel in) {
//            return new UserData(in);
//        }
//
//        @Override
//        public UserData[] newArray(int size) {
//            return new UserData[size];
//        }
//    };

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(ID);
//        dest.writeString(firstName);
//        dest.writeString(lastName);
//        dest.writeString(age);
//        dest.writeString(sex);
//        dest.writeString(city);
//        dest.writeString(country);
//        dest.writeString(height);
//        dest.writeString(weight);
//        dest.writeValue(image);
//    }

    public int getID(){ return ID;}

    public String getFirstName(){ return firstName;}
    public void setFirstName(String firstName){ this.firstName = firstName;}

    public String getLastName(){ return lastName;}
    public void setLastName(String lastName){ this.lastName = lastName;}

    public String getSex(){ return sex;}
    public void setSex(String sex){ this.sex = sex;}

    public String getAge(){ return age;}
    public void setAge(String age){ this.age = age;}

    public String getCity(){ return city;}
    public void setCity(String city){ this.city = city;}

    public String getCountry(){ return country;}
    public void setCountry(String country){ this.country = country;}

    public String getHeight(){ return height;}
    public void setHeight(String height){ this.height = height;}

    public String getWeight(){ return weight;}
    public void setWeight(String weight){ this.weight = weight;}

    public byte[] getImage(){ return image;}
    public void setImage(byte[] image){ this.image = image;}
}
