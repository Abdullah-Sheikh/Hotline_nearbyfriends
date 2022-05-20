package com.findnearby.hotline.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findnearby.hotline.R;


public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> {



    Context context;
    private int serialCount =1;


    public PeopleAdapter(Context context) {

        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.peoples_recycler_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return 8;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView serialNo,name;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's

        }
    }
}
