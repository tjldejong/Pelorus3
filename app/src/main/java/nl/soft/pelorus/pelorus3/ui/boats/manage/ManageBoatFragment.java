package nl.soft.pelorus.pelorus3.ui.boats.manage;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 26-8-2017.
 */

public class ManageBoatFragment  extends LifecycleFragment {
    private EditText editTextBoatname;
    private EditText editTextSW;
    private FloatingActionButton floatingActionButton;
    private ManageBoatViewModel manageBoatViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_boat, container, false);

        setupViews(view);
        setupClickListeners();
        setupViewModel();
        return view;
    }

    private void setupViews(View view) {
        editTextBoatname = (EditText) view.findViewById(R.id.editText_boatname);
        editTextSW = (EditText) view.findViewById(R.id.editText_sw);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_commit_boat);
    }

    private void setupViewModel(){
        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        manageBoatViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(ManageBoatViewModel.class);
    }

    private void setupClickListeners(){
        editTextBoatname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                manageBoatViewModel.setBoatName(editable.toString());
            }
        });

        editTextSW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty())
                    try
                    {
                        manageBoatViewModel.setSw(Double.parseDouble(editable.toString()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
            }
        });


        floatingActionButton.setOnClickListener(view -> {
            manageBoatViewModel.addBoat();
            getActivity().finish();
        });
    }
}
