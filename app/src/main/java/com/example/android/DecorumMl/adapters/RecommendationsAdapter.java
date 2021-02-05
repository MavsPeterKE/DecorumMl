package com.example.android.DecorumMl.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.DecorumMl.R;
import com.example.android.DecorumMl.models.Furniture;

import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.MyViewHolder> {

    private Context context;
    private List<Furniture> furnitureList;
    private static final String LOG_TAG = RecommendationsAdapter.class.getSimpleName();

    public RecommendationsAdapter(Context context, List<Furniture> furnitureList) {
        super();
        this.context = context;
        this.furnitureList = furnitureList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cat_item_layout, parent, false);


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Furniture furniture= furnitureList.get(position);
        holder.tvName.setText(furniture.name);
        holder.tvDescription.setText(furniture.decription);
        holder.tvCash.setText(String.valueOf(furniture.amount));
        holder.imageView.setImageResource(furniture.imageId);
    }


    @Override
    public int getItemCount() {
        if (furnitureList.size() == 0) {
            return 0;
        }else {
            return furnitureList.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDescription;
        TextView tvCash;
        ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFurnitureName);
            tvDescription = itemView.findViewById(R.id.tvItemdesc);
            tvCash = itemView.findViewById(R.id.tvCash);
            imageView = itemView.findViewById(R.id.imDesign);
        }
    }



}
