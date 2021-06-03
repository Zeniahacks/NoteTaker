package sg.edu.rp.c346.id19023702.notetaker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * The main activity is the main page of the application. The application will then allow the user
 * to add a note such as the title of the topic of what the user want to add and the description of what
 * the user want s to have in the description. Then, there is two buttons, save and delete, the user can
 * choose to save the note or delete the note that the user added. Editing also can be made to allow the
 * user to change the description in case of a mistake.
 */

public class MainActivity extends AppCompatActivity {

    // Request and result code constants.
    public static final int REQUEST_ADD_NOTE = 0;
    public static final int REQUEST_MODIFY_NOTE = 1;
    public static final int RESULT_SAVE_NOTE = 0;
    public static final int RESULT_DELETE_NOTE = 1;
    public static final int RESULT_DISCARD_NOTE = 2;

    // Intent extra constants.
    public static final String INTENT_EXTRA_KEY = "KEY";
    public static final String INTENT_EXTRA_TITLE = "TITLE";
    public static final String INTENT_EXTRA_DESCRIPTION = "DESCRIPTION";
    public static final String INTENT_EXTRA_TIME = "TIME";

    // Shared preference constants.
    public static final String SHARED_PREFERENCES_FILE_NAME = "SHARED_PREFERENCES";
    public static final String SHARED_PREFERENCES_STRING_JSON = "JSON";

    // List view and helper objects.
    private ArrayList<sg.edu.rp.c346.id19023702.notetaker.Note> notes;
    private sg.edu.rp.c346.id19023702.notetaker.NotesAdapter notesAdapter;
    private ListView listView;

    // Shared preference and helper objects.
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private String json;
    private Type type;

    /**
     * First function called when the activity executes. It sets up the list view and specifies what to
     * do in response to clicks.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Attempt to get array list from shared preferences. If shared preferences had no array
         * list, initialize as empty array list. */
        notes = getNotesFromSharedPreferences();

        if (notes == null)
            notes = new ArrayList<>();

        // Initialize adapter for list view.
        notesAdapter = new sg.edu.rp.c346.id19023702.notetaker.NotesAdapter(this, notes);

        // Initialize list view.
        listView = findViewById(R.id.list_view_notes);
        listView.setAdapter(notesAdapter);

        /* Specify list view clicks to start the detail activity with request code 1. Pass
         * attributes of the clicked note as intent extras to the activity. */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                sg.edu.rp.c346.id19023702.notetaker.Note selectedNote = (sg.edu.rp.c346.id19023702.notetaker.Note) notesAdapter.getItem(i);

                Intent intent = new Intent(MainActivity.this, sg.edu.rp.c346.id19023702.notetaker.DetailActivity.class);
                intent.putExtra(INTENT_EXTRA_KEY, selectedNote.getKey());
                intent.putExtra(INTENT_EXTRA_TITLE, selectedNote.getTitle());
                intent.putExtra(INTENT_EXTRA_DESCRIPTION, selectedNote.getDescription());
                intent.putExtra(INTENT_EXTRA_TIME, selectedNote.getTime());

                startActivityForResult(intent, REQUEST_MODIFY_NOTE);
            }
        });
    }
    /**
     * Is called when the activity leaves the foreground. It calls saveNotesToSharedPreferences(),
     * to save the current state of the array list to shared preferences.
     */
    @Override
    protected void onPause() {
        saveNotesToSharedPreferences();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_SAVE_NOTE) {
            if (requestCode == REQUEST_MODIFY_NOTE)
                deleteNote(data.getLongExtra(INTENT_EXTRA_KEY, -1));
            addNote(data.getStringExtra(INTENT_EXTRA_TITLE), data.getStringExtra(INTENT_EXTRA_DESCRIPTION), data.getStringExtra(INTENT_EXTRA_TIME));
            notesAdapter.notifyDataSetChanged();
        }

        // If the user was deleting an existing note, then delete it from the array list.
        else if (resultCode == RESULT_DELETE_NOTE && requestCode == REQUEST_MODIFY_NOTE) {
            deleteNote(data.getLongExtra(INTENT_EXTRA_KEY, -1));
            notesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // If add note action is pressed, start the detail activity with request code 0.
        if (id == R.id.action_add_note) {
            Intent intent = new Intent(MainActivity.this, sg.edu.rp.c346.id19023702.notetaker.DetailActivity.class);
            startActivityForResult(intent, REQUEST_ADD_NOTE);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNote(String title, String description, String time) {
        notes.add(new sg.edu.rp.c346.id19023702.notetaker.Note(
                System.currentTimeMillis(),
                title,
                description,
                time
        ));
    }

    public void deleteNote(long key) {
        Iterator<sg.edu.rp.c346.id19023702.notetaker.Note> iterator = notes.iterator();
        while(iterator.hasNext()) {
            sg.edu.rp.c346.id19023702.notetaker.Note note = iterator.next();
            if (note.getKey() == key) {
                iterator.remove();
                break;
            }
        }
    }

    public ArrayList<sg.edu.rp.c346.id19023702.notetaker.Note> getNotesFromSharedPreferences() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        gson = new Gson();
        json = sharedPreferences.getString(SHARED_PREFERENCES_STRING_JSON, null);
        type = new TypeToken<ArrayList<sg.edu.rp.c346.id19023702.notetaker.Note>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveNotesToSharedPreferences() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
        json = gson.toJson(notes);
        editor.putString(SHARED_PREFERENCES_STRING_JSON, json);
        editor.apply();
    }
}