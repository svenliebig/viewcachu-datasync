package io.github.sly321.viewcachu.datasync.service.impl;

import io.github.sly321.viewcachu.datasync.model.Series;
import io.github.sly321.viewcachu.datasync.service.TheTvDatabaseApiWrapper;
import io.github.sly321.viewcachu.datasync.service.helper.TheTvDatabaseResponseTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.List;

import javax.xml.ws.http.HTTPException;


public class TheTvDatabaseApiWrapperImpl implements TheTvDatabaseApiWrapper {
    private String API_KEY = "FDDE19A386F91E9D";

    @Override
    public void getSerieById(final int id) {
        final String urlString = API_URL + API_KEY + "/series/" + id + "/" + LANG_KEY;
        requestUrl(urlString);
    }

    @Override
    public List<Series> findSerieByName(final String name) {
        String urlString = "";

        try {
            urlString = API_URL + BY_NAME + URLEncoder.encode(name, "UTF-8") + LANG_PARAM;
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        final String response = requestUrl(urlString);
        final List<Series> series = TheTvDatabaseResponseTransformer.transformResponseToSeriesList(
                response);

        return series;
    }

    private String requestUrl(final String urlString) {
        String output = null;

        try {
            final URL url = new URL(urlString);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            final int responseCode = conn.getResponseCode();
            checkConnection(responseCode);

            final BufferedReader br =
                new BufferedReader(new InputStreamReader((conn.getInputStream())));

            output = "";

            String line;

            while ((line = br.readLine()) != null) {
                output += line;
            }

            conn.disconnect();
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    private void checkConnection(final int responseCode) throws HTTPException {
        if (responseCode != 200) {
            throw new HTTPException(responseCode);
        }
    }
}
