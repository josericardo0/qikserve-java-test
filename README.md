## To run the API, first make sure you have downloaded the wiremock from: https://cdn-dev.preoday.com/test/mock-api.zip

## Once downloaded, unzip and find the paste named "wiremock", double click the start.sh to run the wiremock, so you can run the API from your IDE.

This project uses the GSON library from Google. To make it work, first you will need to do the following instructions:

#### IntelliJ IDEA:

1. Open your IntelliJ IDEA project.

2. Navigate to "File" > "Project Structure".

3. In the Project Structure dialog, select "Modules" on the left sidebar.

4. Click on the "Dependencies" tab.

5. Click the "+" button and select "JARs or directories" or "Library -> Select Library Type -> From Maven.

6. Locate the Gson JAR file that you downloaded and select it.

7. Click "OK" to add the Gson library to your project.

#### Eclipse:

1. Open your Eclipse project.

2. Right-click on your project in the Project Explorer and select "Properties".

3. In the Properties dialog, navigate to "Java Build Path".

4. Select the "Libraries" tab.

5. Click on "Add External JARs..." if you downloaded Gson as a JAR file.

6. Locate the Gson JAR file in your file system and select it.

7. Click "Apply and Close" to add the Gson library to your project.

## The thought process

1. Problem Understanding: The task is to create a Java application that interacts with a Wiremock server to fetch product data, apply promotions, calculate savings, and display the results.
2. Setup URL and Connection: We start by defining the URL of the Wiremock server (WIREMOCK_URL). Then, we create a URL object and establish an HttpURLConnection to connect to the server.
3. Fetching Data: We use a BufferedReader to read the response from the server. The response is then parsed into a JsonArray using Gson's JsonParser.
4. Processing Products: We iterate over each product in the JsonArray. For each product, we extract its ID, name, price, and promotions (if any).
5. Applying Promotions: If the product has promotions, we apply them based on their type. The promotion logic is implemented in separate methods (applyPromotions, applyBuyXGetYFreePromotion, applyQtyBasedPriceOverride, applyFlatPercentDiscount).
6. Calculating Savings: We calculate the savings for each product by subtracting the final price after promotions from the original price.
7. Displaying Results: Finally, we print out the details of each product along with its original price, final price after promotions, and savings. We also accumulate the total savings across all products and display it at the end.
8. Exception Handling: We handle IOException to catch any errors that might occur during the network request or data processing.
9. Overall, the code follows a structured approach, separating concerns into methods for better readability and maintainability. It leverages Gson library for JSON parsing and handles potential exceptions gracefully.


## Follow up questions

1. How long did you spend on the test? What would you add if you had more time?

Answer: I spent a day thinking about the solution, according to the information and instructions provided and about 1 hour to implement the code. If I had more time, I would develop more error handling and also do unit tests for each scenario.


2. What was the most useful feature that was added to the latest version of your chosen language? Please include a snippet of code that shows how you've used it.


Answer: One of the most useful features in the latest version of Java is Record. I used it in the test to facilitate the encapsulation of data in a single entity called Product. Below are the images showing the use of Record in the code:
![image](https://github.com/josericardo0/qikserve-java-test/assets/92414548/d401bb9b-24a3-402b-8c10-65d9a5f5b52f)
![image](https://github.com/josericardo0/qikserve-java-test/assets/92414548/f971f766-5f78-4c57-8c4b-1f33f72278e0)


3. What did you find most difficult?


Answer: What I found most difficult was arriving at the solution without developing an API with Spring Boot, but using Java's own libraries and resources from the language itself.


4. What mechanism did you put in place to track down issues in production on this code? If you didn’t put anything, write down what you could do.


Answer: I haven't implemented anything to track problems in production in the code, but what I would definitely do is implement logs with Log4j when requests are made to the external API, and know what data is received, and any errors encountered during processing.


6. The Wiremock represents one source of information. We should be prepared to integrate with more sources. List the steps that we would need to take to add more sources of items with diferent formats and promotions.


Answer: To prepare for integrating with more sources of items with different formats and promotions, here are the steps we would need to take:

1. Define a Common Data Model: Create a common data model that can represent products and promotions from different sources. This model should be flexible enough to accommodate variations in data formats and promotions.
2. Abstract Data Access Layer: Implement an abstract data access layer that can fetch data from various sources, such as databases, APIs, or flat files. This layer should provide a consistent interface for accessing product and promotion data, regardless of the underlying source.
3. Implement Source-specific Adapters: Develop adapters or connectors for each data source to retrieve product and promotion data in their respective formats. These adapters should translate the data from each source into the common data model defined in step 1.
4. Handle Data Transformation: Handle data transformation logic within the adapters to map the fields and formats from each source to the corresponding fields in the common data model. This may involve parsing JSON, XML, or other data formats and converting them into objects.
5. Normalize Promotions: Normalize promotion data across different sources to ensure consistency. For example, if one source uses "discount_percent" and another uses "percentage_off", map both to a common field like "discountPercentage" in the common data model.
6. Handle Promotion Variations: Identify variations in promotion types and formats across different sources. Define a set of standard promotion types and formats, and map the variations from each source to these standards.
7. Update Processing Logic: Update the processing logic in your application to handle data from multiple sources. Modify the code to retrieve data from the abstract data access layer and process it using the common data model.
8. Test with Mock Data: Test the integration with mock data from different sources to ensure that the adapters correctly retrieve and transform the data into the common format. Use tools like WireMock to simulate responses from various sources during testing.
9. Integration Testing: Conduct integration testing to validate that the application can seamlessly integrate with multiple data sources and handle different formats and promotions effectively.
10. Error Handling and Logging: Implement robust error handling and logging mechanisms to capture and log errors encountered during data retrieval and processing. Monitor logs during testing and production to identify any issues or discrepancies.
11 .Documentation and Maintenance: Document the integration process, including data formats, mappings, and configurations, to facilitate future maintenance and updates. Regularly review and update the integration as new sources or changes occur.


