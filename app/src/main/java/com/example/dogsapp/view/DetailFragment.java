package com.example.dogsapp.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dogsapp.R;
import com.example.dogsapp.databinding.FragmentDetailBinding;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.model.DogPallete;
import com.example.dogsapp.util.Util;
import com.example.dogsapp.viewmodel.DetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel detailViewModel;
    FragmentDetailBinding binding;
    /*
    private ImageView dogImage;
    private TextView dogName;
    private TextView dogPurpose;
    private TextView dogTemperament;
    private TextView dogLifeSpan;*/


    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // View view=inflater.inflate(R.layout.fragment_detail, container, false);
       /* dogImage=view.findViewById(R.id.dogImage);
        dogName=view.findViewById(R.id.dogName);
        dogPurpose=view.findViewById(R.id.dogPurpose);
        dogTemperament=view.findViewById(R.id.dogTemperament);
        dogLifeSpan=view.findViewById(R.id.dogLifeSpan);*/
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.fetch(dogUuid);
        obererveViewModel();


    }

    private void obererveViewModel() {
        detailViewModel.dogLiveData.observe(this, dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                /*
                dogName.setText(dogBreed.dogBreed);
                dogPurpose.setText(dogBreed.breedFor);
                dogTemperament.setText(dogBreed.temperament);
                dogLifeSpan.setText(dogBreed.lifeSpan);
                if(dogBreed.imageUrl!=null){
                    Util.loadImage(dogImage,dogBreed.imageUrl,new CircularProgressDrawable(getContext()));
                }*/
                binding.setDog(dogBreed);
                if(dogBreed.imageUrl!=null){
                    setupBackgrounfColor(dogBreed.imageUrl);
                }
            }
        });
    }
    private void setupBackgrounfColor(String url){
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@Nullable Palette palette) {
                                        int intColor=palette.getLightMutedSwatch().getRgb();
                                        DogPallete myPallete=new DogPallete(intColor);
                                        binding.setPalette(myPallete);

                                    }
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

}