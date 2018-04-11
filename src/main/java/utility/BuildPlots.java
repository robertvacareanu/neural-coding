package utility;

// in order to generate plots

import data_conversion.file_utility.FeatureType;
import data_conversion.file_utility.SpikesTypes;
import data_conversion.file_utility.Timestamps;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static data_conversion.file_utility.FileNamesAndLocations.RESULTS_COMPARED_EXPORt_FOLDER;

class PlottingService {

    private void runPlottingScript(String plottingCommand) {
        try {
            Process p = Runtime.getRuntime().exec(plottingCommand);
            p.waitFor();
            System.out.println("Done plotting!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildPlottingCommand(int orientation1, String sourceFile1,
                                        int orientation2, String sourceFile2,
                                        Timestamps timestamp, SpikesTypes spikesType) {

        String scriptPath = "python_scripts/lda_plots_double.py";
        String PYTHON = "python";
        String MEAN_AMPLITUDE = "MA";
        String FIRE_RATE = "FR";


        return String.format(
                "%s %s %s %s %s %s %s %s %s %s %s %s",
                PYTHON,
                scriptPath,
                sourceFile1,
                orientation1,
                orientation2,
                MEAN_AMPLITUDE + "_" + orientation1 + "_" + orientation2, // title
                sourceFile2,
                orientation1,
                orientation2,
                FIRE_RATE + "_" + orientation1 + "_" + orientation2, //title
                RESULTS_COMPARED_EXPORt_FOLDER,
                String.format("%s_%s_%s_%s_%s_%s", spikesType.getDescription(), MEAN_AMPLITUDE, FIRE_RATE, timestamp.getTimeStamp(), orientation1, orientation2)
        );
    }

    private String getFileName(SpikesTypes spikesType, FeatureType featureType, Timestamps timestamp) {

        return String.format("data/%s_%s_%s_M017.CSV",
                spikesType.getDescription(),
                featureType.getFeatureDesc(),
                timestamp.getTimeStamp());
    }

    private void buildPlotsForTimestampsAndOrientations(List<SpikesTypes> spikeTypes,
                                                        Pair<FeatureType, FeatureType> features,
                                                        List<Timestamps> timestamps,
                                                        List<Integer> orientations) {

        for (Integer i = 0; i < orientations.size(); i++) {
            for (Integer j = i + 1; j < orientations.size(); j++) {

                int orientation1 = orientations.get(i) * 45;
                int orientation2 = orientations.get(j) * 45;

                for (SpikesTypes spkType : spikeTypes) {
                    for (Timestamps tms : timestamps) {

                        String fileName1 = getFileName(spkType, features.getKey(), tms);
                        String fileName2 = getFileName(spkType, features.getValue(), tms);

                        String plotCommand = buildPlottingCommand(orientation1,
                                fileName1,
                                orientation2,
                                fileName2,
                                tms,
                                spkType);

                        runPlottingScript(plotCommand);
                    }
                }
            }
        }
    }

    public void makeComparablePlots() {

        List<SpikesTypes> spikesTypes = Collections.singletonList(SpikesTypes.UNSORTED_SPIKES);
        Pair<FeatureType, FeatureType> features = new Pair<>(FeatureType.MEAN_AMP, FeatureType.FIRE_RATE);
        List<Timestamps> timestamps = Arrays.asList(Timestamps.BEFORE, Timestamps.BETWEEN, Timestamps.AFTER);

        List<Integer> orientations = IntStream.range(0, 4)
                .boxed()
                .collect(Collectors.toList());

        buildPlotsForTimestampsAndOrientations(spikesTypes, features, timestamps, orientations);
    }
}


public class BuildPlots {

    public static void main(String[] args) {
        new PlottingService().makeComparablePlots();
    }

}
