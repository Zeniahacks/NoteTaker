package sg.edu.rp.c346.id19023702.notetaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
                Toast.makeText(getApplicationContext(),
                        "Deleted Note. Make a new Note!",
                        Toast.LENGTH_LONG)
                        .show();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.INTENT_EXTRA_KEY, selectedNote.getKey());
                    intent.putExtra(MainActivity.INTENT_EXTRA_TITLE, editTextTitle.getText().toString());
                    intent.putExtra(MainActivity.INTENT_EXTRA_DESCRIPTION, editTextDescription.getText().toString());
                    setResult(MainActivity.RESULT_SAVE_NOTE, intent);
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Note has been Saved. Make a new Note!",
                            Toast.LENGTH_LONG)
                            .show();
                    clearAllFields();
                }
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

    private boolean checkAllFields() {
        ArrayList<String> problems = new ArrayList<>();
        if (editTextTitle.getText().toString().trim().isEmpty()) {
            problems.add("Title");
        }
        if (editTextDescription.getText().toString().trim().isEmpty()) {
            problems.add("Description");
        }
        if (problems.size() > 0) {
            String message = "Ensure that: ";

            if (problems.size() == 1) {
                message += problems.get(0) + " ";
            }
            else if (problems.size() > 1) {
                for (int i = 0; i < problems.size(); i++) {
                    if (i == problems.size() - 1) {
                        message += "and " + problems.get(i) + " ";
                    }
                    else {
                        message += problems.get(i) + " ";
                    }
                }
            }

            message += "are filled/selected correctly.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        return problems.isEmpty();
    }

    private void clearAllFields() {
        editTextTitle.setText("");
        editTextDescription.setText("");
    }
}