package nl.soft.pelorus.pelorus3;

import android.app.Application;

import nl.soft.pelorus.pelorus3.injection.DaggerPelorusComponent;
import nl.soft.pelorus.pelorus3.injection.PelorusComponent;
import nl.soft.pelorus.pelorus3.injection.PelorusModule;


/**
 * Created by tobia on 3-8-2017.
 */

public class PelorusApplication extends Application {
    private final PelorusComponent pelorusComponent = createPelorusComponent();

    public PelorusComponent createPelorusComponent() {
        return DaggerPelorusComponent.builder()
                .pelorusModule(new PelorusModule(this))
                .build();
    }

    public PelorusComponent getPelorusComponent() {return pelorusComponent;}
}
