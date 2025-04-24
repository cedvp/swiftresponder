package nl.bluetrails.swiftresponder;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class App {
    public static void main(String[]args){
        System.out.println("this responds to swift messages");
    }

    public static String LINEBREAK = System.lineSeparator();
    public static HashMap<String,String > getSwiftResponse(String rawSwiftMessage) throws IOException, MissingTagException {
    HashMap<String,String>responses = new HashMap<>();


        SwiftMT swiftMTIncoming = new SwiftMT(rawSwiftMessage);
        String incomingType = rawSwiftMessage.substring(33,36);
        String sender = rawSwiftMessage.substring(6,18);
        String receiver12 = rawSwiftMessage.substring(36,47);
        HashMap<String,String> messagesVSSettlements = new HashMap<>();
        messagesVSSettlements.put("540","544");
        messagesVSSettlements.put("541","545");
        messagesVSSettlements.put("542","546");
        messagesVSSettlements.put("543","547");
        if (messagesVSSettlements.containsKey(incomingType)) {



            //info needed from incoming messages:
            String _20c_seme = null;
            String _20c_comm = null;
            String _20c_pool = null;
            String _22f_setr = null;
            String _98a_trad = null;
            String _98a_sett = null;
            String _90a_ = null;
            String _90b_ = null;
            String _35b_ = null;
            String _36b_ = null;
            String _70e_ = null;
            String _97a_ = null;
            String _97a_CASH = null;

            String _95p_ = null;
            String _95r_deag_ = null;
            String _95r_reag_ = null;
            String _19a_ = null;


            //new way of parsing messages
            boolean skiptag90 = false;
            _20c_seme = swiftMTIncoming.getTagbyAnything(":20C:",":SEME//").getTagValue();
            _20c_comm = swiftMTIncoming.getTagbyAnything(":20C:",":COMM//").getTagValue();
            _20c_pool = swiftMTIncoming.getTagbyAnything(":20C:",":POOL//").getTagValue();
            _22f_setr = swiftMTIncoming.getTagbyAnything(":22F:",":SETR//").getTagValue();
            _98a_trad = swiftMTIncoming.getTagbyAnything(":98A:",":TRAD//").getTagValue();
            _98a_sett = swiftMTIncoming.getTagbyAnything(":98A:",":SETT//").getTagValue();
            if(!Objects.isNull(swiftMTIncoming.getTagbyAnything(":90A:",null))){
                String _90a = swiftMTIncoming.getTagbyAnything(":90A:",null).getTagValue();
            } else if (!Objects.isNull(swiftMTIncoming.getTagbyAnything(":90B:",null))){
                String _90b = swiftMTIncoming.getTagbyAnything(":90B:",null).getTagValue();
            }
            if (Objects.isNull(swiftMTIncoming.getTagbyAnything(":90B:",null)) && Objects.isNull(swiftMTIncoming.getTagbyAnything(":90A:",null))){
                skiptag90 = true;
            }
            _70e_ = swiftMTIncoming.getTagbyAnything(":70E:",null).getTagValue();
            _35b_ = swiftMTIncoming.getTagbyAnything(":35B:",null).getTagValue();
            _36b_ = swiftMTIncoming.getTagbyAnything(":36B:",null).getTagValue();
           /** if(!Objects.isNull(swiftMTIncoming.getTagbyAnything(":70E:",null))){
                //_70e_ = swiftMTIncoming.getTagbyAnything(":70E:",null).getTagValue();
            }
            **/
            _97a_ = swiftMTIncoming.getTagbyAnything(":97A:",null).getTagValue();
            _95p_ = swiftMTIncoming.getTagbyAnything(":95P:",null).getTagValue();
            _95r_reag_ = swiftMTIncoming.getTagbyAnything(":95R:",":REAG/").getTagValue();
            _95r_deag_ = swiftMTIncoming.getTagbyAnything(":95R:",":DEAG/").getTagValue();

            //cash only
            String _97a_CASH_str = null;
            String _19a_str = null;
            if (isCash(incomingType)){
                _97a_CASH_str = swiftMTIncoming.getTagbyAnything(":97A:","null").getTagValue();
                _19a_str = swiftMTIncoming.getTagbyAnything(":19A:",null).getTagValue();
            }


            HashMap<String,String> fieldsNeededFromIncoming = new HashMap<>();
            fieldsNeededFromIncoming.put("_20c_seme",_20c_seme);
            fieldsNeededFromIncoming.put("_20c_comm",":COMM//"+_20c_comm);
            fieldsNeededFromIncoming.put("_20c_pool",":POOL//"+_20c_pool);
            fieldsNeededFromIncoming.put("_98a_trad",_98a_trad);
            fieldsNeededFromIncoming.put("_98a_sett",_98a_sett);
            fieldsNeededFromIncoming.put("_90a_",_90a_);
            fieldsNeededFromIncoming.put("_90b_",_90b_);
            fieldsNeededFromIncoming.put("_35b_",_35b_);
            fieldsNeededFromIncoming.put("_36b_",_36b_);
            fieldsNeededFromIncoming.put("_70e_",_70e_);
            fieldsNeededFromIncoming.put("_97a_",_97a_);
            fieldsNeededFromIncoming.put("_95p_",_95p_);
            fieldsNeededFromIncoming.put("_95r_reag",_95r_reag_);
            fieldsNeededFromIncoming.put("_95r_deag",_95r_deag_);
            fieldsNeededFromIncoming.put("_19a_",_19a_str);
            fieldsNeededFromIncoming.put("_97a_CASH",_97a_CASH);


            //content final answer
            String headerLine = createHeaderResponse(incomingType,sender,receiver12, messagesVSSettlements.get(incomingType));
            String responseline=":16R:GENL"+LINEBREAK;
            responseline+=":20C::SEME//CH"+System.currentTimeMillis()+System.lineSeparator().trim()+LINEBREAK;
            responseline+=":23G:NEWM"+LINEBREAK;
            responseline+=":98C::PREP//CH"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+System.lineSeparator().trim()+LINEBREAK;
            responseline+=":16R:LINK"+LINEBREAK;
            responseline+=":13A::LINK//"+incomingType+LINEBREAK;
            responseline+=":20C::RELA//"+_20c_seme+LINEBREAK;
            responseline+=":16S:LINK"+LINEBREAK;
            responseline+=":16R:LINK"+LINEBREAK;
            responseline+=":20C::COMM//"+_20c_comm+LINEBREAK;
            responseline+=":16S:LINK"+LINEBREAK;
            responseline+=":16R:LINK"+LINEBREAK;
            responseline+=":20C::POOL//"+_20c_pool+LINEBREAK;
            responseline+=":16S:LINK"+LINEBREAK;
            responseline+=":16S:GENL"+LINEBREAK;
            responseline+=":16R:TRADDET"+LINEBREAK;
            responseline+=":98A::TRAD//"+_98a_trad+LINEBREAK;
            responseline+=":98A::SETT//"+_98a_sett+LINEBREAK;
            String _98a_eset = _98a_sett.replace("SETT","ESET");
            responseline+=":98A::ESET//"+_98a_eset+LINEBREAK;
            //here we should populate 90A if 90A is populated, 90B if 90B is populated
            if(!skiptag90 && null!= _90a_){
                responseline+=":90A::DEAL//"+_90a_+LINEBREAK;
            } else if (!skiptag90 && null != _90b_){
                responseline+=":90B::DEAL//"+_90b_+LINEBREAK;
            }
            responseline+=":35B:"+_35b_+LINEBREAK;
            responseline+=":16R:FIA"+LINEBREAK;
            responseline+=":70E::FIAN//POSTYP/NO"+LINEBREAK;
            responseline+=":16S:FIA"+LINEBREAK;

            responseline+=":70E:"+":SPRO//"+_70e_+LINEBREAK;
            responseline+="/MOVTTYP/CSRT"+LINEBREAK;
            responseline+=":16S:TRADDET"+LINEBREAK;
            responseline+=":16R:FIAC"+LINEBREAK;
            String _36b_estt = _36b_.replace("SETT","ESTT");
            responseline+=":36B:"+":ESTT//"+_36b_estt+LINEBREAK;
            responseline+=":97A:"+":SAFE//"+_97a_+LINEBREAK;
            if(isCash(incomingType)){
                responseline+=":97A:"+_97a_CASH+LINEBREAK;
            }
            responseline+=":16S:FIAC"+LINEBREAK;
            responseline+=":16R:SETDET"+LINEBREAK;
            responseline+=":22F:"+":SETR/"+_22f_setr+LINEBREAK;
            responseline+=":22F:"+":STCO//NOMC"+LINEBREAK;
            responseline+=":16R:SETPRTY"+LINEBREAK;
            responseline+=":95P:"+":PSET//"+_95p_+LINEBREAK;
            responseline+=":16S:SETPRTY"+LINEBREAK;
            responseline+=":16R:SETPRTY"+LINEBREAK;
            responseline+=":95R:"+":REAG//"+_95r_reag_+LINEBREAK;
            responseline+=":16S:SETPRTY"+LINEBREAK;
            responseline+=":16R:SETPRTY"+LINEBREAK;
            responseline+=":95R:"+":DEAG//"+_95r_deag_+LINEBREAK;
            responseline+=":20C:"+"PROC//9809230"+LINEBREAK;
            responseline+=":70E:"+"DECL///CPORDR/CH1701448154677"+LINEBREAK;
            responseline+=":16S:SETPRTY"+LINEBREAK;
            if(isCash(incomingType)){
                if(null == _19a_str){
                    throw new MissingTagException("_19a_ missing");
                }
                String _19a_estt = _19a_str.replace("SETT","ESTT");
                responseline+=":16R:AMT"+LINEBREAK;
                responseline+=":19A:ESTT//"+_19a_estt+LINEBREAK;
                responseline+=":16S:AMT"+LINEBREAK;
            }
            responseline+=":16S:SETDET"+LINEBREAK;
            responseline+="-}";

            responses.put("ack", AutoSettleMT548Preparator.getMT548Response(incomingType,fieldsNeededFromIncoming,false, sender, receiver12));
            responses.put("match", AutoSettleMT548Preparator.getMT548Response(incomingType,fieldsNeededFromIncoming,true, sender, receiver12));
            responses.put("settle", headerLine+responseline);
            return responses;
        } else {
            return null;
        }
    }


    public static String createHeaderResponse(String incomingType, String sender, String receiver12, String responsetype){


            String headerblock1 = "{1:F01";
                headerblock1+=sender;
                headerblock1+="0001";
                headerblock1+="000001";
                headerblock1+="}";
            String headerblock2 = "{2:O";
                headerblock2+=responsetype;
                headerblock2+="0000000000";
                headerblock2+=receiver12;
                headerblock2+="0000000000";

                headerblock2+=new SimpleDateFormat("yyMMdd").format(new Date());
                headerblock2+=new SimpleDateFormat("HHmm").format(new Date());
                headerblock2+="N}";
            String headerblock3 = "{3:{113:XXXX}{108:J3H7RA3018459519}}{4:";
            return headerblock1+headerblock2+headerblock3.trim()+LINEBREAK;
    }








    public static boolean isPay(String incomingType){
        return Arrays.asList("541","542").contains(incomingType);
    }

    public static boolean isCash(String incomingType){
        return Arrays.asList("541","543").contains(incomingType);
    }
}



