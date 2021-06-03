package sg.edu.rp.c346.id19023702.notetaker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Note selectedNote;
    EditText editTextTitle, editTextDescription;
    Button buttonDelete, buttonSave, buttonAlarm;
    TextView tvTime;

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
                intent.getStringExtra(MainActivity.INTENT_EXTRA_DESCRIPTION),
                intent.getStringExtra(MainActivity.INTENT_EXTRA_TIME)
        );

        // Initialize layout elements.
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonDelete = findViewById(R.id.button_delete_note);
        buttonSave = findViewById(R.id.button_save_note);
        buttonAlarm = findViewById(R.id.button_alarm);
        tvTime = findViewById(R.id.text_view_time);

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.INTENT_EXTRA_KEY, selectedNote.getKey());
                    intent.putExtra(MainActivity.INTENT_EXTRA_TITLE, editTextTitle.getText().toString());
                    intent.putExtra(MainActivity.INTENT_EXTRA_DESCRIPTION, editTextDescription.getText().toString());
                    intent.putExtra(MainActivity.INTENT_EXTRA_TIME, buttonAlarm.getText().toString());
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

        final Calendar newCalender = Calendar.getInstance();
        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                DatePickerDialog dialog = new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(DetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis() > 0)
                                    buttonAlarm.setText(newDate.getTime().toString());
                                else
                                    Toast.makeText(DetailActivity.this,"Invalid time",Toast.LENGTH_SHORT).show();

                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();
                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DetailActivity.this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startAlarm(c);
        }
    }
}