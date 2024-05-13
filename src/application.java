package src;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Scanner;

public class application {
    private static final String ffmpegPath = "ffmpeg.exe"; //path to ffmpeg
    private static final String ffmpegOption = "-c copy -bsf:a aac_adtstoasc"; //options for ffmpeg
    private static final String output = "test.mp4"; //output file

    public static void main(String[] args) {
        String link;
        application app = new application();
        Scanner scanner = new Scanner(System.in);
        System.out.print("1: One Link, 2: More links ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                System.out.print("Enter link: ");
                link = scanner.nextLine();
                app.executeFFmpegCommands(app.getCommand(link, output));
                break;
            case 2:
                System.out.print("How many links?: ");
                int links = scanner.nextInt();
                scanner.nextLine();
                String[] commands = new String[links];

                for (int i = 0; i < links; i++) {
                    System.out.print("Enter link: ");
                    link = scanner.nextLine();
                    commands[i] = app.getCommand(link, String.format(i + output));
                }
                app.executeFFmpegCommands(commands);
        }
    }

    private void executeFFmpegCommands(String... strings) {
        try {
            for (String command : strings) {
                if (!command.isEmpty()) {
                    System.out.println("execute " + command);

                    ProcessBuilder builder = new ProcessBuilder("cmd", "/c", command);
                    builder.redirectErrorStream(true);
                    Process process = builder.start();

                    //read ffmpeg output
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    System.out.println("Output of ffmpeg:");
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }

                    process.waitFor();
                    System.out.println("download complete.");
                }
            }
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("process was terminated " + e.getMessage());
        }
    }

    private String getCommand(String link, String output) {
        return String.format("%s -i \"%s\" %s %s", ffmpegPath, link, ffmpegOption, output);
    }
}
