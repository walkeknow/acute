package com.kewal.acute;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmployeeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private static String emp_type;
    private static String id_type;
    private static String card_no;
    private static String emp_name;
    private static String emp_age;
    private static int card_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Add Employee");
            // actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Spinner job_spinner = (Spinner) findViewById(R.id.spinner);
        Spinner card_spinner = (Spinner) findViewById(R.id.spinner2);
        Button save_button = (Button) findViewById(R.id.button_save);
        final TextView birthDate = findViewById(R.id.editDate);
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar currentCalendar = Calendar.getInstance();



        save_button.setOnClickListener(this);
        job_spinner.setOnItemSelectedListener(this);
        card_spinner.setOnItemSelectedListener(this);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                final Date date = new GregorianCalendar(year,month,day).getTime();

                int current_year = currentCalendar.get(Calendar.YEAR);

                int age = current_year - year;


                if (age < 18) {
                    Toast.makeText(AddEmployeeActivity.this,"Age cannot be less than 18", Toast.LENGTH_SHORT).show();
                } else if (age > 60) {
                    Toast.makeText(AddEmployeeActivity.this,"Age cannot be more than 60", Toast.LENGTH_SHORT).show();
                } else {
                    birthDate.setText(formatDate(date));
                }
            }

        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddEmployeeActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        ArrayAdapter<CharSequence> job_adapter = ArrayAdapter.createFromResource(this,
                R.array.job_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> card_adapter = ArrayAdapter.createFromResource(this,
                R.array.card_array, android.R.layout.simple_spinner_item);

        job_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        job_spinner.setAdapter(job_adapter);

        card_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        card_spinner.setAdapter(card_adapter);

    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String birthDate = sdf.format(date);
        return birthDate;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                //Toast.makeText(AddEmployeeActivity.this, parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                emp_type = parent.getSelectedItem().toString();


            case R.id.spinner2:
                //Toast.makeText(AddEmployeeActivity.this, parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                id_type = parent.getSelectedItem().toString();

                card_id = (int) parent.getSelectedItemId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(AddEmployeeActivity.this, "Select at least one item", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_save) {
            EditText editText_card = findViewById(R.id.editText_card);
            EditText editText_name = findViewById(R.id.editText_name);
            TextView textView_birth_date = findViewById(R.id.editDate);

            String birth_date = null;

            if (textView_birth_date.getText() != null ) {
                birth_date = textView_birth_date.getText().toString();
            }

            String card = editText_card.getText().toString();
            String name = editText_name.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(AddEmployeeActivity.this, "Name should not be empty" ,Toast.LENGTH_SHORT).show();
            } else if (birth_date.isEmpty()) {
                Toast.makeText(AddEmployeeActivity.this, "Date of Birth should not be empty" ,Toast.LENGTH_SHORT).show();
            } else if (!ValidateDetails.isNameValid(name)) {
                Toast.makeText(AddEmployeeActivity.this, "Please enter a valid name",Toast.LENGTH_SHORT).show();
            } else  if (card.isEmpty()) {
                startActivity(new Intent(AddEmployeeActivity.this, AddDetails.class));
            } else {
                //TODO: copy below statements in else block
                card_no = editText_card.getText().toString();
                emp_name = editText_name.getText().toString();
                boolean validated = true;

                switch(card_id) {
                    case 0:
                        if(!ValidateDetails.isPanValid(card_no)) {
                            Toast.makeText(AddEmployeeActivity.this, "Invalid Pan Card number entered" ,Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                        break;
                    case 1:
                        if(!ValidateDetails.isAadharValid(card_no)) {
                            Toast.makeText(AddEmployeeActivity.this, "Invalid Aadhar Card number entered" ,Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                        break;
                    case 2:
                        if(!ValidateDetails.isVoterValid(card_no)) {
                            Toast.makeText(AddEmployeeActivity.this, "Invalid Voter ID Card number entered" ,Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                        break;
                    case 3:
                        if(!ValidateDetails.isDriverValid(card_no)) {
                            Toast.makeText(AddEmployeeActivity.this, "Invalid Driving Licence number entered" ,Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                        break;
                    case 4:
                        if(!ValidateDetails.isPassportValid(card_no)) {
                            Toast.makeText(AddEmployeeActivity.this, "Invalid Passport number entered" ,Toast.LENGTH_SHORT).show();
                            validated = false;
                        }
                    default:
                        break;
                }
                if(validated) {
                    startActivity(new Intent(AddEmployeeActivity.this, AddDetails.class));
                }
            }

            // Toast.makeText(AddEmployeeActivity.this, String.valueOf(card_no) ,Toast.LENGTH_SHORT).show();
            //(new Intent(AddEmployeeActivity.this, AddDetails.class));
        }
    }
}

