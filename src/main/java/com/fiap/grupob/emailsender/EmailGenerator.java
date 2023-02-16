package com.fiap.grupob.emailsender;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailGenerator {

    static final List<String>  DEFAULT_KEYS = Arrays.asList("destination-email","destination-name","subject","mail-template","data-exchange");

    /**
     * Formato do JSON de entrada, em rabbitMessage:
     * {
     *     "destination-email": "",
     *     "destination-name": "",
     *     "subject": "",
     *     "mail-template": "",
     *     "data-exchange": {"_chave_":"valor"}
     * }
     * Exemplo útil:
     *  {
     *   "destination-email": "williandrade@outlook.com",
     *   "destination-name": "Willian Andrade",
     *   "subject": "Sistema isGood",
     *   "mail-template": "1",
     *   "data-exchange": {"_tratamento_":"garoto"}
     *  }
     * @param rabbitMessage
     * @throws Exception
     */

    public EmailGenerator(String rabbitMessage) throws Exception{
        JSONObject data;
        try{
            data = new JSONObject(rabbitMessage.trim());
        } catch (Exception e){
            throw new Exception(String.format("Não foi possível entender o conteúdo enviado. \n%s", rabbitMessage));
        }

        //Validando as chaves do json informado
        if (!data.keySet().containsAll(DEFAULT_KEYS)){
            log("DATASET NÃO APROVADO");
            throw new Exception(String.format("Não foi possível validar o dataset com as chaves necessárias. "+
                    "Verifique o conteúdo informado.\nDados informados: %s\nChaves esperadas: %s",
                    rabbitMessage, DEFAULT_KEYS));
        }

        //Forma o conteúdo do e-mail de acordo com o mail-template:
        String mailContent = getMailContent(
                data.getString("mail-template"), data.getJSONObject("data-exchange")
        );

        log(String.format("Conteúdo do e-mail a ser enviado: %s", mailContent));

        //Enviando e-mail
        TLSEmailEngine.sendByTLS(data.getString("destination-email"),
                data.getString("subject"),
                mailContent);

    }

    private String getMailContent(String mailTemplate, JSONObject dataExchange) throws Exception {
        String ret = "";
        switch (mailTemplate) {
            case "1" :
                ret = "<h2>Exemplo de e-mail enviado pelo sistema isGood</h2>\n<p>Objetivo cumprido com sucesso _tratamento_!</p>";
                break;
            default:
                throw new Exception(String.format("Template e e-mail [%s] não configurada.", mailTemplate));
        }

        //Subistituindo os termos na template pelos termos do campo dataExchange

        for (Iterator<String> keys = dataExchange.keys(); keys.hasNext(); ) {
            String keyStr = keys.next();
            ret = ret.replaceAll(keyStr, dataExchange.getString(keyStr));

        }
        dataExchange.keySet().forEach(keyStr -> {
        });
        return ret;
    }

    private static void log (String msg){
        Logger.getLogger("dev").log(Level.INFO,msg);
    }
}
