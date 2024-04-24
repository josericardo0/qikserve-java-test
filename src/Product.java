import com.google.gson.JsonArray;

public record Product(String id, String name, int price, JsonArray promotions) {}
