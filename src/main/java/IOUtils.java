import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class IOUtils {

    public static List<String> read(File file, Charset charset) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, charset);
        BufferedReader br = new BufferedReader(isr);
        List<String> result = new ArrayList<>();
        String buffer;

        while ((buffer = br.readLine()) != null) {
            result.add(buffer);
        }

        br.close();
        isr.close();
        fis.close();
        return result;
    }

    public static void write(File file, String info, Charset charset) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
        BufferedWriter bw = new BufferedWriter(osw);

        verifyFilePathExists(file);

        bw.write(info);
        bw.close();
        osw.close();
        fos.close();
    }

    public static void verifyFilePathExists(File file) {
        StringBuilder directoryBuilder = new StringBuilder();
        String path = file.getAbsolutePath().replaceAll("\\\\", "/");
        String[] folderSeparation = path.split("/");
        File directory;

        for (int i = 0; i < folderSeparation.length - 1; i++) {
            directoryBuilder.append(folderSeparation[i]);
            directoryBuilder.append("/");
        }

        directory = new File(directoryBuilder.toString());

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

}
