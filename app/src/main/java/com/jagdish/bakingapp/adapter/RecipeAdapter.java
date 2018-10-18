package com.jagdish.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.RecipeDetailActivity;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.widget.RecipeWidgetUpdateService;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private static final String TAG = RecipeAdapter.class.getName();
    Context mContext;
    List<Recipe> recipeList;

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mRecipeImage;
        public final TextView mRecipeName;


        public RecipeAdapterViewHolder(View view) {
            super(view);
            mRecipeImage = (ImageView) view.findViewById(R.id.recipe_image);
            mRecipeName = (TextView) view.findViewById(R.id.recipe_name);
        }
    }

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.mContext = context;
        this.recipeList = recipeList;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterViewHolder, final int position) {

        final Recipe recipe = recipeList.get(position);

        if (recipe.getName() != null) {
            recipeAdapterViewHolder.mRecipeName.setText(recipe.getName());

//            if (recipe.getName().equalsIgnoreCase(mContext.getResources().getString(R.string.nutella_pie))) {
//                Picasso.with(mContext).load(R.drawable.nutella).placeholder(R.drawable.ic_thumbnails_loading).error(mContext.getResources().getDrawable(R.drawable.ic_thumbnails_no_image)).into(recipeAdapterViewHolder.mRecipeImage);
//            } else if (recipe.getName().equalsIgnoreCase(mContext.getResources().getString(R.string.brownies))) {
////                recipeAdapterViewHolder.mRecipeImage.setImageResource(R.drawable.brownie1);
//                Picasso.with(mContext).load(R.drawable.brownie1).placeholder(R.drawable.ic_thumbnails_loading).error(mContext.getResources().getDrawable(R.drawable.ic_thumbnails_no_image)).into(recipeAdapterViewHolder.mRecipeImage);
//
//            } else if (recipe.getName().equalsIgnoreCase(mContext.getResources().getString(R.string.yellow_cake))) {
////                recipeAdapterViewHolder.mRecipeImage.setImageResource(R.drawable.yellow_cake);
//                Picasso.with(mContext).load(R.drawable.yellow_cake2).placeholder(R.drawable.ic_thumbnails_loading).error(mContext.getResources().getDrawable(R.drawable.ic_thumbnails_no_image)).into(recipeAdapterViewHolder.mRecipeImage);
//
//            } else if (recipe.getName().equalsIgnoreCase(mContext.getResources().getString(R.string.cheescake))) {
////                recipeAdapterViewHolder.mRecipeImage.setImageResource(R.drawable.cheese_cake);
//                Picasso.with(mContext).load(R.drawable.cheese_cake).placeholder(R.drawable.ic_thumbnails_loading).error(mContext.getResources().getDrawable(R.drawable.ic_thumbnails_no_image)).into(recipeAdapterViewHolder.mRecipeImage);
//            }

        }

        recipeAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecipeWidgetUpdateService.startActionUpdateRecipe(mContext, recipe);

                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra("recipe", recipe);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == recipeList) return 0;
        return recipeList.size();
    }

}
