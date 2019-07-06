package com.niki.wihealth.adapter;

import android.content.Context;
import java.lang.Math;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.niki.wihealth.DataPassing;
import com.niki.wihealth.DetailPage;
import com.niki.wihealth.R;
import com.niki.wihealth.Utility;
import com.niki.wihealth.model.SportCenter;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SportCenter> sportCenters;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Utility util = new Utility();
    private DataPassing dataPassing = DataPassing.getInstance();

    public CategoryListAdapter(Context context, ArrayList<SportCenter> sportCenters){
        this.context = context;
        this.sportCenters = sportCenters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvSearchName.setText(sportCenters.get(position).getName() + " (" + sportCenters.get(position).getDistance() + ") km");
        holder.tvSearchCategory.setText(util.capitalizeString(sportCenters.get(position).getCategory()));
        holder.tvSearchCount.setText("" + sportCenters.get(position).getCount() + " person(s)");
        dataPassing.setSportCenter(sportCenters.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataPassing.setSportCenter(sportCenters.get(position));
                context.startActivity(new Intent(context, DetailPage.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportCenters.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tvSearchName, tvSearchCount, tvSearchCategory;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvSearchName = itemView.findViewById(R.id.tv_search_name);
            tvSearchCount = itemView.findViewById(R.id.tv_search_count);
            tvSearchCategory = itemView.findViewById(R.id.tv_search_category);
            cardView = itemView.findViewById(R.id.cv_search_list_card);
        }
    }
}
