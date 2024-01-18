package com.example.itemlist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itemlist.AddNewItem;
import com.example.itemlist.MainActivity;
import com.example.itemlist.Model.ItemModel;
import com.example.itemlist.R;
import com.example.itemlist.Utils.DatabaseHandler;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemModel> itemList;
    private DatabaseHandler db;
    private MainActivity activity;

    public ItemAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ItemModel items = itemList.get(position);
        holder.item.setText(items.getItem());
        holder.item.setChecked(toBoolean(items.getStatus()));
        holder.item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(items.getId(), 1);
                } else {
                    db.updateStatus(items.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setItem(List<ItemModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ItemModel item = itemList.get(position);
        db.deleteItem(item.getId());
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ItemModel item = itemList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("item", item.getItem());
        AddNewItem fragment = new AddNewItem();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewItem.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox item;

        ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.itemCheckBox);
        }
    }
}

