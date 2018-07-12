package nl.soft.pelorus.pelorus3.ui.mainmenu.profile;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 10-8-2017.
 */

public class ViewProfileFragment  extends LifecycleFragment {

    private TextView textViewUserName;

    private ViewProfileViewModel viewProfileViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        setupViewModel();

        textViewUserName = (TextView) view.findViewById(R.id.textView_username);

        viewProfileViewModel.getCurrentUser().observe(this, user -> {
            if(user!=null) {
                textViewUserName.setText(user.getName());
            }
        });

        return view;
    }

    private void setupViewModel() {
        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        viewProfileViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(ViewProfileViewModel.class);
    }
}
