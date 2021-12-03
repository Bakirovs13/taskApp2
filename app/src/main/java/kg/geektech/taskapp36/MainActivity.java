package kg.geektech.taskapp36;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import kg.geektech.taskapp36.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.profile_notifications)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
       Prefs prefs = new Prefs(this);
        if (FirebaseAuth.getInstance().getCurrentUser() == null){  //if person is not signed
            // show login fragment
            navController.navigate(R.id.loginFragment);
        }
        if (!prefs.isBoardShown())
        navController.navigate(R.id.boardFragment);


       navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
           @Override
           public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
               if (destination.getId() == R.id.navigation_home||
                       destination.getId() == R.id.navigation_dashboard||
                       destination.getId() == R.id.navigation_notifications||
                       destination.getId() == R.id.profile_notifications
               ){
                   binding.navView.setVisibility(View.VISIBLE);
               }else{
                   binding.navView.setVisibility(View.GONE);
               }
               if (destination.getId()==R.id.boardFragment){
                   getSupportActionBar().hide();
               }else {
                   getSupportActionBar().show();
               }
           }
       });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();  // метод , для того чтобы стрелка в action bar работала

    }
}