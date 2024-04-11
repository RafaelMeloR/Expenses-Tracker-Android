package com.example.expensestracker.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensestracker.CustomAdapter;
import com.example.expensestracker.EditExpenses;
import com.example.expensestracker.MainActivity;
import com.example.expensestracker.R;
import com.example.expensestracker.databinding.FragmentDashboardBinding;
import com.example.expensestracker.models.Expenses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.LegendModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private GridView gridExpenses;
    private GridView gridCategories;
    PieChart pieChart;
    Context context;
    HashMap<String, Float> chartHM;
    DatabaseReference databaseRef;

    private FragmentDashboardBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        context = getContext().getApplicationContext();
        chartHM = new HashMap<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        //Chart
        pieChart = v.findViewById(R.id.piechart);
        //get the view of gridCategories
        gridCategories = (GridView) v.findViewById(R.id.gridCategories);
        //get the view of gridExpenses
        gridExpenses = (GridView) v.findViewById(R.id.gridExpenses);
        ArrayList<String> expensesList = new ArrayList<String>() ;
        ArrayList<String> idList = new ArrayList<String>() ;
        //Read from firebase database and writing in a scrollTextView
        String userId = FirebaseAuth.getInstance().getUid().toString();
        databaseRef.child("Expenses").orderByChild("userID").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Clear the existing list before populating with new data
                expensesList.clear();

                for(DataSnapshot ss : snapshot.getChildren()) {
                    String output=ss.getValue(Expenses.class).getCategory()+": $"+ss.getValue(Expenses.class).getExpense().toString()+" \n"+ss.getValue(Expenses.class).getDateTime();
                    expensesList.add(output);
                    idList.add(ss.getKey());
                    chartData(ss.getValue(Expenses.class).getCategory(),ss.getValue(Expenses.class).getExpense().floatValue());
                }

                // After populating expensesList, initialize ArrayAdapter and set it to the GridView
                ArrayAdapter<String> adapterType = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, expensesList);
                gridExpenses.setAdapter(adapterType);
                gridExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clickedItemId = idList.get(position);
                        Intent intent = EditExpenses.newIntent(getActivity(),clickedItemId);
                        startActivity(intent);
                    }
                });
                generateChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database query cancelled.", error.toException());
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void chartData(String category,float expense)
    {
        float temp;
        if(chartHM.isEmpty())
        {
            chartHM.put(category,expense);
        }
        else if (chartHM.containsKey(category))
        {
            temp=(chartHM.get(category) + expense);
            chartHM.put(category, temp);
        }
        else {
            chartHM.put(category, expense);
        }
    }
    public void generateChart()
    {
        // Create a color map to store colors for each key
        Map<String, Integer> colorMap = new HashMap<>();

        for (Map.Entry<String, Float> entry: chartHM.entrySet()) {
            // Generate a random color value
            int randomColorValue = (int) (Math.random() * 0x1000000);
            // Create a color using the generated random color value
            int color = Color.rgb(Color.red(randomColorValue), Color.green(randomColorValue), Color.blue(randomColorValue));
            colorMap.put(entry.getKey(), color);
            pieChart.addPieSlice(new PieModel(""+entry.getKey(),entry.getValue(), color));

        }
        List<String> keysList = new ArrayList<>(chartHM.keySet());
        // Create a custom adapter for GridView
        CustomAdapter adapterType = new CustomAdapter(getActivity(), keysList, colorMap,chartHM);
        gridCategories.setAdapter(adapterType);

        pieChart.startAnimation();

    }


}