package com.example.csc311capstone.App;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GIPITYTEST {

    // Static messages array to maintain conversation history
    private static JSONArray messages = new JSONArray();

    public static void main(String[] args) {
        // Initialize scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Load environment variables
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("OpenAI_Key"); // API key goes here

        // Define the system message with your instructions
        String systemMessage = """
            You are a life guru and career coach. Your goal is to help users reflect deeply on their life path, career, and personal goals by asking the following questions one by one. Tailor each question and the discussion that follows based on the user's current life stage—whether they're a student, early-career professional, established professional, or transitioning between roles. Offer relevant advice at each stage, and at the end, provide resources and action steps based on their responses. Make sure to ask only one question at a time and wait for the user's response before proceeding.
            First Question : Where are you in life right now? Are you a student/ in early career / late career/ somewhere inbetween? <- Make this sound more professional.
            Understanding the User's Achievements:

                Where are you at in life right now (student, early career, late career, etc.)?
                                                                                                           What do you enjoy most about what you currently do?
                                                                                                           What frustrates you the most about your current studies/career/environment?
                                                                                                           Do you feel like your current path aligns with your personal values?
                                                                                                           What excites you about the future in your current field or location?
                                                                                                           Are you learning new things regularly, or do you feel stagnant?
                                                                                                           Do you feel a sense of purpose in what you’re doing now?
                                                                                                           If you could change one thing about your current situation, what would it be?
                                                                                                           Do you feel like your skills and strengths are being used effectively?
                                                                                                           How often do you feel motivated to tackle your work/studies?
                                                                                                           Do you see opportunities for growth where you are?
                                                                                                           How much control do you feel you have over your future in your current path?
                                                                                                           How do you feel about the people around you in your current environment (classmates, coworkers, etc.)?
                                                                                                           Are you happy with your work-life balance right now?
                                                                                                           Do you find yourself daydreaming about a different career, field of study, or place to live?
                                                                                                           How supportive are the people in your life of your current goals?
                                                                                                           Do you feel financially secure with the path you’re on?
                                                                                                           When was the last time you felt truly excited or passionate about what you’re doing?
                                                                                                           If you were starting fresh, would you choose the same path again?
                                                                                                           What’s one thing you wish you had more of in your current life (time, support, money, opportunities, etc.)?

            Guidance and Resource Recommendations: When the questions are completed, analyze the user's responses. Tailor guidance to suggest career planning steps, or academic goals that align with their vision. Provide reputable links to resources like online courses, career workshops, or personal development guides relevant to the user's stage in life.
            """;

        // Add the system message to conversation history
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemMessage);
        messages.put(systemMsg);

        // Initial user message to start the conversation
        System.out.println("You: Hello, I'm feeling a bit lost in my career path.");

        // Add the initial user message to conversation history
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", "Hello, I'm feeling a bit lost in my career path.");
        messages.put(userMsg);

        // Call the chatGPT method to get the assistant's first response
        String assistantResponse = chatGPT(apiKey, messages);

        // Print the assistant's response
        System.out.println("Assistant: " + assistantResponse);

        // Start the interactive conversation loop
        while (true) {
            // Get user input
            System.out.print("You: ");
            String userInput = scanner.nextLine();

            // Exit the conversation if the user types 'exit' or 'quit'
            if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                System.out.println("Conversation ended.");
                break;
            }

            // Add user's response to conversation history
            userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", userInput);
            messages.put(userMsg);

            // Get assistant's response
            assistantResponse = chatGPT(apiKey, messages);

            // Print assistant's response
            System.out.println("Assistant: " + assistantResponse);
        }

        // Close the scanner
        scanner.close();
    }

    public static String chatGPT(String apiKey, JSONArray messages) {
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-4o"; // Current model of ChatGPT API

        try {
            // Build the JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", model);
            jsonBody.put("messages", messages);

            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Send the request body
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            writer.write(jsonBody.toString());
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the response and extract the assistant's reply
            String assistantReply = extractContentFromResponse(response.toString());

            // Add assistant's response to conversation history
            JSONObject assistantMsg = new JSONObject();
            assistantMsg.put("role", "assistant");
            assistantMsg.put("content", assistantReply);
            messages.put(assistantMsg);

            return assistantReply;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method extracts the assistant's reply from the API response
    public static String extractContentFromResponse(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray choices = jsonResponse.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        String content = message.getString("content");
        return content.trim();
    }
}
