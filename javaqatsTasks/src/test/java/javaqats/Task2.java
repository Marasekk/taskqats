package javaqats;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.*;
import java.io.*;
import java.time.*;
public class Task2 {
    ObjectMapper map = new ObjectMapper(); //Mapper initialization
    int timeLimitMs = 150;
    @Test void TestCase1(){
        try{
        URL url = new URL("https://reqres.in/api/users?page=2");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        assertEquals(con.getResponseCode(), 200);
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream())); //deserialization
        String l = br.readLine();
        br.close();
        JsonNode node = map.readTree(l);  //mapping the received response
        ArrayNode data = (ArrayNode)node.get("data");
        assertEquals(12, node.get("total").asInt(), "Total count ("+node.get("total").asInt()+") is not 12") ;
        assertEquals("Lawson", data.get(0).get("last_name").textValue(), "Last name of the first person in response is not correct");
        assertEquals("Ferguson", data.get(1).get("last_name").textValue(), "Last name of the second person in response is not correct");
        assertEquals(node.get("total").asInt(), data.size(), "Total count value ("+node.get("total").intValue()+") is not matching the length of the data array ("+data.size()+") (because only records from second page were fetched)");
        }
        catch(Exception e)
        {}
    }
    static int[] getInputLength() {
        ObjectMapper map = new ObjectMapper();
        int [] input;
        try {
            String path = "./src/test/java/javaqats/task2_external_data.json";
            String out = new String(Files.readAllBytes(Paths.get(path)));
            JsonNode node = map.readTree(out);  //Mapping the JSON object read from the file in path
            ArrayNode data = (ArrayNode)node.get("data"); //Getting the data array from the JSON object
            input = new int[data.size()];
            for(int i = 0; i < data.size(); i++)
                input[i] = i;
        }
        catch (Exception er)
        {
            return  null;
        }
        return  input;
    }
    JsonNode getRecordFromJson(int i){
        ArrayNode data;
        try {
            String path = "./src/test/java/javaqats/task2_external_data.json";
            String out = new String(Files.readAllBytes(Paths.get(path)));  //Reading the JSON file as string
            JsonNode node = map.readTree(out);  //Mapping the JSON object read from the file in path
            data = (ArrayNode)node.get("data"); //Getting the data array from the JSON object
            if (data == null)
                throw new RuntimeException("JsonNode did not initialize");
        }
        catch (Exception er)
        {
            return  null;
        }
        return data.get(i);//Returns a JSON object from the data array according to the index value
    }
    @ParameterizedTest(name ="TestCase2() [{index}]")
    @MethodSource("getInputLength")
    void TestCase2(int i){
        try{
            JsonNode data = getRecordFromJson(i);
            assertNotNull(data, "Received JSON object is null");
            System.out.println(i);
            long start = System.currentTimeMillis();
            URL url = new URL("https://reqres.in/api/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "application/json; charset=UTF-8");
            OutputStream os = con.getOutputStream(); //Creating a stream for the HTTP request
            os.write(data.asText().getBytes(StandardCharsets.UTF_8));//asText returns the JSON object as a string, getBytes - serialization
            System.out.println((data.get("name")!= null && data.get("job") != null));
            System.out.println(data.get("name"));
            if(data.get("name")!= null && data.get("job") != null)
                assertEquals(201, con.getResponseCode(), "Status code is not 201");
            else
                assertEquals(400, con.getResponseCode(), "Status code is not 400");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String l = br.readLine();
            br.close();
            long end = System.currentTimeMillis();
            JsonNode node2 = map.readTree(l); //Mapping the response
            System.out.println(node2);
            LocalDate date = LocalDate.now();
            assertEquals(Integer.toString(date.getYear()), node2.get("createdAt").textValue().substring(0,4), "Record was not created this year");
            System.out.println(node2.get("id").asInt());
            assertTrue(end - start < timeLimitMs, " Response time ("+(end-start)+"ms) is longer than declared time limit ("+timeLimitMs+"ms)");
            assertEquals(0, node2.get("id").asInt(), "Id number does not match. Id is generated, no sense to make assertion of value." );
        }
        catch(Exception e)
        {}
    }
}
