package com.zhangly.zmenu.api.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangluya on 16/8/8.
 */
public class Menus {

    public List<Menu> data;
    public String totalNum;
    public String pn;
    public String rn;

    public static class Menu implements Parcelable {

        public String id;
        public String title;
        public String tags;
        public String imtro;
        public String ingredients;
        public String burden;
        public List<String> albums;
        public ArrayList<Step> steps;

        public Menu() {
        }

        protected Menu(Parcel in) {
            id = in.readString();
            title = in.readString();
            tags = in.readString();
            imtro = in.readString();
            ingredients = in.readString();
            burden = in.readString();
            albums = in.createStringArrayList();
            steps = in.createTypedArrayList(Step.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(title);
            dest.writeString(tags);
            dest.writeString(imtro);
            dest.writeString(ingredients);
            dest.writeString(burden);
            dest.writeStringList(albums);
            dest.writeTypedList(steps);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Menu> CREATOR = new Creator<Menu>() {
            @Override
            public Menu createFromParcel(Parcel in) {
                return new Menu(in);
            }

            @Override
            public Menu[] newArray(int size) {
                return new Menu[size];
            }
        };
    }

    public static class Step implements Parcelable {

        public String img;
        public String step;

        protected Step(Parcel in) {
            img = in.readString();
            step = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(img);
            dest.writeString(step);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Step> CREATOR = new Creator<Step>() {
            @Override
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            @Override
            public Step[] newArray(int size) {
                return new Step[size];
            }
        };
    }
}
