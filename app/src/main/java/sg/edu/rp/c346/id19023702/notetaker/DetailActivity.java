package sg.edu.rp.c346.id19023702.notetaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Note selectedNote;
    private EditText editTextTitle, editTextDescription;
    private Button buttonDelete, buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Set action bar title as empty string.
        getSupportActionBar().setTitle("");

        // Get attributes of the selected note.
        Intent intent = getIntent();
        selectedNote = new Note(
                intent.getLongExtra(MainActivity.INTENT_EXTRA_KEY, -1),
                intent.getStringExtra(MainActivity.INTENT_EXTRA_TITLE),
                intent.getStringExtra(MainActivity.INTENT_EXTRA_DESCRIPTION)
        );

        // Initialize layout elements.
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonDelete = findViewById(R.id.button_delete_note);
        buttonSave = findViewById(R.id.button_save_note);

        // Set text as attributes of the selected note object.
        editTextTitle.setText(selectedNote.getTitle());
        editTextDescription.setText(selectedNote.getDescription());

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.INTENT_EXTRA_KEY, selectedNote.getKey());
                setResult(MainActivity.RESULT_DELETE_NOTE, intent);
                finish();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.INTENT_EXTRA_KEY, selectedNote.getKey());
                intent.putExtra(MainActivity.INTENT_EXTRA_TITLE, editTextTitle.getText().toString());
                intent.putExtra(MainActivity.INTENT_EXTRA_DESCRIPTION, editTextDescription.getText().toString());
                setResult(MainActivity.RESULT_SAVE_NOTE, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(MainActivity.RESULT_DISCARD_NOTE);
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}