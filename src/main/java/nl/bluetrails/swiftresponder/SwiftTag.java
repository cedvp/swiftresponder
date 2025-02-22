package nl.bluetrails.swiftresponder;

import java.util.ArrayList;
import java.util.List;

public class SwiftTag {

    public boolean subcodeonlyone(String cleanline){
        List<String> subcodeswithonlyoneslash = new ArrayList<>();
        subcodeswithonlyoneslash.add(":COLA/");
        subcodeswithonlyoneslash.add(":REAG/");
        subcodeswithonlyoneslash.add(":DEAG/");
        return subcodeswithonlyoneslash.contains(cleanline.substring(5,11));
    }

    public boolean line_has_subcode(String cleanline){
        return cleanline.substring(4,6).contains("::");
    }

    public int lengthOfSubCode(String cleanline){
        if(line_has_subcode(cleanline) && subcodeonlyone(cleanline)){
            return 6;
        } else if (line_has_subcode(cleanline)){
            return 7;
        } else {
            return 0;
        }
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagSubCode() {
        return tagSubCode;
    }

    public void setTagSubCode(String tagSubCode) {
        this.tagSubCode = tagSubCode;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    String tagCode;
    String tagSubCode;
    String tagValue;
    public SwiftTag(String line){
        int beginOfValue;
        int beginOfSubCode;
        String cleanline = line.trim();
        tagCode = cleanline.substring(0,5);
        if(line_has_subcode(cleanline)){
            tagSubCode = cleanline.substring(5,6+lengthOfSubCode(cleanline)-1);
        }
        beginOfValue = 5+lengthOfSubCode(cleanline);
        tagValue = cleanline.substring(beginOfValue);
    }
}
