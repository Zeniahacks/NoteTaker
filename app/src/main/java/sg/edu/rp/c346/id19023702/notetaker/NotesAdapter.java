package sg.edu.rp.c346.id19023702.notetaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<Note> {

    private ArrayList<Note> notes;
    private Context context;

    public NotesAdapter(@NonNull Context context, @NonNull ArrayList<Note> objects) {
        super(context, 0, objects);
        // Store the module that is passed to this adapter
        notes = objects;
        // Store Context object as we would need to use it later
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the ith note object.
        Note note = getItem(position);

        // Specify the layout file.
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_note, parent, false);

        // Initialize layout elements.
        TextView textViewTitle = convertView.findViewById(R.id.text_view_title);
        TextView textViewDescription = convertView.findViewById(R.id.text_view_description);

        // Set text as attributes of the note object.
        textViewTitle.setText(note.getTitle());
        textViewDescription.setText(note.getDescription());

        return convertView;
    }
}