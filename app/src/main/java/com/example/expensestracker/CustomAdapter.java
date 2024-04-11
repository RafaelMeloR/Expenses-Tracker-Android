package com.example.expensestracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

// Custom Adapter Class
public class CustomAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mKeysList;
    private Map<String, Integer> mColorMap;
    private Map<String, Float> mValuesMap; // Assuming you have a map to store values

    public CustomAdapter(Context context, List<String> keysList, Map<String, Integer> colorMap, Map<String, Float> valuesMap) {
        super(context, android.R.layout.list_content, keysList);
        mContext = context;
        mKeysList = keysList;
        mColorMap = colorMap;
        mValuesMap = valuesMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Get the key
        String key = mKeysList.get(position);

        // Calculate the percentage
        float totalValue = 0;
        for (float value : mValuesMap.values()) {
            totalValue += value;
        }
        float percentage = (mValuesMap.get(key) / totalValue) * 100;

        // Set the text including percentage
        TextView textView = view.findViewById(android.R.id.text1);
        // Calculate the percentage with two decimal points
        String formattedPercentage = String.format("%.2f", percentage);

        // Set the text including percentage
        textView.setText(key + " (" + formattedPercentage + "%)");

        // Set the background color using color map
        Integer color = mColorMap.get(key);
        if (color != null) {
            textView.setTextColor(color);
        }

        return view;
    }
}