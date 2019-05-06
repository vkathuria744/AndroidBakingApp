package quizapp.com.androidbakingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    String ingredient;
    String quantity;
    String measure;

    public Ingredient(String ingredient)
    {
        this.ingredient = ingredient;
    }
    public String getIngredient(){
        return ingredient;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getMeasure() {
        return measure;
    }
    public Ingredient(Parcel in) {
        ingredient = in.readString();
        quantity = in.readString();
        measure  = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        dest.writeString(quantity);
        dest.writeString(measure);
    }
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {

        public Ingredient createFromParcel(Parcel in)
        {
            return new Ingredient(in);
        }
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
