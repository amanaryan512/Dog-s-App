package com.example.dogsapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DogBreed.class},version = 1)
public abstract class DogDatabase extends RoomDatabase  {


    private static DogDatabase instance;  //variable

    //Singleton design pattern
    public static DogDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(                // generating instance...if instance==null
                    context.getApplicationContext(),
                    DogDatabase.class,
                    "dogdatabase")
                    .build();
        }
        return instance;
    }
    //create Dao
    public abstract DogDao dogDao();  // provide the interface and allow the application to have access methods inside it.
}
