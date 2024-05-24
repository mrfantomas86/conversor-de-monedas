package com.consumo.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApiGson {

    public static String getExchangeRate(String fromCurrency, String toCurrency ) throws IOException, InterruptedException {

        String GET_URL = "https://v6.exchangerate-api.com/v6/cd8c5579f6e5edca88eb87ed/latest/" + fromCurrency;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){

            Gson gson = new Gson();
            JsonObject object = gson.fromJson(response.body(), JsonObject.class);
            double exchangeRate = object.getAsJsonObject("conversion_rates").get(toCurrency).getAsDouble();
            return Double.toString(exchangeRate);
        } else{
            throw new IOException("La petición GET no funcionó");
        }
    }

}
