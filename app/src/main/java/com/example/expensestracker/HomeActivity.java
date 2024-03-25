package com.example.expensestracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.expensestracker.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    private static  final String EXTRA_USER="com.example.expensestracker.user";
    public static Intent newIntent(Context packageContext, User userLoggedIn )
    {
        Intent intent = new Intent(packageContext, HomeActivity.class);
        intent.putExtra(EXTRA_USER, userLoggedIn.getUserId());
        intent.putExtra(EXTRA_USER, userLoggedIn.getUserEmail());
        intent.putExtra(EXTRA_USER, userLoggedIn.getUserPassword());
        intent.putExtra(EXTRA_USER, userLoggedIn.getUserName());
        return  intent;

    }

}