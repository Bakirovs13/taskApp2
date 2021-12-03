package kg.geektech.taskapp36.ui.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentDashboardBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;
import kg.geektech.taskapp36.ui.fragments.home.TaskAdapter;

public class DashboardFragment extends Fragment {


    private FragmentDashboardBinding binding;
    private TaskAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                 updateTask(adapter.getItem(position));

            }

            @Override
            public void onLongClick(int position) {

                deleteTask(position);

            }
        });
    }

    private void deleteTask(int position) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(adapter.getItem(position).getDocId())
                .delete();
        adapter.removeItem(position);
    }

    private void updateTask(Task task) {
       Bundle bundle = new Bundle();
       bundle.putSerializable("key",task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.taskFragment,bundle);
    }





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);
       // getDataLive(); //метод для запроса данных с сервера

        getData();
    }

    private void getDataLive() {
        FirebaseFirestore
                .getInstance()
                .collection("tasks")
                .orderBy("createdAt",Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Task> list =  new ArrayList<>();
                        for (DocumentSnapshot snapshot: value){
                            Task task  = snapshot.toObject(Task.class);
                            task.setDocId(snapshot.getId());
                            list.add(task);
                        }
//                        List<Task> list = snapshots.toObjects(Task.class);
                        adapter.addItems(list);

                    }
                });

    }

    private void getData() {
        FirebaseFirestore
                .getInstance()
                .collection("tasks")
                .orderBy("createdAt", Query.Direction.DESCENDING)//sorting list - lat added task will be shown in the top
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        
                        List<Task> list =  new ArrayList<>();
                        for (DocumentSnapshot snapshot: snapshots){
                            Task task  = snapshot.toObject(Task.class);
                            task.setDocId(snapshot.getId());
                            list.add(task);
                        }
//                        List<Task> list = snapshots.toObjects(Task.class);
                        adapter.addItems(list);

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}