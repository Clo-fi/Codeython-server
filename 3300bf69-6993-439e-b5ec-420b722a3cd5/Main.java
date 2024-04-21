import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        Solution s = new Solution();
        ObjectMapper mapper = new ObjectMapper();
System.out.print(mapper.writeValueAsString(s.solution(mapper.readValue(args[0], int.class),mapper.readValue(args[1], int[].class))));}}