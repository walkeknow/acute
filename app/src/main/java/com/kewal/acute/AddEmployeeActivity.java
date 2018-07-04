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

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEmployeeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private static String emp_type;
    private static String id_type;
    private static String card_no;
    private static String emp_name;
    private static String emp_age;
    private static int card_id;
    private static String card_type;
    private static String birthDate;
    private static String rating;
    private static String cityId;
    private static String comment;
    private static String photo;
    private static String bossId;


    DatabaseReference databaseReferenceEmployees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Add Employee");
            // actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Spinner job_spinner = (Spinner) findViewById(R.id.spinnerEmployee);
        Spinner card_spinner = (Spinner) findViewById(R.id.spinnerCard);
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

                //databaseReferenceEmployees = FirebaseDatabase.getInstance().getReference("Employees");
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
            case R.id.spinnerEmployee:
                //Toast.makeText(AddEmployeeActivity.this, parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                emp_type = parent.getSelectedItem().toString();
                break;

            case R.id.spinnerCard:
                //Toast.makeText(AddEmployeeActivity.this, parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                id_type = parent.getSelectedItem().toString();
                card_type = parent.getSelectedItem().toString();
                card_id = (int) parent.getSelectedItemId();
                break;
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

            String card_no = editText_card.getText().toString().trim();
            String name = editText_name.getText().toString().trim();
            String id = null;

            if (name.isEmpty()) {
                Toast.makeText(AddEmployeeActivity.this, "Name should not be empty" ,Toast.LENGTH_SHORT).show();
            } else if (birth_date.isEmpty()) {
                Toast.makeText(AddEmployeeActivity.this, "Date of Birth should not be empty" ,Toast.LENGTH_SHORT).show();
            } else if (!ValidateDetails.isNameValid(name)) {
                Toast.makeText(AddEmployeeActivity.this, "Please enter a valid name",Toast.LENGTH_SHORT).show();
            } else  if (card_no.isEmpty()) {
                card_type = "N/A";
                card_no = "N/A";
                rating = "N/A";
                Employee employee = new Employee(
                    id,
                    name,
                    emp_type,
                    card_no,
                    card_type,
                    birth_date,
                    rating,
                    cityId,
                    comment,
                    photo,
                    bossId
                );

                Intent intent = new Intent(AddEmployeeActivity.this, AddDetails.class);
                intent.putExtra("EmpObj", employee);
                startActivity(intent);

            } else {
                //TODO: copy below statements in else block
                card_no = editText_card.getText().toString();
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

                    /*String id = databaseReferenceEmployees.getKey();
                    final Employee employee = new Employee();

                    employee.setEmpId(id);
                    employee.setEmpName(name);
                    employee.setCardNo(card_no);
                    employee.setCardType(card_type);
                    employee.setBirthDate(birth_date);
                    employee.setRating(rating);
                    employee.setCityId(cityId);
                    employee.setComment(comment);
                    employee.setPhoto(photo);
                    employee.setBossId(bossId);*/

                    /*databaseReferenceEmployees.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReferenceEmployees.child("User01").setValue(employee);
                            startActivity(new Intent(AddEmployeeActivity.this, AddDetails.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                }
            }

            // Toast.makeText(AddEmployeeActivity.this, String.valueOf(card_no) ,Toast.LENGTH_SHORT).show();
            //(new Intent(AddEmployeeActivity.this, AddDetails.class));
        }
    }
}

