package quizapp.com.androidbakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    String id;
    String name;

    List<Ingredient> ingredients;
    ArrayList<RecipeStep> steps;
    String servings;
    String image;

    public String getId() {
        return id;

    }
    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }
    public String getServings() {
        return servings;
    }
    public String getImage() {
        return image;
    }

    public Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        steps = in.readArrayList(RecipeStep.class.getClassLoader());
        servings = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeString(servings);
        dest.writeString(image);

    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int describeContents() {
        return 0;
    }

}
