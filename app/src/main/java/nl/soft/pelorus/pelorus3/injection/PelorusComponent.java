package nl.soft.pelorus.pelorus3.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import nl.soft.pelorus.pelorus3.ui.boats.BoatsActivity;
import nl.soft.pelorus.pelorus3.ui.boats.BoatsActivityViewModel;
import nl.soft.pelorus.pelorus3.ui.login.add.AddUserViewModel;
import nl.soft.pelorus.pelorus3.ui.mainmenu.MainMenuViewModel;
import nl.soft.pelorus.pelorus3.ui.boats.BoatsFragmentViewModel;
import nl.soft.pelorus.pelorus3.ui.boats.manage.ManageBoatViewModel;
import nl.soft.pelorus.pelorus3.ui.mainmenu.events.EventFragmentViewModel;
import nl.soft.pelorus.pelorus3.ui.mainmenu.profile.ViewProfileViewModel;
import nl.soft.pelorus.pelorus3.ui.race.RaceViewModel;
import nl.soft.pelorus.pelorus3.ui.race.dashboard.DashboardViewModel;
import nl.soft.pelorus.pelorus3.ui.race.leaderboard.LeaderboardViewModel;
import nl.soft.pelorus.pelorus3.ui.race.map.MapViewModel;

/**
 * Created by tobia on 3-8-2017.
 */
@Singleton
@Component(modules = {PelorusModule.class})
public interface PelorusComponent {

    void inject(AddUserViewModel userViewModel);

    void inject(ViewProfileViewModel viewProfileViewModel);

    void inject(EventFragmentViewModel eventFragmentViewModel);

    void inject(BoatsFragmentViewModel boatsFragmentViewModel);

    void inject(ManageBoatViewModel manageBoatViewModel);

    void inject(MapViewModel mapViewModel);

    void inject(RaceViewModel raceViewModel);

    void inject(DashboardViewModel dashboardViewModel);

    void inject(MainMenuViewModel mainMenuViewModel);

    void inject(LeaderboardViewModel leaderboardViewModel);

    void inject(BoatsActivityViewModel boatsActivityViewModel);

    interface Injectable{
        void inject(PelorusComponent pelorusComponent);
    }
}
