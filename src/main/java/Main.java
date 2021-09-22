import java.io.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("KK:mm:ss,SSS");
        Charset charset = StandardCharsets.UTF_8;
        List<String> originalLines, newLines = new ArrayList();
        File input, output;
        String result = "";
        int millisOffset, count = 0;
        boolean isLookingForTimes = false;

        try {
            input = new File(args[0]);
            output = new File(args[1]);
            millisOffset = Integer.parseInt(args[2]);

            if (args.length > 3) {
                charset = Charset.forName(args[3]);
            }

            originalLines = IOUtils.read(input, charset);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("You didn't include the proper arguments as parameters!");
            System.out.println("The order is as follows: input_file output_file millis_offset charset");
            System.out.println("(charset will default to UTF-8 if none is provided)");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < originalLines.size(); i++) {
            if (isLookingForTimes) {
                String[] timesStr = originalLines.get(i).split(" --> ");
                Date startTime, endTime;

                try {
                    startTime = format.parse(timesStr[0]);
                    endTime = format.parse(timesStr[1]);
                } catch (ParseException e) {
                    System.out.println("The timestamp seems to be formatted incorrectly on line " + (i + 1) + ".");
                    e.printStackTrace();
                    return;
                }

                startTime = new Date(startTime.toInstant().plusMillis(millisOffset).toEpochMilli());
                endTime = new Date(endTime.toInstant().plusMillis(millisOffset).toEpochMilli());
                newLines.add(format.format(startTime) + " --> " + format.format(endTime));

                count++;
                isLookingForTimes = false;
            } else {
                try {
                    if (Integer.parseInt(originalLines.get(i)) == count + 1) {
                        isLookingForTimes = true;
                    }
                } catch (NumberFormatException e) {
                    isLookingForTimes = false;
                }

                newLines.add(originalLines.get(i));
            }

            result += newLines.get(i) + System.lineSeparator();
        }

        try {
            IOUtils.write(output, result, charset);
            System.out.println(count + " subtitles were found and subsequently offset.");
            System.out.println("If this number is wrong from the true number of subtitles, something went wrong.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
