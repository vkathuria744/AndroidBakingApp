package quizapp.com.androidbakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

public class OnRecipeClickTest {

    @Rule
    public ActivityTestRule<Activity_Main> mActivityRule = new ActivityTestRule<>(Activity_Main.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();

        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void onRecipeClick() {

        onData(anything()).inAdapterView(withId(R.id.recipes_gv)).atPosition(0).perform(click());
        onView((withId(R.id.ingredientsLabel_tv))).check(matches(withText("Ingredients")));


    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