class AutoSettleMT548Preparator {
    static String LINEBREAK = System.lineSeparator();

    public static String getMT548Response(String incomingType, HashMap<String, String> fieldsFromIncoming, boolean isForMatch, String sender, String receiver12) {

        String headerLine = App.createHeaderResponse(incomingType,sender,receiver12, "548").trim();
        String responseline=":16R:GENL"+LINEBREAK;
        responseline+=":20C::SEME//CH"+System.currentTimeMillis()+System.lineSeparator().trim()+LINEBREAK;
        responseline+=":23G:INST"+LINEBREAK;
        responseline+=":98C::PREP//CH"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+System.lineSeparator().trim()+LINEBREAK;
        responseline+=":16R:LINK"+LINEBREAK;
        responseline+=":13A::LINK//"+incomingType+LINEBREAK;
        responseline+=":20C::RELA//"+fieldsFromIncoming.get("_20c_seme")+LINEBREAK;
        responseline+=":16S:LINK"+LINEBREAK;
        responseline+=":16R:LINK"+LINEBREAK;
        responseline+=":20C:"+fieldsFromIncoming.get("_20c_comm")+LINEBREAK;
        responseline+=":16S:LINK"+LINEBREAK;
        responseline+=":16R:LINK"+LINEBREAK;
        responseline+=":20C:"+fieldsFromIncoming.get("_20c_pool")+LINEBREAK;
        responseline+=":16S:LINK"+LINEBREAK;
        responseline+=":16R:STAT"+LINEBREAK;
        if(!isForMatch){
            responseline+=":25D::IPRC//PACK"+LINEBREAK;
            responseline+=":16R:REAS"+LINEBREAK;
            responseline+=":24B::PACK//NARR"+LINEBREAK;
            responseline+=":70D:REAS///STATUS/101"+LINEBREAK;
            responseline+="POSTYP/NO"+LINEBREAK;
            responseline+=":16S:REAS"+LINEBREAK;
        } else {
            responseline+=":25D::MTCH//MACH"+LINEBREAK;
        }
        responseline+=":16S:STAT"+LINEBREAK;
        responseline+=":16S:GENL"+LINEBREAK;
        responseline+=":16S:SETTRAN"+LINEBREAK;
        responseline+=":35B:"+fieldsFromIncoming.get("_35b_")+LINEBREAK;
        responseline+=":36B:"+fieldsFromIncoming.get("_36b_")+LINEBREAK;
        if(App.isCash(incomingType)){
            responseline+=":19A:"+fieldsFromIncoming.get("_19a_")+LINEBREAK;
        }
        responseline+=":97A:"+fieldsFromIncoming.get("_97a_")+LINEBREAK;
        responseline+=":22F::SETR//TRAD"+LINEBREAK;
        if (App.isPay(incomingType)) {
            responseline+=":22H::REDE//DELI"+LINEBREAK;

        } else {
            responseline+=":22H::REDE//RECE"+LINEBREAK;
        }
        if(App.isCash(incomingType)){
            responseline+=":22H::PAYM//APMT"+LINEBREAK;
        } else {
            responseline+=":22H::PAYM//FREE"+LINEBREAK;
        }

        responseline+=":22F::STCO//NOMC"+LINEBREAK;
        responseline+=":98A:"+":SETT//"+fieldsFromIncoming.get("_98a_sett")+LINEBREAK;
        responseline+=":98A:"+":TRAD//"+fieldsFromIncoming.get("_98a_trad")+LINEBREAK;
        responseline+=":70E:"+fieldsFromIncoming.get("_70e_")+LINEBREAK;
        responseline+=":16R:SETPRTY"+LINEBREAK;
        responseline+=":95P:"+fieldsFromIncoming.get("_95p_")+LINEBREAK;
        responseline+=":16S:SETPRTY"+LINEBREAK;
        responseline+=":16R:SETPRTY"+LINEBREAK;
        responseline+=":95R:"+fieldsFromIncoming.get("_95r_deag")+LINEBREAK;
        responseline+=":16S:SETPRTY"+LINEBREAK;
        responseline+=":16R:SETPRTY"+LINEBREAK;
        responseline+=":95R:"+fieldsFromIncoming.get("_95r_reag")+LINEBREAK;
        responseline+=":16S:SETPRTY"+LINEBREAK;
        responseline+=":16S:SETTRAN"+LINEBREAK;
        responseline+="-}";
        return headerLine+LINEBREAK+responseline;

    }
}
