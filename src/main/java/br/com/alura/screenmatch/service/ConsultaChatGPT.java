package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

    public class ConsultaChatGPT {
        public static String obterTraducao(String texto) {

            OpenAiService service = new OpenAiService("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");

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