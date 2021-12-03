package kg.geektech.taskapp36.ui.fragments.onBoard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.BoardListBinding;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private BoardListBinding binding;

    private final String[] titles = new String[]{"Manage your task","Work on Time","Get reminder of Time"};
    private final String[] description = new String[]{"description1","description2","description3"};
    private final int[] images = new int[]{R.raw.anim,R.raw.anim,R.raw.anim};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       binding = BoardListBinding.inflate(LayoutInflater
               .from(parent.getContext()),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
        if (position ==2){
            binding.startBtn.setVisibility(View.VISIBLE);
        }else{
            binding.startBtn.setVisibility(View.INVISIBLE);
        }

    }



    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull BoardListBinding itemView) {
            super(itemView.getRoot());

            binding.startBtn.setOnClickListener(view -> {
                Navigation.findNavController(itemView.getRoot()).navigate(R.id.navigation_home);
            });
        }


        public void bind(int position) {
            binding.titleBoard.setText(titles[position]);
            binding.descriptionBoard.setText(description[position]);
            binding.animationView.setAnimation(images[position]);


        }
    }
}
