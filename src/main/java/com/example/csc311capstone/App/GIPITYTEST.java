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

            Question: "What are you most proud of in your life right now?"
            Contextual Tip: For students, consider their academic or personal growth; for professionals, consider career milestones.

            Fulfillment in Their Path:

            Question: "How fulfilled do you feel with your current career or study path?"
            Contextual Tip: Emphasize long-term goals and alignment with passions.

            Identifying Core Values:

            Question: "What are three core values that are most important to you?"
            Contextual Tip: Encourage examples related to current academic focus or career choices.

            Future Visioning:

            Question: "Where do you see yourself in five years?"
            Contextual Tip: Tailor to their stage—students may focus on beginning their career; professionals on advancement or transition.

            Skill Development Focus:

            Question: "What is one skill you wish you could master in the next year?"
            Contextual Tip: Suggest skills relevant to their field, such as coding for tech majors or public speaking for new managers.

            Perfect Day Exercise:

            Question: "What does a ‘perfect day’ look like for you, and how often do you experience it?"
            Contextual Tip: Highlight the connection between their ideal day and real-world career roles.

            Motivation Source Exploration:

            Question: "Are you more motivated by personal growth, career advancement, or making a positive impact?"
            Contextual Tip: Relate to their future goals and their current priorities.

            Barriers to Success:

            Question: "What do you think is holding you back from achieving your full potential?"
            Contextual Tip: Identify study habits for students, workplace challenges for professionals.

            Daily Excitement Levels:

            Question: "How often do you feel excited to start your day?"
            Contextual Tip: Encourage them to think about what brings them joy in school or work.

            Routine Improvements:

            Question: "If you could change one thing about your daily routine, what would it be?"
            Contextual Tip: Students might want study hacks, while professionals may want time management tips.

            Curiosity in New Fields:

            Question: "Is there a field or industry that you’ve always been curious to explore?"
            Contextual Tip: Encourage exploring through internships for students or online courses for professionals.

            Ideal Work Environment:

            Question: "What type of environment or culture do you feel most productive and happy in?"
            Contextual Tip: Students might think about future workplaces; professionals about current or ideal teams.

            Financial vs. Passion Prioritization:

            Question: "How important is financial stability to you compared to pursuing your passions?"
            Contextual Tip: Help them see potential paths that align both financial and personal goals.

            Unlimited Resources Exercise:

            Question: "What would you do if money were no object?"
            Contextual Tip: Encourage them to think creatively, considering both personal dreams and career ambitions.

            Short-Term Goals:

            Question: "What are three things you’d like to accomplish before the end of the year?"
            Contextual Tip: Students might focus on academics; professionals on career or personal achievements.

            Role Model Reflection:

            Question: "Who are your role models, and why do they inspire you?"
            Contextual Tip: Suggest thinking about both famous figures and personal mentors.

            Collaboration Preferences:

            Question: "Do you prefer working with others or independently, and does your current situation align with that?"
            Contextual Tip: Students might think about study groups; professionals about teamwork.

            Location Contentment:

            Question: "Are you content with your current location, or would you consider relocating for better opportunities?"
            Contextual Tip: Tailor to those in school vs. considering job relocations.

            Legacy Goals:

            Question: "What do you want people to remember you for?"
            Contextual Tip: Focus on long-term impact in their field of study or career.

            Balancing Priorities:

            Question: "How do you prioritize work, relationships, and self-care, and would you like that balance to shift?"
            Contextual Tip: Help them explore how to adjust for well-being at each stage of their life.

            Guidance and Resource Recommendations: When the questions are completed, analyze the user's responses and provide a summary. Tailor guidance to suggest career planning steps, self-improvement activities, or academic goals that align with their vision. Provide reputable links to resources like online courses, career workshops, or personal development guides relevant to the user's stage in life.
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
        String model = "gpt-3.5-turbo"; // Current model of ChatGPT API

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
