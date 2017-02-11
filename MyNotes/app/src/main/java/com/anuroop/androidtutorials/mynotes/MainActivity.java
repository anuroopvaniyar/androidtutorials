package com.anuroop.androidtutorials.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    static ArrayAdapter arrayAdapter;
    static List<String> notesData = new ArrayList<String>();
    static Set<String> notesDataSet = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout addNew = (FrameLayout) findViewById(R.id.add);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("noteApp", MODE_PRIVATE);
        notesDataSet = sharedPreferences.getStringSet("notesData", null);

        notesData.clear();

        if (notesDataSet != null) {
            notesData.addAll(notesDataSet);
        }

        ListView notesListView = (ListView) findViewById(R.id.notesList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesData);
        notesListView.setAdapter(arrayAdapter);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesData.add(" ");
                if (notesDataSet != null) {
                    notesDataSet.clear();
                } else {
                    notesDataSet = new HashSet<String>();
                }
                notesDataSet.addAll(notesData);
                sharedPreferences.edit().putStringSet("notesData", notesDataSet).apply();
                editANote(notesData.size() - 1);
            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editANote(position);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesData.remove(position);
                                if (notesDataSet != null) {
                                    notesDataSet.clear();
                                } else {
                                    notesDataSet = new HashSet<String>();
                                }
                                notesDataSet.addAll(notesData);
                                sharedPreferences.edit().putStringSet("notesData", notesDataSet).apply();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });
    }

    /**
     * This method is called when a new note is created or an existing note is opened to edit or see
     *
     * @param position the position of the item clicked
     */
    private void editANote(int position) {
        Intent i = new Intent(MainActivity.this, NewNoteActivity.class);
        i.putExtra("noteId", position);
        startActivity(i);
    }

}
