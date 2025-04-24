package nl.bluetrails.swiftresponder;
/**
 Copyright [2025] [Cedric Van Pelt]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **/
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
