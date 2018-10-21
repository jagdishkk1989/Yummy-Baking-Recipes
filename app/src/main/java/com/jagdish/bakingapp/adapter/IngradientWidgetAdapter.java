package com.jagdish.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.data.Ingredient;

import java.util.List;


public class IngradientWidgetAdapter extends RecyclerView.Adapter<IngradientWidgetAdapter.RecipeAdapterViewHolder> {

    private static final String TAG = IngradientWidgetAdapter.class.getName();
    private Context mContext;
    private List<Ingredient> ingredientList;

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView ingradient_textview;


        public RecipeAdapterViewHolder(View view) {
            super(view);
            ingradient_textview = (TextView) view.findViewById(R.id.widget_item_textview);
        }
    }

    public IngradientWidgetAdapter(Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.ingredientList = ingredientList;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.widget_ingradient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterViewHolder, final int position) {

        final Ingredient ingredient = ingredientList.get(position);

        StringBuilder ingredientString = new StringBuilder();
        double quantity;
        String ingredientName;
        String measure;

        if (ingredient != null) {
            ingredientName = ingredient.getName();
            quantity = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            ingredientString.append("\u25CF ");
            ingredientString.append(ingredientName);
            ingredientString.append(" (");
            ingredientString.append(quantity);
            ingredientString.append(" ");
            ingredientString.append(measure);
            ingredientString.append(")");
        }

        String strIngradient = ingredientString.toString();

    }

    @Override
    public int getItemCount() {
        if (null == ingredientList) return 0;
        return ingredientList.size();
    }

}
