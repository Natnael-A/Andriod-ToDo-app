package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.OneShotPreDrawListener;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//This class will essentially display data from the model into the row in the recycler view
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }
    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListner) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this layout inflator is used to infate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
       //wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    //binds data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //Grab the item at the position
        String item = items.get(position);
    //Bind the item into the specified view holder
        holder.bind(item);
    }

    //Tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each row in the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update the view inside the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    // Notifying the lister of the position that was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
