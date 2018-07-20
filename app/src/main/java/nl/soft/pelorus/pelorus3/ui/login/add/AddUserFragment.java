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

import java.io.IOException;

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
        setupViewModel();

        setupClickListeners();

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
            if(isOnline()) {
                startActivityForResult(addUserViewModel.getSignInIntent(), RC_SIGN_IN);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
                builder.setMessage("Please connect to the internet to use this app")
                        .setTitle("No internet")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setupViews(View view) {
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        addUserViewModel.setAccount(data);

        if(addUserViewModel.accountSet){
            Intent intent = new Intent(getActivity(), MainMenuActivity.class);
            startActivity(intent);
        }
        else {
            CharSequence text = "Login failed";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
        }

    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


}
