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


public class IngradientsAdapter extends RecyclerView.Adapter<IngradientsAdapter.IngradientAdapterViewHolder> {

    private static final String TAG = IngradientsAdapter.class.getName();
    Context mContext;
    List<Ingredient> ingredientList;

    public class IngradientAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mGradientName;

        public IngradientAdapterViewHolder(View view) {
            super(view);
            mGradientName = (TextView) view.findViewById(R.id.ingradient_textview);
        }
    }

    public IngradientsAdapter(Context context, List<Ingredient> recipeList) {
        this.mContext = context;
        this.ingredientList = recipeList;
    }

    @Override
    public IngradientAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingradient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new IngradientAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(IngradientAdapterViewHolder ingradientAdapterViewHolder, final int position) {

        final Ingredient ingredient = ingredientList.get(position);

        if (ingredient.getName() != null) {
            String ingradientValue = String.format("%.1f %2s %3s", ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getName());
            ingradientAdapterViewHolder.mGradientName.setText(ingradientValue);
        }

        ingradientAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    @Override
    public int getItemCount() {
        if (null == ingredientList) return 0;
        return ingredientList.size();
    }

}
