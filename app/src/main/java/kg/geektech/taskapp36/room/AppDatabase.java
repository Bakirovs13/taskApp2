package kg.geektech.taskapp36.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geektech.taskapp36.models.Task;


    @Database(entities = {Task.class},version =4)
    public abstract class AppDatabase extends RoomDatabase {

        public abstract TaskDao taskDao();
    }


