package com.example.csc311capstone.Controllers;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatWindowController {

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;

    // Conversation history
    private JSONArray messages = new JSONArray();

    // OpenAI API key
    private String apiKey;

    @FXML
    private void initialize() {
        // Load environment variables
        Dotenv dotenv = Dotenv.configure().load();
        apiKey = dotenv.get("OpenAI_Key"); // Ensure your .env file contains OpenAI_Key

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

        // Auto-scroll to bottom whenever new messages are added
        dialogContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(1.0);
        });

        // Send the initial message from the AI assistant
        sendInitialMessage();
    }

    /**
     * Sends the initial message from the AI assistant when the window opens.
     */
    private void sendInitialMessage() {
        // Disable send button and user input during processing
        sendButton.setDisable(true);
        userInput.setDisable(true);

        // Create a Task to handle the API call asynchronously
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                // The assistant needs a user prompt to start the conversation
                // We'll send a default user message to initiate the dialogue
                String initialUserMessage = "Hello";
                JSONObject userMsg = new JSONObject();
                userMsg.put("role", "user");
                userMsg.put("content", initialUserMessage);
                messages.put(userMsg);

                return chatGPT(apiKey, messages);
            }

            @Override
            protected void succeeded() {
                String assistantResponse = getValue();

                // Add assistant's response to conversation history
                JSONObject assistantMsg = new JSONObject();
                assistantMsg.put("role", "assistant");
                assistantMsg.put("content", assistantResponse);
                messages.put(assistantMsg);

                // Display assistant's response
                Label aiLabel = new Label("Assistant: " + assistantResponse);
                aiLabel.setWrapText(true);
                aiLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10; -fx-background-radius: 5;");
                aiLabel.maxWidthProperty().bind(scrollPane.widthProperty().subtract(25));
                dialogContainer.getChildren().add(aiLabel);

                // Re-enable send button and user input
                sendButton.setDisable(false);
                userInput.setDisable(false);
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                throwable.printStackTrace();

                Label errorLabel = new Label("Assistant: Sorry, an error occurred.");
                errorLabel.setWrapText(true);
                errorLabel.setStyle("-fx-background-color: pink; -fx-padding: 10; -fx-background-radius: 5;");
                errorLabel.maxWidthProperty().bind(scrollPane.widthProperty().subtract(25));
                dialogContainer.getChildren().add(errorLabel);

                // Re-enable send button and user input
                sendButton.setDisable(false);
                userInput.setDisable(false);
            }
        };

        // Run the task in a background thread
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void handleSend() {
        String input = userInput.getText().trim();
        if (!input.isEmpty()) {
            // Display user's message
            Label userLabel = new Label("You: " + input);
            userLabel.setWrapText(true);
            userLabel.setStyle("-fx-background-color: lightblue; -fx-padding: 10; -fx-background-radius: 5;");
            userLabel.maxWidthProperty().bind(scrollPane.widthProperty().subtract(25));
            dialogContainer.getChildren().add(userLabel);

            // Add user's response to conversation history
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", input);
            messages.put(userMsg);

            // Clear user input
            userInput.clear();

            // Disable send button and user input during processing
            sendButton.setDisable(true);
            userInput.setDisable(true);

            // Create a Task to handle the API call asynchronously
            Task<String> task = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return chatGPT(apiKey, messages);
                }

                @Override
                protected void succeeded() {
                    String assistantResponse = getValue();

                    // Add assistant's response to conversation history
                    JSONObject assistantMsg = new JSONObject();
                    assistantMsg.put("role", "assistant");
                    assistantMsg.put("content", assistantResponse);
                    messages.put(assistantMsg);

                    // Display assistant's response
                    Label aiLabel = new Label("Assistant: " + assistantResponse);
                    aiLabel.setWrapText(true);
                    aiLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10; -fx-background-radius: 5;");
                    aiLabel.maxWidthProperty().bind(scrollPane.widthProperty().subtract(25));
                    dialogContainer.getChildren().add(aiLabel);

                    // Re-enable send button and user input
                    sendButton.setDisable(false);
                    userInput.setDisable(false);
                }

                @Override
                protected void failed() {
                    Throwable throwable = getException();
                    throwable.printStackTrace();

                    Label errorLabel = new Label("Assistant: Sorry, an error occurred.");
                    errorLabel.setWrapText(true);
                    errorLabel.setStyle("-fx-background-color: pink; -fx-padding: 10; -fx-background-radius: 5;");
                    errorLabel.maxWidthProperty().bind(scrollPane.widthProperty().subtract(25));
                    dialogContainer.getChildren().add(errorLabel);

                    // Re-enable send button and user input
                    sendButton.setDisable(false);
                    userInput.setDisable(false);
                }
            };

            // Run the task in a background thread
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    private void handleGeneratePDF() {
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();

            // Use a standard font
            PDFont font = PDType1Font.HELVETICA;
            PDFont boldFont = PDType1Font.HELVETICA_BOLD;

            // Initialize variables for text placement
            float leading = 14.5f;
            PDPageContentStream contentStream = null;
            float yPosition = PDRectangle.LETTER.getHeight() - 50;
            int pageCount = 0;

            // Prepare content
            List<String> lines = new ArrayList<>();

            // Build content from conversation history
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String role = message.getString("role");
                String content = message.getString("content");

                if (role.equals("user") || role.equals("assistant")) {
                    String prefix = role.substring(0, 1).toUpperCase() + role.substring(1) + ": ";
                    String fullMessage = prefix + content;
                    lines.add(fullMessage);
                    lines.add(""); // Add empty line for spacing
                }
            }

            // Add final summary if needed
            // If the last assistant message is the final summary, you might want to highlight it
            // You can implement logic here to detect and format the final summary

            for (String line : lines) {
                // Create new page if needed
                if (contentStream == null || yPosition < 50) {
                    if (contentStream != null) {
                        contentStream.endText();
                        contentStream.close();
                    }
                    PDPage page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    yPosition = PDRectangle.LETTER.getHeight() - 50;
                    pageCount++;
                }

                // Check if the line is a user or assistant message
                if (line.startsWith("You:")) {
                    contentStream.setFont(boldFont, 12);
                } else if (line.startsWith("Assistant:")) {
                    contentStream.setFont(font, 12);
                } else {
                    contentStream.setFont(font, 12);
                }

                // Split long lines
                List<String> wrappedLines = wrapText(line, 80);

                for (String wrappedLine : wrappedLines) {
                    contentStream.showText(wrappedLine);
                    contentStream.newLineAtOffset(0, -leading);
                    yPosition -= leading;

                    // Add new page if needed
                    if (yPosition < 50) {
                        contentStream.endText();
                        contentStream.close();
                        PDPage page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(font, 12);
                        contentStream.newLineAtOffset(50, PDRectangle.LETTER.getHeight() - 50);
                        yPosition = PDRectangle.LETTER.getHeight() - 50;
                        pageCount++;
                    }
                }
            }

            // Close the content stream
            if (contentStream != null) {
                contentStream.endText();
                contentStream.close();
            }

            // Save the document
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(sendButton.getScene().getWindow());
            if (file != null) {
                document.save(file);
                System.out.println("PDF generated successfully at " + file.getAbsolutePath());
            }

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to wrap text into lines of a specified length
    private List<String> wrapText(String text, int maxLength) {
        List<String> lines = new ArrayList<>();
        String[] paragraphs = text.split("\n");

        for (String paragraph : paragraphs) {
            while (paragraph.length() > maxLength) {
                int splitAt = paragraph.lastIndexOf(" ", maxLength);
                if (splitAt == -1) splitAt = maxLength;
                lines.add(paragraph.substring(0, splitAt));
                paragraph = paragraph.substring(splitAt).trim();
            }
            lines.add(paragraph);
        }

        return lines;
    }

    @FXML
    private void handleExit() {
        // Close the chat window
        sendButton.getScene().getWindow().hide();
    }

    private String chatGPT(String apiKey, JSONArray messages) {
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo"; // Or any other available model

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
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return "Sorry, an error occurred while contacting the assistant.";
        }
    }

    // This method extracts the assistant's reply from the API response
    private String extractContentFromResponse(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray choices = jsonResponse.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        return message.getString("content").trim();
    }
}
