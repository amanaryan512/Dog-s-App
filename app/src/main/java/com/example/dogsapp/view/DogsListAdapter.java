package com.example.dogsapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsapp.R;
import com.example.dogsapp.databinding.ItemDogBinding;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListner{

    private ArrayList<DogBreed> dogsList;  // lists of dog that we want to display

    public DogsListAdapter(ArrayList<DogBreed> dogsList) {
        this.dogsList = dogsList;    // we will store the list that we will get in the constructor
    }
    public void updateDogslist(List<DogBreed> newDogsList){
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged(); //tell the system that the data for this adapter has changed therefore you read the whole list
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemDogBinding view= DataBindingUtil.inflate(inflater,R.layout.item_dog,parent,false);


    //    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog,parent,false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) { //attach information from dogList to the view holder

        holder.itemView.setDog(dogsList.get(position));
        holder.itemView.setListener(this::onDogClicked);
      /*  ImageView image=holder.itemView.findViewById(R.id.imageView);
        TextView name=holder.itemView.findViewById(R.id.name);
        TextView lifeSpan=holder.itemView.findViewById(R.id.lifespan);
        LinearLayout layout=holder.itemView.findViewById(R.id.dogLayout);

        name.setText(dogsList.get(position).dogBreed);
        lifeSpan.setText(dogsList.get(position).lifeSpan);
        Util.loadImage(image,dogsList.get(position).imageUrl,Util.getProgressDrawable(image.getContext()));
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ListFragmentDirections.ActionDetail action=ListFragmentDirections.actionDetail();
              action.setDogUuid(dogsList.get(position).uuid);
                Navigation.findNavController(layout).navigate(action);
            }
        }); */
    }
    @Override
    public void onDogClicked(View v) {
        String uuidString= ((TextView)v.findViewById(R.id.dogId)).getText().toString();
        int uuid=Integer.valueOf(uuidString);
        ListFragmentDirections.ActionDetail action=ListFragmentDirections.actionDetail();
        action.setDogUuid(uuid);
        Navigation.findNavController(v).navigate(action);



    }



    @Override
    public int getItemCount() {
        return dogsList.size();


    }



    class DogViewHolder extends RecyclerView.ViewHolder {

        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;  //we want to store the view elements inside our viewholder
        }
    }
}
