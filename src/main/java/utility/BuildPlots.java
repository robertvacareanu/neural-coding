package utility;

// in order to generate plots

import static data_conversion.file_utility.FileNamesAndLocations.*;

class PlottingService {

    private final String scriptPath = "python_scripts/lda_plots_double.py";
    private final String PYTHON = "python";

    private final String MEAN_AMPLITUDE = "MA";
    private final String FIRE_RATE = "FR";
    private final String BETWEEN = "BTW";
    private final String BEFORE = "BEF";
    private final String AFTER = "AFT";
    private final String AFTER_OFF = "AFT_OFF_INT";
    private final String AFTER_ON = "AFT_ON_INT";


    private void runPlottingScript(String plottingCommand) {
        try {
            Process p = Runtime.getRuntime().exec(plottingCommand);
            p.waitFor();
            System.out.println("Done plotting!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String MAvsFR_BteweenCommand(int orientation1, int orientation2) {

       return String.format(
                "%s %s %s %s %s %s %s %s %s %s %s %s",
                PYTHON,
                scriptPath,
                MEAN_DATA_BETWEEN_M017_CSV,
                orientation1,
                orientation2,
                MEAN_AMPLITUDE + "_" + orientation1 + "_" + orientation2, // title
                FIRE_RATE_DATA_BETWEEN_M017_CSV,
                orientation1,
                orientation2,
                FIRE_RATE + "_" + orientation1 + "_" + orientation2, //title
                RESULTS_COMPARED_EXPORt_FOLDER,
                String.format("%s_%s_%s_%s_%s", MEAN_AMPLITUDE, FIRE_RATE, BETWEEN, orientation1, orientation2)
        );
    }

    private String MAvsFR_BeforeCommand(int orientation1, int orientation2) {

        return String.format(
                "%s %s %s %s %s %s %s %s %s %s %s %s",
                PYTHON,
                scriptPath,
                MEAN_DATA_BEFORE_M017_CSV,
                orientation1,
                orientation2,
                MEAN_AMPLITUDE + "_" + orientation1 + "_" + orientation2, // title
                FIRE_RATE_DATA_BEFORE_M017_CSV,
                orientation1,
                orientation2,
                FIRE_RATE + "_" + orientation1 + "_" + orientation2, //title
                RESULTS_COMPARED_EXPORt_FOLDER,
                String.format("%s_%s_%s_%s_%s", MEAN_AMPLITUDE, FIRE_RATE, BEFORE, orientation1, orientation2)
        );
    }

    private String MAvsFR_AfterCommand(int orientation1, int orientation2) {

        return String.format(
                "%s %s %s %s %s %s %s %s %s %s %s %s",
                PYTHON,
                scriptPath,
                MEAN_DATA_AFTER_M017_CSV,
                orientation1,
                orientation2,
                MEAN_AMPLITUDE + "_" + orientation1 + "_" + orientation2, // title
                FIRE_RATE_DATA_AFTER_M017_CSV,
                orientation1,
                orientation2,
                FIRE_RATE + "_" + orientation1 + "_" + orientation2, //title
                RESULTS_COMPARED_EXPORt_FOLDER,
                String.format("%s_%s_%s_%s_%s", MEAN_AMPLITUDE, FIRE_RATE, AFTER, orientation1, orientation2)
        );
    }

    private String MAvsFR_AfterStimOffIntervalCommand(int orientation1, int orientation2) {

        return String.format(
                "%s %s %s %s %s %s %s %s %s %s %s %s",
                PYTHON,
                scriptPath,
                MEAN_DATA_AFTER_OFF_TIME_M017_CSV,
                orientation1,
                orientation2,
                MEAN_AMPLITUDE + "_" + orientation1 + "_" + orientation2, // title
                FIRE_RATE_DATA_AFTER_OFF_TIME_M017_CSV,
                orientation1,
                orientation2,
                FIRE_RATE + "_" + orientation1 + "_" + orientation2, //title
                RESULTS_COMPARED_EXPORt_FOLDER,
                String.format("%s_%s_%s_%s_%s", MEAN_AMPLITUDE, FIRE_RATE, AFTER_OFF, orientation1, orientation2)
        );
    }

    private String MAvsFR_AfterStimOnIntervalCommand(int orientation1, int orientation2) {

        return String.format(
                "%s %s %s %s %s %s %s %s %s %s %s %s",
                PYTHON,
                scriptPath,
                MEAN_DATA_AFTER_ON_TIME_M017_CSV,
                orientation1,
                orientation2,
                MEAN_AMPLITUDE + "_" + orientation1 + "_" + orientation2, // title
                FIRE_RATE_DATA_AFTER_ON_TIME_M017_CSV,
                orientation1,
                orientation2,
                FIRE_RATE + "_" + orientation1 + "_" + orientation2, //title
                RESULTS_COMPARED_EXPORt_FOLDER,
                String.format("%s_%s_%s_%s_%s", MEAN_AMPLITUDE, FIRE_RATE, AFTER_ON, orientation1, orientation2)
        );
    }

    private void MAvsFR_Between() {

        for(int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {

                int orientation1 = i * 45;
                int orientation2 = j * 45;

                String plotCommand = MAvsFR_BteweenCommand(orientation1, orientation2);
                runPlottingScript(plotCommand);
            }
        }
    }

    private void MAvsFR_Before() {

        for(int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {

                int orientation1 = i * 45;
                int orientation2 = j * 45;

                String plotCommand = MAvsFR_BeforeCommand(orientation1, orientation2);
                runPlottingScript(plotCommand);
            }
        }
    }

    private void MAvsFR_After() {

        for(int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {

                int orientation1 = i * 45;
                int orientation2 = j * 45;

                String plotCommand = MAvsFR_AfterCommand(orientation1, orientation2);
                runPlottingScript(plotCommand);
            }
        }

    }

    private void MAvsFR_AfterStimOffInt() {

        for(int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {

                int orientation1 = i * 45;
                int orientation2 = j * 45;

                String plotCommand = MAvsFR_AfterStimOffIntervalCommand(orientation1, orientation2);
                runPlottingScript(plotCommand);
            }
        }

    }

    private void MAvsFR_AfterStimOnInt() {

        for(int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {

                int orientation1 = i * 45;
                int orientation2 = j * 45;

                String plotCommand = MAvsFR_AfterStimOnIntervalCommand(orientation1, orientation2);
                runPlottingScript(plotCommand);
            }
        }

    }

    public void makeComparablePlots() {
        //MAvsFR_Between();
        //MAvsFR_Before();
        //MAvsFR_After();
        //MAvsFR_AfterStimOffInt();
        MAvsFR_AfterStimOnInt();
    }
}


public class BuildPlots {

    public static void main(String[] args) {
        new PlottingService().makeComparablePlots();
    }

}
