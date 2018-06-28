package com.kewal.acute;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SearchReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_review);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Search Employee");
        }

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchReviewActivity.this, DisplayResultActivity.class));
            }
        });

        final TextView birthDate = findViewById(R.id.editDate);
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar currentCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                final Date date = new GregorianCalendar(year,month,day).getTime();

                int current_year = currentCalendar.get(Calendar.YEAR);

                int differenceYears = current_year - year;


                if (differenceYears < 18) {
                    Toast.makeText(SearchReviewActivity.this,"Age cannot be less than 18", Toast.LENGTH_SHORT).show();
                } else if (differenceYears > 60) {
                    Toast.makeText(SearchReviewActivity.this,"Age cannot be more than 60", Toast.LENGTH_SHORT).show();
                } else {
                    birthDate.setText(formatDate(date));
                }
            }

        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(SearchReviewActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SearchReviewActivity.this,
                R.array.city_array, android.R.layout.simple_dropdown_item_1line);

        AutoCompleteTextView textView = findViewById(R.id.autoCompleteTextView_city);
        textView.setAdapter(adapter);
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String birthDate = sdf.format(date);
        return birthDate;
    }
}
