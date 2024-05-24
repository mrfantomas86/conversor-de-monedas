import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;



public class ConvertidorFinal {
    public static void main(String[] args) {
        boolean avanzado = true;

        do {
            HashMap<Integer, String> codigosDivisa = new HashMap<>();
            codigosDivisa.put(1, "PEN");
            codigosDivisa.put(2, "CLP");
            codigosDivisa.put(3, "MXN");
            codigosDivisa.put(4, "COP");
            codigosDivisa.put(5, "ARS");
            codigosDivisa.put(6, "BRL");

            int desde;
            String desdeDivisa;
            double montoAConvertir;

            Scanner teclado = new Scanner(System.in);

            System.out.println("Bievenido al conversor de divisas a dólares");

            System.out.println("¿Qué divisa deseas convertir?");
            System.out.println("1.- PEN (Sol Peruano\n"+
                    "2.- CLP (Peso Chileno)\n"+
                    "3.- MXN (Peso Mexicano)\n"+
                    "4.- COP (Peso Colombiano)\n"+
                    "5.- ARS (Peso Argentino)\n"+
                    "6.- BRL (Real Brasileño)");

            desde = teclado.nextInt();
            while (desde < 1 || desde > 6){
                System.out.println("Por favor elija una divisa válida entre 1 y 6");
                System.out.println();
                desde = teclado.nextInt();
            }

            desdeDivisa = codigosDivisa.get(desde);

            System.out.println("¿Qué monto deseas convertir?");
            montoAConvertir = teclado.nextDouble();

            try{
                Double exchangeRate = Double.parseDouble(getExchangeRate("USD", desdeDivisa));
                DecimalFormat f = new DecimalFormat("0.00");
                System.out.println(f.format(montoAConvertir) + "  " + desdeDivisa + " = "+ f.format(montoAConvertir / exchangeRate) + " USD ");
            }catch (IOException | InterruptedException e){
                System.out.println("Error al realizar la solicitud " + e.getMessage());
            }

            System.out.println("¿Desea convertir otra divisa?");
            System.out.println("Presiona 1: Si \n Presiona otro número: No");

            if (teclado.nextInt() != 1){
                avanzado = false;
            }

        } while (avanzado);
        System.out.println("Gracias por usar nuestro convertidor!");

    }

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
