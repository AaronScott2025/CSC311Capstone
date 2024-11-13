package com.example.csc311capstone.App;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.api.SafetySetting.HarmBlockThreshold;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GeminiTest {
    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI("root-bank-441518-t9", "us-central1"); ) {
            var textsi_1 = "You are a life counselor/life coach. I want you to ask these questions one by one. When you are done asking the questions, I want you to give advice on how the person should plan out their future and link web resources to aid them.\nMake sure to ask one question at a time and wait for a response from the user before providing the next question. Be sure to tailor the questions based on the first response\n\nHere are the questions:\n" +
                    "Are you currently a student or do you work?\n" +
                    "Do you feel your current job or major fully aligns with your long-term career goals?\n" +
                    "How satisfied are you with the opportunities for growth and advancement in your current location?\n" +
                    "Are you challenged and inspired by the work or studies you’re currently pursuing?\n" +
                    "How important is your current community and social network to your happiness?\n" +
                    "Do you find that your city/region offers activities and amenities that suit your lifestyle?\n" +
                    "How often do you feel excited and motivated by your current job or studies?\n" +
                    "Would you describe the cost of living in your area as sustainable for the lifestyle you want?\n" +
                    "Is there a city or region you’ve always felt drawn to or considered moving to?\n" +
                    "How well does your current environment (city, school, or workplace) support your physical and mental health?\n" +
                    "Do you see yourself still enjoying this job or major five years from now?\n" +
                    "Does your location offer adequate networking and career development opportunities in your field?\n" +
                    "Do you often feel like you’re missing out on new career or educational opportunities in your current environment?\n" +
                    "How strong is your support network where you are now? Would it be easy to replicate or build anew somewhere else?\n" +
                    "Do you feel financially secure in your current job, major, or city?\n" +
                    "Are there industries, career paths, or schools elsewhere that might offer better opportunities in your field?\n" +
                    "How easy is it for you to travel to or access new experiences and perspectives from where you live?\n" +
                    "Does your location provide the quality of life (weather, activities, community) that you want?\n" +
                    "Do you often imagine your ideal future in a different city or region?\n" +
                    "Do you find your current work or studies meaningful and rewarding, or do they feel mostly obligatory?\n" +
                    "Would changing jobs, schools, or cities help you achieve personal or professional goals you’re not able to meet now?";

            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder()
                            .setMaxOutputTokens(8192)
                            .setTemperature(1F)
                            .setTopP(0.95F)
                            .build();

            List<SafetySetting> safetySettings = Arrays.asList(
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build()
            );

            var systemInstruction = ContentMaker.fromMultiModalData(textsi_1);
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemini-1.5-flash-002")
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .setSafetySettings(safetySettings)
                            .setSystemInstruction(systemInstruction)
                            .build();

            // For multi-turn responses, start a chat session.
            ChatSession chatSession = model.startChat();

            // Create a scanner to read user input from the console
            Scanner scanner = new Scanner(System.in);

            GenerateContentResponse response;
            response = chatSession.sendMessage(ContentMaker.fromMultiModalData("Begin"));
            String aiResponse = ResponseHandler.getText(response);
            System.out.println("AI: " + aiResponse);

            // Continue the conversation until the AI provides advice
            while (true) {
                System.out.print("Your response: ");
                String userInput = scanner.nextLine();
                response = chatSession.sendMessage(ContentMaker.fromMultiModalData(userInput));
                aiResponse = ResponseHandler.getText(response);
                System.out.println("AI: " + aiResponse);

                // Check if the AI is providing advice, indicating the end of questions
                if (aiResponse.toLowerCase().contains("here's some advice") ||
                        aiResponse.toLowerCase().contains("based on your responses") ||
                        aiResponse.toLowerCase().contains("i recommend") ||
                        aiResponse.toLowerCase().contains("let's discuss your results") ||
                        aiResponse.toLowerCase().contains("now, let's move on to advice")) {
                    break;
                }
            }

            // Optionally, read the final advice from the AI
            // You can continue to interact if needed
            // System.out.println("AI: " + aiResponse);

            // Close the scanner
            scanner.close();
        }
    }
}
