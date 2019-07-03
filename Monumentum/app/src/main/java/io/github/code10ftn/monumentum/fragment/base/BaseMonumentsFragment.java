package io.github.code10ftn.monumentum.fragment.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import io.github.code10ftn.monumentum.adapter.MonumentsAdapter;
import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.utils.Constants;

@EFragment
public abstract class BaseMonumentsFragment extends Fragment {

    @ViewById
    protected SwipeRefreshLayout refreshLayout;

    @ViewById
    protected ListView listView;

    @Bean
    protected MonumentsAdapter adapter;

    protected List<Monument> monuments = new ArrayList<>();

    @AfterViews
    protected void setupListView() {
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(() -> {
            getMonuments();
            refreshLayout.setRefreshing(false);
        });
    }

    @UiThread
    protected void setItems(List<Monument> monuments) {
        adapter.setMonuments(monuments);
        adapter.notifyDataSetChanged();
    }

    @UiThread
    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Receiver(actions = Constants.INTENT_ACTION_SEARCH, local = true)
    protected void onSearch(@Receiver.Extra String searchString, Context context) {
        final List<Monument> filteredList = new ArrayList<>();
        for (Monument monument : monuments) {
            if (monument.getName().toLowerCase().contains(searchString.toLowerCase()) ||
                    monument.getDescription().toLowerCase().contains(searchString.toLowerCase())) {
                filteredList.add(monument);
            }
        }

        setItems(filteredList);
    }

    protected abstract void getMonuments();

    protected boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
