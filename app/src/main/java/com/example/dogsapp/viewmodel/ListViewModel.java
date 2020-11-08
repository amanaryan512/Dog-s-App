package com.example.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.model.DogDao;
import com.example.dogsapp.model.DogDatabase;
import com.example.dogsapp.model.DogsApiService;
import com.example.dogsapp.util.NotificationsHelper;
import com.example.dogsapp.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {
    public MutableLiveData<List<DogBreed>> dogs=new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError=new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();

    DogsApiService dogsService=new DogsApiService();

    private CompositeDisposable disposable=new CompositeDisposable();

    private AsyncTask<List<DogBreed>,Void,List<DogBreed>> insertTask;
    private AsyncTask<Void,Void,List<DogBreed>> retrieveTask;

    private SharedPreferencesHelper prefHelper=SharedPreferencesHelper.getInstance(getApplication());
    private long refereshTime=5*60*1000*1000*100L;


    public ListViewModel(@NonNull Application application) { //Application provides us the context in the ViewModel
        super(application);
    }
    public void refresh() {
        checkCacheDuration();
        long updateTime=prefHelper.getUpdateTime();
        long currentTime=System.nanoTime();
        if(updateTime!=0 && currentTime-updateTime < refereshTime)
            fetchFromDatabase();
        else
            fetchFromRemote();

    }

    public void refreshBypassCache(){
        fetchFromRemote();

    }
    private void checkCacheDuration(){
        String cachePreference =prefHelper.getCacheDuration();
        if(!cachePreference.equals("")){
            try {
                int cachePreferenceInt=Integer.parseInt(cachePreference);
                refereshTime=cachePreferenceInt*1000*1000*1000L;  //convert in nanoSecond
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

        }

    }
    private void fetchFromDatabase(){
        loading.setValue(true);
        retrieveTask=new RetrieveDogsTask();
        retrieveTask.execute();
    }

    private void fetchFromRemote(){
        loading.setValue(true);
        disposable.add(
        dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(List<DogBreed> dogBreeds) {
                        insertTask=new InsertDogsTask();
                        insertTask.execute(dogBreeds);
                        Toast.makeText(getApplication(),"tretrived from end point",Toast.LENGTH_SHORT).show();
                        NotificationsHelper.getInstance(getApplication()).createNotification();



                    }

                    @Override
                    public void onError(Throwable e) {
                        dogLoadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();

                    }
                })
        );
    }
    private void dogRetrived(List<DogBreed> dogList){
        dogs.setValue(dogList);
        loading.setValue(false);
        dogLoadError.setValue(false);


    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        if(insertTask!=null){
            insertTask.cancel(true);
            insertTask=null;
        }
        if(retrieveTask!=null){
            retrieveTask.cancel(true);
            retrieveTask=null;
        }
    }
    private class InsertDogsTask extends AsyncTask<List<DogBreed>,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list=lists[0];
            DogDao dao= DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();
            ArrayList<DogBreed> newList=new ArrayList<>(list);
            List<Long> result=dao.insertAll(newList.toArray(new DogBreed[0]));
            int i=0;
            while (i<list.size()){
                list.get(i).uuid=result.get(i).intValue();
                i++;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) { //Main thread...
            dogRetrived(dogBreeds);
            prefHelper.saveUpdateTime(System.nanoTime());
        }
    }
    private class RetrieveDogsTask extends AsyncTask<Void,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogRetrived(dogBreeds);
            Toast.makeText(getApplication(),"retrived from database point",Toast.LENGTH_SHORT).show();
        }
    }
}
