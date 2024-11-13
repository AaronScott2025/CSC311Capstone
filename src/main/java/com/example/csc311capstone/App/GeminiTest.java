package com.example.csc311capstone.App;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

// Import statements for PDF generation
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
// Use PDType0Font for Unicode support
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class GeminiTest {
    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI("root-bank-441518-t9", "us-central1"); ) {

            // Define the questions for students and non-students
            List<String> studentQuestions = Arrays.asList(
                    "Do you feel your current major fully aligns with your long-term career goals?",
                    "Are you challenged and inspired by the studies you’re currently pursuing?",
                    "How satisfied are you with the opportunities for growth and advancement in your school?",
                    "How important is your current campus community and social network to your happiness?",
                    "Do you find that your university offers activities and amenities that suit your lifestyle?",
                    "How often do you feel excited and motivated by your current studies?",
                    "Is there a university or program you’ve always felt drawn to or considered transferring to?",
                    "How well does your current environment (city, school) support your physical and mental health?",
                    "Do you see yourself still enjoying this major five years from now?",
                    "Does your school offer adequate networking and career development opportunities in your field?",
                    "Do you often feel like you’re missing out on educational opportunities in your current environment?",
                    "Do you feel financially secure in your current major or city?",
                    "Are there schools elsewhere that might offer better opportunities in your field?",
                    "Does your location provide the quality of life (weather, activities, community) that you want?",
                    "Do you often imagine your ideal future in a different city or university?",
                    "Do you find your current studies meaningful and rewarding, or do they feel mostly obligatory?",
                    "Would changing majors or universities help you achieve personal or professional goals you’re not able to meet now?"
            );

            List<String> nonStudentQuestions = Arrays.asList(
                    "Do you feel your current job fully aligns with your long-term career goals?",
                    "Are you challenged and inspired by the work you’re currently doing?",
                    "How satisfied are you with the opportunities for growth and advancement in your current location?",
                    "How important is your current community and social network to your happiness?",
                    "Do you find that your city/region offers activities and amenities that suit your lifestyle?",
                    "How often do you feel excited and motivated by your current job?",
                    "Would you describe the cost of living in your area as sustainable for the lifestyle you want?",
                    "Is there a city or region you’ve always felt drawn to or considered moving to?",
                    "How well does your current environment (city, workplace) support your physical and mental health?",
                    "Do you see yourself still enjoying this job five years from now?",
                    "Does your location offer adequate networking and career development opportunities in your field?",
                    "Do you often feel like you’re missing out on new career opportunities in your current environment?",
                    "Do you feel financially secure in your current job or city?",
                    "Are there industries or career paths elsewhere that might offer better opportunities in your field?",
                    "How easy is it for you to travel to or access new experiences and perspectives from where you live?",
                    "Does your location provide the quality of life (weather, activities, community) that you want?",
                    "Do you often imagine your ideal future in a different city or region?",
                    "Do you find your current work meaningful and rewarding, or does it feel mostly obligatory?",
                    "Would changing jobs or cities help you achieve personal or professional goals you’re not able to meet now?"
            );

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

            // Set up the model
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemini-1.5-flash-002")
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .setSafetySettings(safetySettings)
                            .build();

            // Start a chat session
            ChatSession chatSession = model.startChat();

            // Create a scanner to read user input from the console
            Scanner scanner = new Scanner(System.in);

            // StringBuilder to collect the conversation and advice text
            StringBuilder conversationText = new StringBuilder();

            // Ask if the user is a student
            System.out.println("AI: Are you a student?");
            conversationText.append("AI: Are you a student?\n");
            System.out.print("Your response: ");
            String studentResponse = scanner.nextLine();
            conversationText.append("Your response: ").append(studentResponse).append("\n");

            // Determine which set of questions to use
            boolean isStudent = studentResponse.equalsIgnoreCase("yes");
            List<String> questions = isStudent ? studentQuestions : nonStudentQuestions;

            // Collect user responses
            List<String> userResponses = new ArrayList<>();

            // Loop through the questions
            for (String question : questions) {
                System.out.println("AI: " + question);
                conversationText.append("AI: ").append(question).append("\n");
                System.out.print("Your response: ");
                String userInput = scanner.nextLine();
                conversationText.append("Your response: ").append(userInput).append("\n");
                userResponses.add(userInput);
            }

            // Prepare the prompt for the AI to generate advice
            StringBuilder advicePrompt = new StringBuilder();
            advicePrompt.append("Based on the following responses, provide advice on how the person should plan out their future and link web resources to aid them.\n\n");
            advicePrompt.append("Responses:\n");
            for (int i = 0; i < questions.size(); i++) {
                advicePrompt.append(questions.get(i)).append("\n");
                advicePrompt.append("User's response: ").append(userResponses.get(i)).append("\n\n");
            }

            // Send the advice prompt to the AI
            GenerateContentResponse response = chatSession.sendMessage(ContentMaker.fromMultiModalData(advicePrompt.toString()));
            String aiAdvice = ResponseHandler.getText(response);
            System.out.println("\nAI Advice:\n" + aiAdvice);

            // Append the advice to the conversation text
            conversationText.append("\nAI Advice:\n").append(aiAdvice).append("\n");

            // Close the scanner
            scanner.close();

            // Generate PDF with the conversation and advice
            System.out.println("Generating PDF...");
            generatePdf(conversationText.toString());

            System.out.println("Advice has been saved to advice.pdf");
        }
    }

    // Method to generate PDF using Apache PDFBox
    private static void generatePdf(String contentText) {
        PDDocument document = new PDDocument();
        try {
            // Load a font that supports Unicode
            PDType0Font font = PDType0Font.load(document, GeminiTest.class.getResourceAsStream("/fonts/FreeSerif.ttf"));

            // Split content into pages if necessary
            String[] lines = contentText.split("\\r?\\n");
            float fontSize = 12;
            float leading = 1.5f * fontSize;
            PDRectangle pageSize = PDRectangle.LETTER;
            float margin = 50;
            float width = pageSize.getWidth() - 2 * margin;
            float startY = pageSize.getHeight() - margin;

            // Initialize variables for text placement
            PDPageContentStream contentStream = null;
            float yPosition = startY;

            for (int i = 0; i < lines.length; i++) {
                String text = lines[i];

                if (contentStream == null) {
                    PDPage page = new PDPage(pageSize);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, fontSize);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                }

                List<String> wrappedLines = wrapLine(text, font, fontSize, width);
                for (String line : wrappedLines) {
                    if (yPosition <= margin) {
                        // End the current content stream and start a new page
                        contentStream.endText();
                        contentStream.close();
                        PDPage page = new PDPage(pageSize);
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(font, fontSize);
                        contentStream.beginText();
                        yPosition = startY;
                        contentStream.newLineAtOffset(margin, yPosition);
                    }
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -leading);
                    yPosition -= leading;
                }
                // Add extra space between paragraphs
                contentStream.newLineAtOffset(0, -leading);
                yPosition -= leading;
            }

            // End the last content stream
            if (contentStream != null) {
                contentStream.endText();
                contentStream.close();
            }

            // Save the document
            document.save("advice.pdf");
        } catch (IOException e) {
            System.err.println("Exception while trying to create PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                System.err.println("Exception while trying to close PDF document: " + e.getMessage());
            }
        }
    }

    // Utility method to wrap a line of text
    private static List<String> wrapLine(String text, PDType0Font font, float fontSize, float width) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (font.getStringWidth(line + word) / 1000 * fontSize > width) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            if (line.length() > 0) {
                line.append(" ");
            }
            line.append(word);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines;
    }
}
