package nl.soft.pelorus.pelorus3.ui.boats;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 25-8-2017.
 */

public class BoatsFragment extends LifecycleFragment {

    private BoatsFragmentViewModel boatsFragmentViewModel;

    private BoatsAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_boats, container, false);

        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        boatsFragmentViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(BoatsFragmentViewModel.class);

        boatsFragmentViewModel.getAllBoatsFromServer();

        setupRecyclerView(view);

        boatsFragmentViewModel.getAllMyBoats().observe(this,boats -> {
            adapter.setItems(boats);
        });

        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list_boats);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BoatsAdapter(new ArrayList<Boat>(),boatsFragmentViewModel);
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


}
