package nl.bluetrails.swiftresponder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwiftMT {
    SwiftHeader myHeader;

    public SwiftHeader getMyHeader() {
        return myHeader;
    }

    public void setMyHeader(SwiftHeader myHeader) {
        this.myHeader = myHeader;
    }

    public HashMap<String, SwiftTag> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, SwiftTag> tags) {
        this.tags = tags;
    }

    HashMap<String,SwiftTag> tags;

    public HashMap<String, SwiftTag> getTags_without_subcodes() {
        return tags_without_subcodes;
    }

    public void setTags_without_subcodes(HashMap<String, SwiftTag> tags_without_subcodes) {
        this.tags_without_subcodes = tags_without_subcodes;
    }

    HashMap<String,SwiftTag> tags_without_subcodes;
    public SwiftMT(String rawline){
        myHeader = new SwiftHeader(rawline.lines().findFirst().orElse(""));
        rawline = rawline.lines().skip(1).collect(Collectors.joining("\n"));
        tags = new HashMap<>();
        tags_without_subcodes = new HashMap<>();
        for (String currentLine : rawline.lines().toList()) {
            if (!currentLine.equalsIgnoreCase("-}")) {
                SwiftTag currentTag = new SwiftTag(currentLine);
                if(currentTag.line_has_subcode(currentLine)){
                    tags.put(currentTag.getTagCode()+"."+currentTag.getTagSubCode(),new SwiftTag(currentLine.trim()));
                    tags_without_subcodes.put(currentTag.getTagCode(),new SwiftTag(currentLine.trim()));
                           } else {
                    tags.put(currentTag.getTagCode(),new SwiftTag(currentLine.trim()));
                    tags_without_subcodes.put(currentTag.getTagCode(),new SwiftTag(currentLine.trim()));
                  }
             }
        }
        System.out.println("bl");
    }

    SwiftTag getTagbyCodeAndSubCode(String code, String subCode){
        return tags.get(code+"."+subCode);
    }

    SwiftTag getTagbyCode(String code){
        return tags.get(code);
    }

    public SwiftTag getTagbyAnything(String code, String subCode){
        if(Objects.isNull(subCode)){
            if(tags.containsKey(code)){
                return tags.get(code);
            } else if (tags_without_subcodes.containsKey(code)) {
                return tags_without_subcodes.get(code);
            } else{
                return new SwiftTag(":000::"+"TAGNOTFOUND");
                }
        } else  {
            if(tags.containsKey(code+"."+subCode)){
                return tags.get(code+"."+subCode);
            } else{
                return new SwiftTag(":000::"+"TAGNOTFOUND");
            }
        }
    }

    public static <K, V> V getValueByPartialKey(HashMap<K, V> map, String partialKey) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey().toString().contains(partialKey)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
