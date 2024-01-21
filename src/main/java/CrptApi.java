import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CrptApi {
   private String signature;
   private Document doc;

   private List<Long> timeStamp= new ArrayList<>();
   volatile long seconds;
   volatile int requestLimit;
   volatile private int curRequests;

    public CrptApi(Document doc, String signature, long seconds, int requestLimit) {
        this.doc = doc;
        this.signature = signature;
        this.seconds = seconds;
        this.requestLimit = requestLimit;
    }
    public void sendRequest() throws JsonProcessingException {
        if(accessChecker())    {
        ObjectMapper mapToJson = new ObjectMapper();
        String body = mapToJson.writeValueAsString(doc);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(doc)))
                .build();
        }
    }


    // this method try to make this class thread-safe
    synchronized boolean accessChecker()   {
           long curTime = System.currentTimeMillis();
           timeStamp.add(curTime);
           if(timeStamp.size() > 1 && Math.abs(timeStamp.get(timeStamp.size() - 1) - timeStamp.get(timeStamp.size() - 2)) < seconds*100 ) {
               if(timeStamp.size() > requestLimit)  {
                   try {
                       Thread.sleep(seconds*100);
                       timeStamp.clear();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
               }
           }
           return true;
    }

    // in fact this is just entity for json
    public class Document {

        String participantInn;
        String doc_id;
        String doc_status;
        String doc_type;
        boolean importRequest;
        String owner_inn;
        String participant_inn;
        String producer_inn;
        String production_date;
        String production_type;
        String certificate_document;
        String certificate_document_date;
        String certificate_document_number;
        String tnved_code;
        String uit_code;
        String uitu_code;
        String reg_date;
        String reg_number;

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;

        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public void setCertificate_document(String certificate_document) {
            this.certificate_document = certificate_document;
        }

        public void setCertificate_document_date(String certificate_document_date) {
            this.certificate_document_date = certificate_document_date;
        }

        public void setCertificate_document_number(String certificate_document_number) {
            this.certificate_document_number = certificate_document_number;
        }

        public void setTnved_code(String tnved_code) {
            this.tnved_code = tnved_code;
        }

        public void setUit_code(String uit_code) {
            this.uit_code = uit_code;
        }

        public void setUitu_code(String uitu_code) {
            this.uitu_code = uitu_code;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }
        @Override
        public String toString() {
            String res =  "description :"   +
            "{" +  "participantInn:"  + participant_inn + "}," +  "doc_id: " + doc_id + ",doc_status:" + doc_status +
                ",doc_type:" +doc_type +",importRequest:" + importRequest +
                                    ", owner_inn:" + owner_inn + ",participant_inn:" + participant_inn + ",producer_inn:" +
                                     producer_inn + ",production_date:" + production_date +  ",production_type:" +  production_type + ",products:" + "[" +  "{" + "certificate_document:"
                    + certificate_document +  ",certificate_document_date:"  + certificate_document_date +
                                    ",certificate_document_number:" + certificate_document_number + ",owner_inn:" + owner_inn +
                                    ", producer_inn:"  + producer_inn +  ",production_date:" +  production_date +
                                  ",tnved_code:" + tnved_code +  ",uit_code:"  + uit_code + ",uitu_code:" + uitu_code + "}" +   "]" +
                    ",reg_date:" + reg_date + ",reg_number:" + reg_number + "}" ;
            return res;
    }





    }
}