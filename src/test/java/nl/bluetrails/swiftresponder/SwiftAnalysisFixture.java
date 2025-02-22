package nl.bluetrails.swiftresponder;

import org.concordion.api.ConcordionFixture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@ConcordionFixture
public class SwiftAnalysisFixture {

    public static String readFileAsString(String fileName) throws IOException {
        // Get the class of the caller
        Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass();

        try (InputStream is = callerClass.getResourceAsStream(fileName)) {
            if (is == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public String getMT5xx(String filename) throws IOException {
        return readFileAsString(filename);
    }

    public String getresponsefirstXchars(String input, String howmany){
     return input.substring(0,Integer.valueOf(howmany));
    }

    public String getHeaderBlock1(String lineHeader){

        return lineHeader.substring(0,29);
    }


    public String getHeaderBlock2MessageType(String lineHeader){

        return lineHeader.substring(32,36);
    }




    public String getresponse(String input, String typeresponse) throws IOException {
        return SwiftResponder.getSwiftResponse(input).get(typeresponse);

    }

    public int countLines(String input){
        return input.split("\r\n|\r|\n").length;
    }

    public List<String> getLines(String input){
        return Arrays.asList(input.split("\\R"));
    }

    public String getElement(List<String> list, String number){
        return list.get(Integer.valueOf(number));
    }


    public String getMT540() throws IOException {
        return readFileAsString("mt540input.txt");
    }

    public String plus1(String input) throws IOException {
        int x = Integer.valueOf(input);
        x++;
        return String.valueOf(x);
    }
}
