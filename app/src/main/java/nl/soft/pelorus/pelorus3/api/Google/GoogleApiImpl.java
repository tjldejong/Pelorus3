package nl.soft.pelorus.pelorus3.api.Google;


import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

/**
 * Created by tobia on 4-8-2017.
 */

public class GoogleApiImpl implements GoogleApi {
    @Inject
    GoogleApiClient googleApiClient;

    public GoogleApiImpl(GoogleApiClient googleApiClient){this.googleApiClient = googleApiClient;}

    @Override
    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

}
