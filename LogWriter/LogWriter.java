package LogWriter;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogWriter {
    public final static String STANDARD = "dd-MM-yyyy hh:mm:ss/SSS";

    private final Logger logger;
    private final String logDataName;
    private final SimpleDateFormat sp = new SimpleDateFormat();

    /**
     * Create a Logger
     * @param pattern the pattern for the file, like dd-MM-yyyy
     * @throws ParseException
     */
    public LogWriter(String pattern) throws ParseException {
        this.logger = Logger.getLogger("LOG");
        pattern = pattern.replaceAll("/|:|\s", "_");
        sp.applyPattern(pattern);
        logDataName = sp.format(new Date());
    }

    /**
     * Log smth
     * @param lvl the log level
     * @param msg the message to log
     * @return this LogWriter - can be ignored
     */
    public LogWriter log(Level lvl, String msg) {
        msg = String.format("%s",msg);
        msg = msg.replaceAll("\n",String.format("\n%s", String.format("%s: ", lvl.getLocalizedName())));
        logger.log(lvl, msg);
        try {
            this.appendToLog(lvl, msg);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to create file: " + String.format("%s.txt",logDataName));
        }

        return this;
    }

    /**
     * Append smth to the file
     * @param lvl the level of the log before
     * @param msg the message like before
     * @throws IOException
     */
    private void appendToLog(Level lvl, String msg) throws IOException {
        String tillNow = "";
        try {
            tillNow = getAll();
        } catch (Exception e)  {
            logger.log(Level.INFO, "Data is empty!");
        }

        msg = String.format("%s %s",lvl.getLocalizedName(),msg);

        FileWriter writer = new FileWriter(String.format("%s.txt",logDataName));

        tillNow.chars().forEach(chr -> {
            try {
                writer.write(chr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        msg.chars().forEach(chr -> {
            try {
                writer.write(chr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        writer.flush();
        writer.close();
    }

    /**
     * get all log till now in the file
     * @return all the logs of the file
     * @throws FileNotFoundException
     */
    private String getAll() throws FileNotFoundException {
        String str = "";

        Scanner sc = new Scanner(new FileReader(String.format("%s.txt",logDataName)));

        while (sc.hasNextLine()) {
            str += String.format("%s\n",sc.nextLine());
        }

        return str;
    }
}