package com.chanresti.dogmanager;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YouTubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;

    public static final String API_KEY = "AIzaSyAmJLRe_MaAx0RYLLAT431X1YbsRoKn3gk";
    public static final String PACKAGE_NAME = "com.chanresti.dogmanager";
    public static final String SHA1 = "80:60:E0:7E:B6:59:D7:CF:DF:5B:56:A8:AB:A0:85:6D:A6:5B:8B:F5";
    private static final long MAX_RESULTS = 100;

    public YouTubeConnector() {

        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

            //initialize method helps to add any extra details that may be required to process the query
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.getHeaders().set("X-Android-Package", PACKAGE_NAME);
                request.getHeaders().set("X-Android-Cert",SHA1);
            }
        }).setApplicationName("SearchYoutube").build();

        try {
            query = youtube.search().list("id,snippet");
            query.setKey(API_KEY);
            query.setType("video");
            query.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");

        } catch (IOException e) {
        }
    }

    public List<VideoItem> search() {

        String queryKeywords = "dog training";
        query.setQ(queryKeywords);
        query.setMaxResults(MAX_RESULTS);

        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> videoList = new ArrayList<VideoItem>();
            if (results != null) {
                videoList = setVideoList(results.iterator());
            }
            return videoList;

        } catch (IOException e) {
            return null;
        }
    }

    private static List<VideoItem> setVideoList(Iterator<SearchResult> iteratorSearchResults) {
        List<VideoItem> videoList = new ArrayList<>();

        while (iteratorSearchResults.hasNext()) {
            SearchResult searchResult = iteratorSearchResults.next();
            ResourceId rId = searchResult.getId();
            if (rId.getKind().equals("youtube#video")) {
                VideoItem videoItem = new VideoItem();
                Thumbnail thumbnail = searchResult.getSnippet().getThumbnails().getHigh();
                videoItem.setId(searchResult.getId().getVideoId());
                videoItem.setTitle(searchResult.getSnippet().getTitle());
                videoItem.setThumbnailURL(thumbnail.getUrl());
                videoList.add(videoItem);
            }
        }
        return videoList;
    }
}
