package com.boutline.sports.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.content.CursorLoader;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.ContentProviders.MatchProvider;
import com.boutline.sports.R;
import com.boutline.sports.adapters.LiveMatchesAdapter;
import com.boutline.sports.adapters.UpcomingMatchesAdapter;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.models.Match;
import com.google.android.gms.plus.model.people.Person;
import com.path.android.jobqueue.JobManager;
/**
 * Created by user on 15/07/14.
 */
public class UpcomingMatchesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    ListView listView;
    TextView text;
    Cursor c;
    SQLController dbController;

    android.app.LoaderManager loadermanager;

    JobManager jobManager;
    SimpleCursorAdapter matchesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public void populateListViewFromDb(View v)
    {

        String[] fromFieldNames = new String[] {"matchshortname","matchvenue"};

        int[] toViewIDs = new int[]
                {R.id.lblMatchName,R.id.lblMatchVenue};

        matchesAdapter = new UpcomingMatchesAdapter(getActivity(),R.layout.item_match,null,fromFieldNames,toViewIDs,0);
        listView = (ListView) v.findViewById(R.id.upcoming_matches_list);
        listView.setAdapter(matchesAdapter);

        loadermanager = getLoaderManager();
        loadermanager.initLoader(1,null,this);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_upcoming_matches,null);
        populateListViewFromDb(v);
        return v;
    }



    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String tournamentId = getActivity().getIntent().getExtras().getString("tournamentId");

        return new CursorLoader(getActivity(),
               Uri.parse(MatchProvider.URI_UPCOMING_MATCHES+"/"+tournamentId), Match.FIELDS, null, null,
                null);

    }




    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        matchesAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        matchesAdapter.swapCursor(null);

    }
}
