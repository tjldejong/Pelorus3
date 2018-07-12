package nl.soft.pelorus.pelorus3.ui.race.leaderboard;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.databinding.ListItemLeaderboardBinding;
import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.BoatLocation;
import nl.soft.pelorus.pelorus3.injection.PelorusFactory;

/**
 * Created by tobia on 12-9-2017.
 */

public class LeaderboardAdapter extends BaseAdapter {
    List<BoatLocation> boats;

    LeaderboardListItemViewModel leaderboardListItemViewModel;

    public LeaderboardAdapter(List<BoatLocation> boats) {
        this.boats = boats;
    }

    void setItems(List<BoatLocation> boats){
        if(this.boats!=boats){
            this.boats = boats;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return boats.size();
    }

    @Override
    public BoatLocation getItem(int i) {
        return boats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return boats.get(i).boatid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;

        if(vi == null){
            ListItemLeaderboardBinding listItemLeaderboardBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.list_item_leaderboard,viewGroup,false);
            vi = listItemLeaderboardBinding.getRoot();
            leaderboardListItemViewModel = new LeaderboardListItemViewModel();
            listItemLeaderboardBinding.setViewModelListItem(leaderboardListItemViewModel);
            vi.setTag(leaderboardListItemViewModel);
        }
        else{
            leaderboardListItemViewModel = (LeaderboardListItemViewModel) vi.getTag();
        }

        if(getItem(i)!=null){
            leaderboardListItemViewModel.setName(getItem(i).name);
            if(getItem(i).getCorrectedTime()!=null){
            leaderboardListItemViewModel.setTime(getItem(i).getCorrectedTime());
            }
        }

        return vi;
    }
}
