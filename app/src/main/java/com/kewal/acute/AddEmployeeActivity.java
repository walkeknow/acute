package com.kewal.acute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEmployeeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private static String emp_type;
    private static String id_type;
    private static long card_no;
    private static String emp_name;
    private static int emp_age;

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

        save_button.setOnClickListener(this);
        job_spinner.setOnItemSelectedListener(this);
        card_spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> job_adapter = ArrayAdapter.createFromResource(this,
                R.array.job_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> card_adapter = ArrayAdapter.createFromResource(this,
                R.array.card_array, android.R.layout.simple_spinner_item);

        job_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        job_spinner.setAdapter(job_adapter);

        card_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        card_spinner.setAdapter(card_adapter);

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
            EditText editTextAge = findViewById(R.id.editTextAge);

            String card = editText_card.getText().toString();
            String age = editTextAge.getText().toString();
            String name = editText_name.getText().toString();

            if (card.isEmpty() || age.isEmpty() || name.isEmpty()) {
                Toast.makeText(AddEmployeeActivity.this, "Fields should not be empty" ,Toast.LENGTH_SHORT).show();

            } else {
                //TODO: copy below statements in else block
                card_no = Long.parseLong(card);
                emp_age = Integer.parseInt(age);
                emp_name = editText_name.getText().toString();

                if(emp_age < 18) {
                    Toast.makeText(AddEmployeeActivity.this, "Legal working age in India is above 18 years of age" ,Toast.LENGTH_SHORT).show();
                } else if (emp_age > 60) {
                    Toast.makeText(AddEmployeeActivity.this, "Legal working age in India is below 60 years of age" ,Toast.LENGTH_SHORT).show();
                }
            }

            // Toast.makeText(AddEmployeeActivity.this, String.valueOf(card_no) ,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddEmployeeActivity.this, AddDetails.class));
        }
    }
}

