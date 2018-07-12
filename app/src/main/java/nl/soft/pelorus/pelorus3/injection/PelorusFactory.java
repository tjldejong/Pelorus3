package nl.soft.pelorus.pelorus3.injection;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import nl.soft.pelorus.pelorus3.PelorusApplication;

/**
 * Created by tobia on 3-8-2017.
 */

public class PelorusFactory extends ViewModelProvider.NewInstanceFactory {

    private PelorusApplication application;

    public PelorusFactory(PelorusApplication pelorusApplication){this.application = pelorusApplication;}

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        T t = super.create(modelClass);
        if (t instanceof PelorusComponent.Injectable) {
            ((PelorusComponent.Injectable) t).inject(application.getPelorusComponent());
        }
        return t;
    }
}
