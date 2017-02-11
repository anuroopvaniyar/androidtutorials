package com.anuroop.androidtutorials.mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

public class NewNoteActivity extends AppCompatActivity {
    private static final String TAG = NewNoteActivity.class.getSimpleName();
    private Integer index;
    private String initialNoteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        index = getIntent().getIntExtra("noteId", -1);
        Log.i(TAG, "index " + index);

        EditText notesText = (EditText) findViewById(R.id.newNote);

        if (index != -1) {
            notesText.setText(MainActivity.notesData.get(index));
            initialNoteData = MainActivity.notesData.get(index);
        }

        Button savebtn = (Button) findViewById(R.id.save);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotesDataChange();
                NewNoteActivity.this.finish();
            }
        });

        notesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.notesData.set(index, String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showSoftKeyboard(notesText);
    }

    /**
     * Save the changes made in the note
     */
    private void saveNotesDataChange() {
        SharedPreferences sharedPreferences = NewNoteActivity.this.getSharedPreferences("noteApp", MODE_PRIVATE);

        if (MainActivity.notesDataSet != null) {
            MainActivity.notesDataSet.clear();
        } else {
            MainActivity.notesDataSet = new HashSet<String>();
        }

        MainActivity.notesDataSet.addAll(MainActivity.notesData);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        sharedPreferences.edit().putStringSet("notesData", MainActivity.notesDataSet).apply();
        Toast.makeText(getApplicationContext(), "Note saved.. ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back")
                .setMessage("Are you sure you don't want to save the changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (initialNoteData != null) {
                            MainActivity.notesData.set(index, initialNoteData);
                            Toast.makeText(getApplicationContext(), "Note not saved.. ", Toast.LENGTH_SHORT).show();
                        }
                        NewNoteActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
