package dev.lemontree.gifcreator.util;

import com.google.gson.Gson;
import dev.lemontree.gifcreator.GifCreator;
import dev.lemontree.gifcreator.json.Gif;
import dev.lemontree.gifcreator.json.Network;
import dev.lemontree.gifcreator.json.SearchJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Giphy {
    private final String apiKey;
    private final Gson gson;

    public Giphy() throws IOException {
        Object configApiKey = GifCreator.getInstance().getConfig().get("giphyApiKey");

        if (configApiKey == null) {
            throw new IOException("No GIPHY Api key!");
        }

        this.apiKey = configApiKey.toString();

        this.gson = new Gson();
    }

    public List<String> createUrlList(List<Gif> gifs) {
        List<String> urls = new ArrayList<>();

        for (Gif gif : gifs) {
            urls.add(gif.images().original().url());
            urls.add(gif.images().fixed_height().url());
            urls.add(gif.images().fixed_width().url());
            urls.add(gif.images().fixed_height_small().url());
            urls.add(gif.images().fixed_width_small().url());
        }

        return urls;
    }

    public List<Gif> getGifSearch(String gifName) throws IOException {
        String urlString = "https://api.giphy.com/v1/gifs/search?limit=10&api_key=" + apiKey + "&q=" + gifName;

        String response = Network.getJson(urlString);

        SearchJson jsonResponse = gson.fromJson(response, SearchJson.class);

        return List.of(jsonResponse.data());
    }
}
