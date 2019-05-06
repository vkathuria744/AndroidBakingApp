package quizapp.com.androidbakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GridRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

    private class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        ArrayList<Ingredient> ingredients;
        public GridRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }
        @Override
        public void onCreate(){}

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = mContext.getSharedPreferences(Activity_RecipeDetail.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = preferences.getString(Activity_RecipeDetail.INGREDIENTS_KEY, "");
            Type type = new TypeToken<ArrayList<Ingredient>>(){}.getType();
            ingredients = gson.fromJson(json, type);
        }
        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_row);
            rv.setTextViewText(R.id.ingredient_widget_1vrow_tv,
                    Integer.toString(position+1)+". "+ingredients.get(position).getIngredient()
            +", "+ingredients.get(position).getQuantity()+ " "+ingredients.get(position).getMeasure());

            return rv;
        }
        @Override
        public void onDestroy() {}

        @Override
        public int getCount() {
            if (ingredients != null) {
                return ingredients.size();

            }
            else return 0;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }
        @Override
        public int getViewTypeCount() {
            return 1;
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
