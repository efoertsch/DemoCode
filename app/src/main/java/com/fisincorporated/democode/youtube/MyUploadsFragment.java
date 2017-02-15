package com.fisincorporated.democode.youtube;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoDrillDownFragment;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * From https://github.com/youtube/api-samples/blob/master/java/src/main/java/com/google/api/services/samples/youtube/cmdline/data/MyUploads.java
 * And modified for fragment
 */
public class MyUploadsFragment extends DemoDrillDownFragment {

    public static final String TAG = "MyUploadsFragment";
        /**
         * Define a global instance of a Youtube object, which will be used
         * to make YouTube Data API requests.
         */
        private static YouTube youtube;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.youtube_demo, container, false);
        retrieveMyUploads();



        return view;
    }

        /**
         * Authorize the user, call the youtube.channels.list method to retrieve
         * the playlist ID for the list of videos uploaded to the user's channel,
         * and then call the youtube.playlistItems.list method to retrieve the
         * list of videos in that playlist.
         */
        public void retrieveMyUploads() {

            // This OAuth 2.0 access scope allows for read-only access to the
            // authenticated user's account, but not other types of account access.
            List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");

            try {
                // Authorize the request.
                Credential credential = Auth.authorize(getActivity(), scopes, "myuploads");

                // This object is used to make YouTube Data API requests.
                youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                        "youtube-cmdline-myuploads-sample").build();

                // Call the API's channels.list method to retrieve the
                // resource that represents the authenticated user's channel.
                // In the API response, only include channel information needed for
                // this use case. The channel's contentDetails part contains
                // playlist IDs relevant to the channel, including the ID for the
                // list that contains videos uploaded to the channel.
                YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
                channelRequest.setMine(true);
                channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
                ChannelListResponse channelResult = channelRequest.execute();

                List<Channel> channelsList = channelResult.getItems();

                if (channelsList != null) {
                    // The user's default channel is the first item in the list.
                    // Extract the playlist ID for the channel's videos from the
                    // API response.
                    String uploadPlaylistId =
                            channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();

                    // Define a list to store items in the list of uploaded videos.
                    List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

                    // Retrieve the playlist of the channel's uploaded videos.
                    YouTube.PlaylistItems.List playlistItemRequest =
                            youtube.playlistItems().list("id,contentDetails,snippet");
                    playlistItemRequest.setPlaylistId(uploadPlaylistId);

                    // Only retrieve data used in this application, thereby making
                    // the application more efficient. See:
                    // https://developers.google.com/youtube/v3/getting-started#partial
                    playlistItemRequest.setFields(
                            "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");

                    String nextToken = "";

                    // Call the API one or more times to retrieve all items in the
                    // list. As long as the API response returns a nextPageToken,
                    // there are still more items to retrieve.
                    do {
                        playlistItemRequest.setPageToken(nextToken);
                        PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                        playlistItemList.addAll(playlistItemResult.getItems());

                        nextToken = playlistItemResult.getNextPageToken();
                    } while (nextToken != null);

                    // Prints information about the results.
                    prettyPrint(playlistItemList.size(), playlistItemList.iterator());
                }

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, e.toString() );
                Log.d(TAG, "There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());

            } catch (Throwable t) {
                Log.d(TAG, t.toString());

            }
        }

        /*
         * Print information about all of the items in the playlist.
         *
         * @param size size of list
         *
         * @param iterator of Playlist Items from uploaded Playlist
         */
        private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) {
            Log.d(TAG,"=============================================================");
            Log.d(TAG, "\t\tTotal Videos Uploaded: " + size);
            Log.d(TAG, "=============================================================\n");

            while (playlistEntries.hasNext()) {
                PlaylistItem playlistItem = playlistEntries.next();
                Log.d(TAG, " video name  = " + playlistItem.getSnippet().getTitle());
                Log.d(TAG, " video id    = " + playlistItem.getContentDetails().getVideoId());
                Log.d(TAG, " upload date = " + playlistItem.getSnippet().getPublishedAt());
                Log.d(TAG, "\n-------------------------------------------------------------\n");
            }
        }

}
