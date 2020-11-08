package com.example.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.model.DogDatabase;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    public MutableLiveData<DogBreed> dogLiveData=new MutableLiveData<DogBreed>();

    private RetrieveDogsTask task;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uuid){
        task=new RetrieveDogsTask();
        task.execute(uuid);

    }

    @Override
    protected void onCleared() {
        if(task!=null){
            task.cancel(true);
            task=null;
        }
    }

    public class RetrieveDogsTask extends AsyncTask<Integer,Void, DogBreed>{

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid=integers[0];
            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);
        }
    }

}
