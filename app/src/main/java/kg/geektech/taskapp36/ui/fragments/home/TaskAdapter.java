package kg.geektech.taskapp36.ui.fragments.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> list;
    private OnItemClickListener onItemClickListener;





    public TaskAdapter() {
        list = new ArrayList<>();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_task,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.getImage())   //dz7
                .load(list.get(position).getImgUrl())
                .into(holder.getImage());

        //зебра
        holder.bind(list.get(position));
        if (position%2 ==0){
            holder.rootView.setBackgroundResource(R.color.white);
        }else{
            holder.rootView.setBackgroundResource(R.color.blue);

            holder.textTitle.setTextColor(Color.parseColor("#FFFFFF"));

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Task getItem(int position){
        return list.get(position);
    }

    //обновление данных по позиции
//
//    public  void update(int pos, Task task){
//        list.set(pos,task);
//       notifyItemChanged(pos);
//    }



    public void addItem(Task task) {

        list.add(0,task);  //добавление таска в адаптер
        notifyItemInserted(0);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeItem(int position) {
         list.remove(position);
         notifyItemRemoved(position);
    }

    public void addItems(List<Task> tasks) {
        list.clear();
        list.addAll(tasks);
        notifyDataSetChanged();

    }

    public void update(int pos,Task task) {
        list.set(pos,task);
        notifyItemChanged(pos);
    }

    public List<Task> getList() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView textTitle;
        private CardView rootView;
        private ImageView image;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.taskItem);
            textTitle =itemView.findViewById(R.id.textTask);
            checkBox = itemView.findViewById(R.id.checkbox);
            image = itemView.findViewById(R.id.image_cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    onItemClickListener.onLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

        public ImageView getImage() {  //dz7
            return image;
        }

        public void bind(Task task) {
            textTitle.setText(task.getText());
            checkBox.setOnClickListener(view -> {
                if (checkBox.isChecked()){
                    textTitle.setPaintFlags(textTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    textTitle.setPaintFlags(textTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                }
            });


            textTitle.setOnClickListener(view -> {
                onItemClickListener.onClick(getAdapterPosition());
            });
        }
    }
}
