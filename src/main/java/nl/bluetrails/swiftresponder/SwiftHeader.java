package nl.bluetrails.swiftresponder;

public class SwiftHeader {
    String fullValue;

    public String getFullValue() {
        return fullValue;
    }

    public void setFullValue(String fullValue) {
        this.fullValue = fullValue;
    }

    public SwiftHeader(String rawline){
    fullValue = rawline;

}
}
