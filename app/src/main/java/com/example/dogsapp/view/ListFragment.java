package com.example.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dogsapp.R;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.viewmodel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {
    private ListViewModel listViewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>()); //In argument we are passing empty array list
    RecyclerView recyclerView;
    TextView listError;
    ProgressBar loadingView;
    SwipeRefreshLayout refreshLayout;


    public ListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.dogsList);
        listError = view.findViewById(R.id.listError);
        loadingView = view.findViewById(R.id.loadingView);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ListFragmentDirections.ActionDetail action=ListFragmentDirections.actionDetail();
        // Navigation.findNavController(view).navigate(action);

        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);// this is how we instantiate the viewModel in the view
        listViewModel.refresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dogsListAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setVisibility(View.GONE);
                listError.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                listViewModel.refreshBypassCache();
                refreshLayout.setRefreshing(false);
            }
        });
        observeViewModel();
    }

    private void observeViewModel() {

        listViewModel.dogs.observe(this, dogs -> {
            if (dogs != null && dogs instanceof List) {
                recyclerView.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogslist(dogs);
            }
        });
        listViewModel.dogLoadError.observe(this, isError -> {
            if (isError != null && isError instanceof Boolean) {
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }

        });
        listViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    listError.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

        });


    }

    //Creating settings...
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    //this method reacts to menu clicks...
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:
                if (isAdded()) {
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;

        }
         return super.onOptionsItemSelected(item);
    }
}

