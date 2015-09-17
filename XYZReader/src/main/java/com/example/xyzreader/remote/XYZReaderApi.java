package com.example.xyzreader.remote;

import com.example.xyzreader.util.JsonUtil;
import com.google.gson.*;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;

import java.lang.reflect.Type;
import java.util.List;

public class XYZReaderApi {
// ------------------------------ FIELDS ------------------------------

    public static final String API_ENDPOINT = "https://dl.dropboxusercontent.com/u/231329/xyzreader_data";

    private Api mApi;

// --------------------------- CONSTRUCTORS ---------------------------

    public XYZReaderApi(OkHttpClient okHttpClient) {
        mApi = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(API_ENDPOINT)
                .setConverter(gsonConverter())
                .build()
                .create(Api.class);
    }

// -------------------------- OTHER METHODS --------------------------

    public List<Article> getRecipes() {
        return mApi.getRecipes();
    }

    private GsonConverter gsonConverter() {
        return new GsonConverter(new GsonBuilder()
                .registerTypeAdapter(Article.class, new ArticleDeserializer())
                .create());
    }

    interface Api {
        @GET("/data.json")
        List<Article> getRecipes();
    }

    static class ArticleDeserializer implements JsonDeserializer<Article> {
        @Override
        public Article deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            Article article = new Article();
            article.author = JsonUtil.toString(element, "author");
            article.body = JsonUtil.toString(element, "body");
            article.id = JsonUtil.toString(element, "id");
            article.photo = JsonUtil.toString(element, "photo");
            article.publishedDate = JsonUtil.toDate(element, "published_date");
            article.title = getTitle(element);
            return article;
        }

        private String getTitle(JsonElement element) {
            String title = JsonUtil.toString(element, "title");
            return title.substring(title.indexOf(' ') + 1);
        }
    }
}
