import java.io.*;
import java.net.*;
import com.google.gson.*;

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

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                JsonArray productsJson = JsonParser.parseString(response.toString()).getAsJsonArray();

                for (JsonElement productElement : productsJson) {
                    JsonObject productJson = productElement.getAsJsonObject();
                    Product product = parseProduct(productJson);
                    int finalPrice = applyPromotions(product.price(), product.promotions());

                    int savings = product.price() - finalPrice;
                    totalSavings += savings;

                    System.out.println("Product ID: " + product.id());
                    System.out.println("Product Name: " + product.name());
                    System.out.println("Original Price: " + product.price());
                    System.out.println("Final Price after Promotions: " + finalPrice);
                    System.out.println("Savings: " + savings);
                    System.out.println("---------------------------");
                }
            }

            System.out.println("Total Savings: " + totalSavings);

        } catch (IOException e) {
            System.err.println("Error fetching or processing products: " + e.getMessage());
        }
    }

    private Product parseProduct(JsonObject productJson) {
        String id = productJson.get("id").getAsString();
        String name = productJson.get("name").getAsString();
        int price = productJson.get("price").getAsInt();
        JsonArray promotions = productJson.getAsJsonArray("promotions");
        return new Product(id, name, price, promotions);
    }

    private int applyPromotions(int price, JsonArray promotions) {
        if (promotions == null) {
            return price;
        }

        int finalPrice = price;
        for (JsonElement promotionElement : promotions) {
            JsonObject promotion = promotionElement.getAsJsonObject();
            String type = promotion.get("type").getAsString();
            switch (type) {
                case "BUY_X_GET_Y_FREE":
                    finalPrice = applyBuyXGetYFreePromotion(price, promotion);
                    break;
                case "QTY_BASED_PRICE_OVERRIDE":
                    finalPrice = applyQtyBasedPriceOverride(price, promotion);
                    break;
                case "FLAT_PERCENT":
                    finalPrice = applyFlatPercentDiscount(price, promotion);
                    break;
                default:
                    System.err.println("Unknown promotion type: " + type);
            }
        }
        return finalPrice;
    }

    private int applyBuyXGetYFreePromotion(int price, JsonObject promotion) {
        int requiredQty = promotion.get("required_qty").getAsInt();
        int freeQty = promotion.get("free_qty").getAsInt();
        int totalQty = requiredQty + freeQty;
        return (price / requiredQty) * totalQty;
    }

    private int applyQtyBasedPriceOverride(int price, JsonObject promotion) {
        int requiredQty = promotion.get("required_qty").getAsInt();
        int overridePrice = promotion.get("price").getAsInt();
        return requiredQty > 0 ? overridePrice : price;
    }

    private int applyFlatPercentDiscount(int price, JsonObject promotion) {
        int percent = promotion.get("amount").getAsInt();
        return price - (price * percent / 100);
    }
}
