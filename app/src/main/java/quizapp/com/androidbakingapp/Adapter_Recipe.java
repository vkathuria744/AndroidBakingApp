package quizapp.com.androidbakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static quizapp.com.androidbakingapp.R.drawable.dummy_recipe;

public class Adapter_Recipe extends BaseAdapter {

    private Context mContext;
    private List<Recipe> recipes;

    public Adapter_Recipe(Context c, List<Recipe> recipes) {
        mContext = c;
        this.recipes = recipes;
    }

    public int getCount() {
        return recipes.size();

    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_list_row, parent, false);

        Recipe recipe = recipes.get(position);
        TextView name_tv = itemView.findViewById(R.id.recipe_name_1vrow_tv);
        name_tv.setText(recipe.getName());

        ImageView imageView = itemView.findViewById(R.id.recipe_image_1vrow_iv);

        if (recipe.getImage() == "") {
            imageView.setImageResource(dummy_recipe);
        }


        return itemView;
    }
}