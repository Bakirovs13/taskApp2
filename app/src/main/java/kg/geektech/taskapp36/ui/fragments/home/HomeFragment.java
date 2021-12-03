package kg.geektech.taskapp36.ui.fragments.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

import kg.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentHomeBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private TaskAdapter adapter;
    private Task task;
    private int pos ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);   //menu
        adapter = new TaskAdapter();
        adapter.addItems(App.getInstance().getDatabase().taskDao().getAll());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {   //update edit
                pos = position;
                task = adapter.getItem(position);
                openFragment(task);
            }

            @Override
            public void onLongClick(int position) {  //delete
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Delete the task ");
                builder.setMessage("Are you sure you wsnt to delete this task?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        task = adapter.getItem(position);
                        App.getInstance().getDatabase().taskDao().delete(adapter.getItem(position));
                        adapter.removeItem(position);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

//                task = adapter.getItem(position);
//                App.getInstance().getDatabase().taskDao().delete(adapter.getItem(position));
//                adapter.removeItem(position);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        binding.fab.setOnClickListener(view1 -> {
            pos=-1;
            openFragment(null);
        });

        getParentFragmentManager().setFragmentResultListener("rk_task", getViewLifecycleOwner(), (requestKey, result) -> {
            task = (Task) result.getSerializable("task");
            Log.e("Home","result =" +task.getText());

                if (pos ==-1) {
                    adapter.addItem(task);
                }else{
                    adapter.update(pos,task);
                }

        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {

            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    private void initList() {
        binding.recyclerView.setAdapter(adapter);
    }

    private void openFragment(Task task) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("key", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.taskFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.alphabetSort){

            adapter.getList().clear();
            adapter.addItems(App.getInstance().getDatabase().taskDao().getAllSortedByTitle());
            return true;
        }else{
            adapter.getList().clear();
            adapter.addItems(App.getInstance().getDatabase().taskDao().getAll());

        }

        return super.onOptionsItemSelected(item);

    }
}