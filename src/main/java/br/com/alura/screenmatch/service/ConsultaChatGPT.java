package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {

    static GetKeyFromProperties getKeyFromProperties = new GetKeyFromProperties();

        public static String obterTraducao(String texto) {

            String apiKey = getKeyFromProperties.obterKeyValue("CHATGPT_APIKEY");

            OpenAiService service = new OpenAiService(apiKey);
            CompletionRequest requisicao = CompletionRequest.builder()
                    .model("text-davinci-003")
                    .prompt("traduza para o português o texto: " + texto)
                    .maxTokens(1000)
                    .temperature(0.7)
                    .build();

            var resposta = service.createCompletion(requisicao);
            return resposta.getChoices().get(0).getText();
        }
    }