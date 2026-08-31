package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.chat.ChatResponseDTO;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.ProductRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Product-recommendation chatbot backed by Google Gemini (free tier).
 * <p>
 * The whole product catalog is sent with the user's problem; the model replies
 * with a short message and a list of product IDs. Those IDs are then resolved
 * against the DB, so the client only ever receives real catalog products
 * (the model cannot invent products).
 */
@Service
public class ChatBotService {

    private static final String SYSTEM_RULES = """
            Ești asistentul virtual al magazinului Terra Bucovina, care vinde remedii florale și produse naturale.
            Pe baza problemei descrise de client, recomanzi între 1 și 3 produse POTRIVITE, alese DOAR din catalogul primit.
            Răspunzi în limba română, prietenos și concis. Include mereu, pe scurt, faptul că aceste produse nu înlocuiesc
            sfatul unui medic. Răspunzi STRICT în format JSON, fără alt text, cu structura:
            {"reply": "text pentru client", "productIds": [numere]}
            Folosești DOAR id-uri care există în catalog. Dacă nimic nu se potrivește, pui productIds gol ([]) și explici politicos.
            """;

    private final ProductRepo productRepo;
    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models}")
    private String apiUrl;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String model;

    public ChatBotService(ProductRepo productRepo, WebClient.Builder webClientBuilder) {
        this.productRepo = productRepo;
        this.webClient = webClientBuilder.build();
    }

    public ChatResponseDTO recommend(String userMessage) {
        List<Product> catalog = productRepo.findAllByActiveTrue();

        try {
            String requestBody = buildGeminiRequest(buildCatalogText(catalog), userMessage);

            String raw = webClient.post()
                    .uri(apiUrl + "/" + model + ":generateContent")
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(java.time.Duration.ofSeconds(25));

            // Gemini wraps the answer at candidates[0].content.parts[0].text
            JsonNode root = mapper.readTree(raw);
            String text = root.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText("");

            JsonNode parsed = mapper.readTree(text);   // our {reply, productIds}
            String reply = parsed.path("reply").asText("");

            List<Integer> ids = new ArrayList<>();
            parsed.path("productIds").forEach(n -> ids.add(n.asInt()));

            List<ChatResponseDTO.RecommendedProduct> recommended = ids.stream()
                    .map(id -> productRepo.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .map(p -> new ChatResponseDTO.RecommendedProduct(
                            p.getId(), p.getName(), p.getPrice(), p.getMainImageUrl()))
                    .toList();

            if (reply.isBlank()) {
                reply = "Îți recomand produsele de mai jos. Reține că acestea nu înlocuiesc sfatul unui medic.";
            }
            return new ChatResponseDTO(reply, recommended);

        } catch (Exception e) {
            if (e instanceof WebClientResponseException wcre) {
                System.err.println("CHATBOT ERROR: " + wcre.getStatusCode()
                        + " (model=" + model + ") - " + wcre.getResponseBodyAsString());
            } else {
                System.err.println("CHATBOT ERROR: " + e.getMessage());
            }
            return new ChatResponseDTO(
                    "Îmi pare rău, nu am putut procesa cererea acum. Te rog încearcă din nou.",
                    List.of());
        }
    }

    private String buildCatalogText(List<Product> catalog) {
        StringBuilder sb = new StringBuilder();
        for (Product p : catalog) {
            sb.append("#").append(p.getId())
              .append(" | ").append(p.getName());
            if (p.getShortDesc() != null && !p.getShortDesc().isBlank()) {
                sb.append(" | ").append(p.getShortDesc());
            }
            if (p.getIngredients() != null && !p.getIngredients().isBlank()) {
                sb.append(" | ingrediente: ").append(p.getIngredients());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String buildGeminiRequest(String catalogText, String userMessage) throws Exception {
        String userText = "CATALOG:\n" + catalogText + "\nPROBLEMA CLIENTULUI:\n" + userMessage;

        var body = new java.util.LinkedHashMap<String, Object>();
        body.put("systemInstruction", java.util.Map.of(
                "parts", List.of(java.util.Map.of("text", SYSTEM_RULES))));
        body.put("contents", List.of(java.util.Map.of(
                "role", "user",
                "parts", List.of(java.util.Map.of("text", userText)))));
        body.put("generationConfig", java.util.Map.of(
                "temperature", 0.3,
                "responseMimeType", "application/json"));

        return mapper.writeValueAsString(body);
    }
}
