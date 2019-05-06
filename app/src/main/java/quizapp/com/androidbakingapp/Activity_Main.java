package quizapp.com.androidbakingapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_Main extends AppCompatActivity {

    Retrofit retrofit;
    API_Ingredients api;

    @Nullable
    @BindView(R.id.nointernetmsg_tv)
    TextView noInternetMsg_tv;

    @Nullable
    @BindView(R.id.retry_btn)
    Button retry_btn;

    @Nullable
    @BindView(R.id.recipes_gv)
    GridView recipes_gv;

    List<Recipe> recipes;
    Adapter_Recipe recipeAdapter;

    @Nullable private  SimpleIdlingResource mIdlingResource = new SimpleIdlingResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isOnline()) {
            setContentView(R.layout.no_internet);
            ButterKnife.bind(this);

            noInternetMsg_tv.setText(R.string.no_internet_msg_long);

            retry_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOnline()) {
                        continueSetupAfterOnline();
                    }

                }
            });
        }
        else {
            continueSetupAfterOnline();
        }

    }

    private void continueSetupAfterOnline() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_Ingredients.BASE_URL)
                .build();

        api = retrofit.create(API_Ingredients.class);
        Call<List<Recipe>> call = api.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes = response.body();
                recipeAdapter = new Adapter_Recipe(Activity_Main.this,recipes);
                recipeAdapter.notifyDataSetChanged();
                recipes_gv.setAdapter(recipeAdapter);

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

                Toast.makeText(Activity_Main.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recipes_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(Activity_Main.this, Activity_RecipeDetail.class);
                intent.putExtra("recipe", recipes.get(position));
                startActivity(intent);

            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Activity_Main.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {

        }
        return mIdlingResource;
    }

}
