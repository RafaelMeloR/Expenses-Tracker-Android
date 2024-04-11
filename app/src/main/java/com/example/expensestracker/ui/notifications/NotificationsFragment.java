package com.example.expensestracker.ui.notifications;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.getSystemService;

import static java.time.LocalTime.now;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensestracker.EditExpenses;
import com.example.expensestracker.HomeActivity;
import com.example.expensestracker.R;
import com.example.expensestracker.databinding.FragmentNotificationsBinding;
import com.example.expensestracker.models.Categories;
import com.example.expensestracker.models.Expenses;
import com.example.expensestracker.models.Notifications;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsFragment extends Fragment {


    private TextView welcome;
    private EditText categoryEditText;
    private Button addCategory;
    private Button removeCategory;
    private GridView NotificationsGrid;

    private FragmentNotificationsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }
        makeNotification();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        //get view CategoryEditText
        categoryEditText = (EditText) v.findViewById(R.id.editTextCategory);

        //get view addCategory
        addCategory=(Button) v.findViewById(R.id.AddCategory);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = categoryEditText.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                HashMap hm= new HashMap<>();
                hm.put(cat,cat);
                databaseRef.child("Categories").updateChildren(hm);
                Toast.makeText(getContext(), "Added to firebase", Toast.LENGTH_SHORT).show();
            }
        });

        //get view removeCategory
        removeCategory=(Button) v.findViewById(R.id.RemoveCateogry);
        removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!categoryEditText.getText().toString().isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Categories").child(categoryEditText.getText().toString()).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Category deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Optionally, you can finish the activity or perform any other action after successful deletion
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed to delete category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "No category selected to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get the view gridNotifications
        NotificationsGrid =(GridView) v.findViewById(R.id.gridNotifications);
        ArrayList<String> notificationsList = new ArrayList<String>() ;
        ArrayList<String> idList = new ArrayList<String>() ;
        String userId = FirebaseAuth.getInstance().getUid().toString();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("Notifications").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Clear the existing list before populating with new data
                notificationsList.clear();

                for(DataSnapshot ss : snapshot.getChildren()) {
                    if(ss.getValue(Notifications.class).getStatus()==true) {
                        String output = ss.getValue(Notifications.class).getTitle() + "\n" + ss.getValue(Notifications.class).getBody().toString() + " \n" + ss.getValue(Notifications.class).getDate();
                        notificationsList.add(output);
                        idList.add(ss.getKey());
                    }
                  }

                // After populating expensesList, initialize ArrayAdapter and set it to the GridView
                if (getContext() != null) {
                    ArrayAdapter<String> adapterType = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, notificationsList);
                    NotificationsGrid.setAdapter(adapterType);
                } else {
                    Log.e(TAG, "Context is null");
                }

                NotificationsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clickedItemId = idList.get(position);
                        String date = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            date = now().toString();
                        }
                        String Title ="Reminder";
                        String Body = "Do you have expenses to register?";
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Notifications notifNew = new Notifications(Title,Body,date,false,user.getUid().toString());
                        HashMap hm= new HashMap<>();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            hm.put(notifNew.getTitle().toString()+""+user.getUid().toString()+""+ LocalDate.now(),notifNew);
                        }
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                        databaseRef.child("Notifications").updateChildren(hm);
                        Toast.makeText(getContext(), "Readed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database query cancelled.", error.toException());
            }
        });


        //get the view of toolbarCourse
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbarDashboard);

        AppCompatActivity activity =(AppCompatActivity) getActivity() ;
        activity.setSupportActionBar(toolbar);

        //get the view of welcome
        welcome = (TextView) v.findViewById(R.id.welcome);
        welcome.setText("Welcome, "+ FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void makeNotification()
    {
        String CHANNELID="CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getActivity(),
                        CHANNELID
                );
        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24).
                setContentTitle("Reminder")
                .setContentText("Do you have expenses to register?")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent= new Intent(getActivity(),HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager nm =(NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=nm.getNotificationChannel(CHANNELID);
            if(notificationChannel==null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel=new NotificationChannel(CHANNELID,
                        "Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                nm.createNotificationChannel(notificationChannel);
            }
        }
        nm.notify(0,builder.build());

        String date = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = now().toString();
        }
        String Title ="Reminder";
        String Body = "Do you have expenses to register?";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Notifications notifNew = new Notifications(Title,Body,date,true,user.getUid().toString());
        HashMap hm= new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hm.put(notifNew.getTitle().toString()+""+user.getUid().toString()+""+ LocalDate.now(),notifNew);
        }
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("Notifications").updateChildren(hm);

    }
}