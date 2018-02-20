package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ArffConverter {

    private String fileName;
    private File exportFile;
    private BufferedWriter bufferedWriter;

    public ArffConverter(String fileName) {
        this.fileName = fileName;
    }

    private void createExportFile() {
        this.exportFile = new File(fileName);
    }

    private void createFileMetadata(Integer nrChannels) throws IOException {

        this.bufferedWriter.write("@relation mean_amplitude\n");

        for (int i = 0; i < nrChannels; i++) {
            this.bufferedWriter.write(String.format("@attribute ch_%d REAL\n", i));
        }
        bufferedWriter.write("@attribute orientation_classes {0,45,90,180,135,225,315,270}\n");
    }

    public void convertData(List<kotlin.Pair<Integer, float[]>> extractedData) {
        try {
            createExportFile();
            FileWriter fileWriter = new FileWriter(exportFile);
            this.bufferedWriter = new BufferedWriter(fileWriter);

            if (extractedData.size() > 0) {
                createFileMetadata(extractedData.get(0).getSecond().length);

                bufferedWriter.write("@data\n");
                extractedData.forEach(data -> {
                    try {
                        float[] means = data.getSecond();
                        for (float mean : means) {
                            bufferedWriter.write(String.format("%.2f,", mean));
                        }
                        bufferedWriter.write(String.format("%d\n", data.getFirst()));
                    } catch (IOException e) {
                        System.out.println("Could not write the mean amplitude in the arff file!!");
                    }
                });
                bufferedWriter.flush();
            } else {
                throw new IOException();
            }

            fileWriter.close();
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("Could not generate the arff file");
        }
    }
}
