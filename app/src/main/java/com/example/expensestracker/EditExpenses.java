package com.example.expensestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.expensestracker.models.Expenses;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditExpenses extends AppCompatActivity {
    private EditText Expense;
    private EditText CalendarView;
    private DatePickerDialog datePickerDialog;
    private Spinner categoriesSpinner;
    Button updateExpense;
    Button deleteExpense;
    private String location;


    private static  final String EXTRA_EXPENSES_ID="com.example.expensestracker.expenses_id";

    public static Intent newIntent(Context packageContext, String expenses_id)
    {
        Intent intent = new Intent(packageContext, EditExpenses.class);
        intent.putExtra(EXTRA_EXPENSES_ID, expenses_id);
        return  intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expenses);

        //get the view of toolbarCourse
        Toolbar toolbar = findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbar);

        //Decoding the extra data from the intent object
        String intentIDExpenses = getIntent().getStringExtra(EXTRA_EXPENSES_ID);
        // initiate the date picker and a button
        CalendarView = (EditText) findViewById(R.id.calendarEditExpenses);
        // perform click event on edit text
        CalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EditExpenses.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                CalendarView.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Expense=(EditText) findViewById(R.id.editTextExpenseEditExpenses);

        //get view spinnerCategory
        categoriesSpinner = (Spinner) findViewById(R.id.spinnerCategoryEditExpenses);
        // Create a list to store the names of publishers
        ArrayList<String> CategoriesList = new ArrayList<>();
        CategoriesList.add("Select Category"); // Placeholder text
        //Read from firebase database and writing in a scrollTextView
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CategoriesList.clear();
                //Read all content of courseModalArrayList
                for (DataSnapshot ss : snapshot.getChildren()) {
                    CategoriesList.add(ss.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // Create the ArrayAdapter using the list of publisher names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditExpenses.this, android.R.layout.simple_spinner_item, CategoriesList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        //getting data from database and populating fields
        DatabaseReference databaseRefGet = FirebaseDatabase.getInstance().getReference();
        databaseRefGet.child("Expenses").orderByChild("id").equalTo(intentIDExpenses).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ss : snapshot.getChildren()) {
                    String output=ss.getValue(Expenses.class).getCategory()+": $"+ss.getValue(Expenses.class).getExpense().toString()+" \n"+ss.getValue(Expenses.class).getDateTime();
                    CalendarView.setText(ss.getValue(Expenses.class).getDateTime());
                    Expense.setText(ss.getValue(Expenses.class).getExpense().toString());
                    location=ss.getValue(Expenses.class).getLocation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateExpense = (Button) findViewById(R.id.updateExpenseEditExpenses);
        updateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = CalendarView.getText().toString();
                Double exp = Double.parseDouble(Expense.getText().toString());
                String cat = categoriesSpinner.getSelectedItem().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Expenses expenseNew = new Expenses(date,location,exp,cat,user.getUid().toString());
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                expenseNew.setId(intentIDExpenses);
                HashMap hm= new HashMap<>();
                hm.put(intentIDExpenses,expenseNew);
                databaseRef.child("Expenses").updateChildren(hm)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditExpenses.this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                                // Optionally, you can finish the activity or perform any other action after successful deletion
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditExpenses.this, "Failed to delete expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });}
        });

        deleteExpense = (Button) findViewById(R.id.deleteExpenseEditExpenses);
        deleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentIDExpenses != null && !intentIDExpenses.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Expenses").child(intentIDExpenses).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditExpenses.this, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Optionally, you can finish the activity or perform any other action after successful deletion
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditExpenses.this, "Failed to delete expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(EditExpenses.this, "No expense selected to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        Intent intent;

        if(id == R.id.navigation_logout)
        {
            Toast.makeText(EditExpenses.this, "Signed Out", Toast.LENGTH_SHORT).show();
            //Start new activity courseMapActivity
            FirebaseAuth.getInstance().signOut();
            intent = new Intent( EditExpenses.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(EditExpenses.this, "Logout", Toast.LENGTH_SHORT).show();
            return  true;
        } else if (id==R.id.navigation_education) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=-M1iMS9WM6U"));
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}