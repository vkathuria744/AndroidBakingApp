package quizapp.com.androidbakingapp;


import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_RecipeStep extends RecyclerView.Adapter<Adapter_RecipeStep.MyViewHolder> {


    private List<RecipeStep> steps;
    final private ListItemClickListener mOnClickListener;

    int clickedItemIndex = 0;
    boolean isTwoPane;

    public interface ListItemClickListener
    {
        void onListItemClick(int clickedItemIndex);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipeSteps_layout)
        ConstraintLayout layout;
        @BindView(R.id.step_name_1vrow_tv)
        TextView name_tv;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickedItemIndex = clickedPosition;
            notifyDataSetChanged();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
    public Adapter_RecipeStep(List<RecipeStep> steps, boolean isTwoPane, ListItemClickListener listener)
    {
       this.steps = steps;
       this.isTwoPane = isTwoPane;
       mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        RecipeStep step = steps.get(position);
        if (position==0)
        {
            holder.name_tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            holder.name_tv.setText(step.getShortDescription());

        }
        else {
            holder.name_tv.setText("Step #"+Integer.toString(position)+" "+step.getShortDescription());

        }
        if(isTwoPane) {
            if (clickedItemIndex == position) {
                holder.layout.setBackgroundResource(R.color.listRowSelected);

            }
            else {
                holder.layout.setBackgroundResource(R.color.listRow);
            }
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();

    }
}
