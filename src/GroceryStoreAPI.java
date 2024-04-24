import java.io.*;
import java.net.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class GroceryStoreAPI {

    private static final String WIREMOCK_URL = "http://localhost:8081";

    public static void main(String[] args) {
        GroceryStoreAPI api = new GroceryStoreAPI();
        api.fetchAndProcessProducts();
    }

    public void fetchAndProcessProducts() {
        int totalSavings = 0;
        try {
            URL url = new URL(WIREMOCK_URL + "/products");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonArray products = JsonParser.parseString(response.toString()).getAsJsonArray();

            for (JsonElement productElement : products) {
                JsonObject product = productElement.getAsJsonObject();
                String productId = product.get("id").getAsString();
                String productName = product.get("name").getAsString();
                int productPrice = product.get("price").getAsInt();
                JsonArray promotions = product.getAsJsonArray("promotions");

                int finalPrice = productPrice;

                if (promotions != null) {
                    finalPrice = applyPromotions(productPrice, promotions);
                }

                int savings = productPrice - finalPrice;
                totalSavings += savings;

                System.out.println("Product ID: " + productId);
                System.out.println("Product Name: " + productName);
                System.out.println("Original Price: " + productPrice);
                System.out.println("Final Price after Promotions: " + finalPrice);
                System.out.println("Savings: " + savings);
                System.out.println("---------------------------");
            }

            System.out.println("Total Savings: " + totalSavings);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int applyPromotions(int price, JsonArray promotions) {
        int finalPrice = price;
        for (JsonElement promotionElement : promotions) {
            JsonObject promotion = promotionElement.getAsJsonObject();
            String type = promotion.get("type").getAsString();
            switch (type) {
                case "BUY_X_GET_Y_FREE":
                    int requiredQty = promotion.get("required_qty").getAsInt();
                    int freeQty = promotion.get("free_qty").getAsInt();
                    finalPrice = applyBuyXGetYFreePromotion(price, requiredQty, freeQty);
                    break;
                case "QTY_BASED_PRICE_OVERRIDE":
                    int overrideQty = promotion.get("required_qty").getAsInt();
                    int overridePrice = promotion.get("price").getAsInt();
                    finalPrice = applyQtyBasedPriceOverride(price, overrideQty, overridePrice);
                    break;
                case "FLAT_PERCENT":
                    int percent = promotion.get("amount").getAsInt();
                    finalPrice = applyFlatPercentDiscount(price, percent);
                    break;
            }
        }
        return finalPrice;
    }

    private int applyBuyXGetYFreePromotion(int price, int requiredQty, int freeQty) {
        int totalQty = requiredQty + freeQty;
        int totalPrice = (price / requiredQty) * totalQty;
        return totalPrice;
    }

    private int applyQtyBasedPriceOverride(int price, int requiredQty, int overridePrice) {
        if (requiredQty <= 0) {
            return price;
        }
        return overridePrice;
    }

    private int applyFlatPercentDiscount(int price, int percent) {
        return price - (price * percent / 100);
    }
}
