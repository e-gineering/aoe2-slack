package com.eg.aoe2slackbot.service;

import com.eg.aoe2slackbot.entity.Aoe2RefData;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class RefDataService {

    private static final Logger LOG = LoggerFactory.getLogger(RefDataService.class.getName());

    public static Aoe2RefData aoe2RefData;

    @PostConstruct
    public void init() throws Exception {
        LOG.info("RefDataService getting AOE2 lookup data...");

        OkHttpClient client = new OkHttpClient();

        HttpUrl url = new HttpUrl
                .Builder()
                .scheme("https")
                .host("aoe2.net")
                .addPathSegments("api/strings")
                .addQueryParameter("game", "aoe2de")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(Aoe2RefData.class);
        aoe2RefData = objectMapper.readValue(response.body().string(), javaType);
        aoe2RefData.buildMaps();

        LOG.info("RefDataService AOE2 lookup data load complete!");

    }

}
