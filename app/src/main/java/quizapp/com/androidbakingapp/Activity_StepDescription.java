package quizapp.com.androidbakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class Activity_StepDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);
        setTitle(this.getIntent().getStringExtra("step_title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = new Bundle();
        bundle.putInt("index", this.getIntent().getIntExtra("index", 0));
        bundle.putParcelableArrayList("steps",this.getIntent().getParcelableArrayListExtra("steps"));

        FragmentManager fm = getSupportFragmentManager();
        Fragment_StepDescription stepDescriptionFrag = (Fragment_StepDescription) fm.findFragmentByTag("fragment_recipeStep");

        if (stepDescriptionFrag == null) {
            stepDescriptionFrag = new Fragment_StepDescription();
            stepDescriptionFrag.setArguments(bundle);
            fm.beginTransaction().add(R.id.recipe_step_frame, stepDescriptionFrag, "fragment_recipeStep").commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
