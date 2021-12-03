package kg.geektech.taskapp36.ui.fragments.task;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import kg.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.models.Task;


public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private Task task;
    private ActivityResultLauncher<String> getImage;
    private String strUrl;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
        binding.progressBar.setVisibility(View.GONE);
        task = (Task) requireArguments().getSerializable("key");
        if (task != null) {
            binding.editTextTask.setText(task.getText());
            Glide.with(requireContext()).load(task.getImgUrl()).into(binding.imageTask);
            // с приходящего  таска берем данные и сеттим в edittext
        }
    }

    private void initListeners() {
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(),result -> {
           if (result!=null){  //choose photo form gallery
               binding.imageTask.setImageURI(result);
               upload(result);

           }
        });
        binding.imageTask.setOnClickListener(view -> getImage.launch("image/*"));

        binding.saveBtn.setOnClickListener(view1 -> {
            if (!Objects.requireNonNull(binding.editTextTask.getText()).toString().isEmpty()&& binding.imageTask.getDrawable() != null){
                save();
            }

        });
    }

    private void upload(Uri uri) {
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String userID = FirebaseAuth.getInstance().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("IMG"+userID+"_"+time+".jpg");
        reference.putFile(uri).continueWithTask(task1 -> reference.getDownloadUrl()).addOnCompleteListener(task1 -> strUrl = task1.getResult().toString());
    }


    private void save() {
        showProgress();
        String text = Objects.requireNonNull(binding.editTextTask.getText()).toString().trim();

        if (task ==null){
             task = new Task(text, System.currentTimeMillis());
             if (strUrl != null){
                 task.setImgUrl(strUrl);
             }
             App.getInstance().getDatabase().taskDao().insert(task);
             saveToFireStore(task);
        }else{
            task.setText(text);
            task.setImgUrl(strUrl);
           App.getInstance().getDatabase().taskDao().update(task);
           if (task.getDocId()!=null){
               updatetoFirestore(task);
           }else{
               closeFragment();
           }

        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        getParentFragmentManager().setFragmentResult("rk_task", bundle);

    }

    private void updatetoFirestore(Task task) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(task.getDocId())
                .update("text",task.getText(),"imgUrl",task.getImgUrl())
                .addOnSuccessListener(unused -> closeFragment());

    }

    private void showProgress() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveBtn.setVisibility(View.GONE);
    }

    private void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
        binding.saveBtn.setVisibility(View.VISIBLE);
    }

    private void saveToFireStore(Task task) {
        FirebaseFirestore
                .getInstance()
                .collection("tasks")
                .add(task).addOnCompleteListener(task1 -> {
                    hideProgress();
                    if (task1.isSuccessful()){
                        closeFragment();
                    }
                });

    }

    private void closeFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();

    }
}