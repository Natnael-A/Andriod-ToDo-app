package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_Position = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button addButton;
    EditText etItem;
    RecyclerView rvItems;
    ItemAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItem);

        System.out.println("hello");

        loadItems();

        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItem();
            }
        };
        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener(){

            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at postition"+ position);
                //create the new activity(intents are like requests, they are used to reach or get things Ex: new page or urls...
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_Position, position);
                //display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdapter = new ItemAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //add item to the model
                items.add(todoItem);
                //Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItem();
            }
        });
    }
    //this will handle the result of the edit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_Position);

            //Update the model at the right position with new item text
            items.set(position, itemText);
            //Notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItem();
            Toast.makeText(getApplicationContext(), "Items Updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This method will load items by reading every line of the data file

    private void loadItems(){
        try {
              items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
         }catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
         }
    }
    //THis method saves items by writing them into the data file
    private void saveItem(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}
