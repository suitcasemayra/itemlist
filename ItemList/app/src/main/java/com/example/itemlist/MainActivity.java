package com.example.itemlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itemlist.Adapter.ItemAdapter;
import com.example.itemlist.Model.ItemModel;
import com.example.itemlist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView itemRecyclerView;
    private ItemAdapter itemAdapter;
    private FloatingActionButton fab;

    private List<ItemModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        itemRecyclerView = findViewById(R.id.tasksRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(db,MainActivity.this);
        itemRecyclerView.setAdapter(itemAdapter);

        //for test only

        //ItemModel item = new ItemModel();
        //item.setItem("This is a test item.");
        //item.setStatus(0);
        //item.setId(1);

        //itemList.add(item);
        //itemList.add(item);
        //itemList.add(item);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(itemAdapter));
        itemTouchHelper.attachToRecyclerView(itemRecyclerView);

        fab = findViewById(R.id.fab);

        itemList = db.getAllItems();
        Collections.reverse(itemList);

        itemAdapter.setItem(itemList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem.newInstance().show(getSupportFragmentManager(), AddNewItem.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        itemList = db.getAllItems();
        Collections.reverse(itemList);
        itemAdapter.setItem(itemList);
        itemAdapter.notifyDataSetChanged();
    }
}