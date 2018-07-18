package nl.soft.pelorus.pelorus3.ui.login.add;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import io.reactivex.annotations.Nullable;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;
import nl.soft.pelorus.pelorus3.ui.mainmenu.MainMenuActivity;

/**
 * Created by tobia on 3-8-2017.
 */

public class AddUserFragment extends LifecycleFragment {

    private static final int RC_SIGN_IN = 9001;

    private SignInButton signInButton;

    private AddUserViewModel addUserViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        setupViews(view);
        setupClickListeners();
        setupViewModel();

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        addUserViewModel.connect();
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        addUserViewModel.disconnect();
    }


    private void setupViewModel() {
        PelorusApplication pelorusApplication = (PelorusApplication) getActivity().getApplication();
        addUserViewModel = ViewModelProviders.of(this, new PelorusFactory(pelorusApplication))
                .get(AddUserViewModel.class);
    }

    private void setupClickListeners() {
        signInButton.setOnClickListener(view -> {
             startActivityForResult(addUserViewModel.getSignInIntent(), RC_SIGN_IN);
        });
    }

    private void setupViews(View view) {
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        boolean unauthenticated;

        unauthenticated = addUserViewModel.setAccount(requestCode, resultCode, data, RC_SIGN_IN);

        if(unauthenticated){
            CharSequence text = "Unauthenticated";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
        }
        else {
            Intent intent = new Intent(getActivity(), MainMenuActivity.class);
            startActivity(intent);
        }

    }


}
