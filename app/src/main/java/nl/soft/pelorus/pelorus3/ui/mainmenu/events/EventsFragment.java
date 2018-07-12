package nl.soft.pelorus.pelorus3.ui.mainmenu.events;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;
import nl.soft.pelorus.pelorus3.ui.mainmenu.profile.ViewProfileViewModel;

/**
 * Created by tobia on 25-8-2017.
 */

public class EventsFragment extends LifecycleFragment {

    private EventFragmentViewModel eventFragmentViewModel;
    private RecyclerView recyclerView;

    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list_events);

        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        eventFragmentViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(EventFragmentViewModel.class);

        setupViewModel();

        eventFragmentViewModel.getAllEvents().observe(this,events -> {
            adapter.setItems(events);
        });

        return view;
    }

    private void setupViewModel() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventAdapter(new ArrayList<>(),eventFragmentViewModel);
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
