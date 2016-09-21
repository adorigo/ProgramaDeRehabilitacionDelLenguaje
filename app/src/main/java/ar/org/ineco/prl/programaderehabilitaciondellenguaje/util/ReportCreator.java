package ar.org.ineco.prl.programaderehabilitaciondellenguaje.util;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ReportLine;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.database.DatabaseLoader;

public class ReportCreator {

    public static Uri generateCsvReport () {

        DatabaseLoader loader = DatabaseLoader.getInstance();
        List<ReportLine> reportData = loader.getReportData();

        String fileContent = "Categoria,Correctos,Incorrectos,Totales,% Correctos,% Incorrectos\n";

        for (ReportLine line : reportData) {
            fileContent += line.category.getName()+","+line.correctAns+","+line.wrongAns+
                    ","+line.totalTries+","+line.correctPerAns+","+line.wrongPerAns+"\n";
        }

        Date dateVal = new Date();
        String filename = dateVal.toString();
        File file = null;

        File root = Environment.getExternalStorageDirectory();
        if (root.canWrite()) {

            File dir = new File(root.getAbsolutePath() + "/Report");
            dir.mkdirs();

            file = new File(dir, filename+".csv");

            FileOutputStream out = null;

            try {
                out = new FileOutputStream(file);
                out.write(fileContent.getBytes());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Uri.fromFile(file);
    }
}
