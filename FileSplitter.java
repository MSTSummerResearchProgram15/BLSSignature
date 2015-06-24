import java.io.*;

/**
 * Created by Dylan on 6/24/2015.
 */
public class FileSplitter {
    int blockSize;
    String filePath;
    String splitFileName;

    public FileSplitter(String filePath){
        this.filePath = filePath;
    }

    // splits one file into multiple files of size blockSize(bytes) into \output folder, returns number of files, -1 if fail
    public int splitFile(String fileName, String splitFileName, int blockSize) throws FileNotFoundException {
        int numberOfFiles = 1;
        String PathName = "output\\".concat(splitFileName);
        File file = new File(fileName);
        InputStream is = new FileInputStream(file);
        byte[] temp = new byte[blockSize];
        try{
            while (is.available() >= blockSize) {

                is.read(temp);
                OutputStream os = new FileOutputStream(PathName.concat(Integer.toString(numberOfFiles)).concat(".txt"));
                numberOfFiles++;
                os.write(temp);
            }
            byte[] temp2 = new byte[is.available()];
            is.read(temp2);
            OutputStream os = new FileOutputStream(PathName.concat(Integer.toString(numberOfFiles)).concat(".txt"));
            os.write(temp);
        } catch(Exception IOException){
            IOException.printStackTrace();
            return -1;
        }
        return numberOfFiles;
    }
}
