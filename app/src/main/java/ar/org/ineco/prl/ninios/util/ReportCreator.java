package ar.org.ineco.prl.ninios.util;

import android.net.Uri;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import ar.org.ineco.prl.ninios.R;
import ar.org.ineco.prl.ninios.classes.ApplicationContext;
import ar.org.ineco.prl.ninios.classes.ReportLine;
import ar.org.ineco.prl.ninios.database.DatabaseLoader;

public class ReportCreator {

    public static Uri generateCsvReport () {

        DatabaseLoader loader = DatabaseLoader.getInstance();
        List<ReportLine> reportData = loader.getReportData();

        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Reporte");

        try {

            // Get the logo.
            Uri uri = Utils.getUriToResource(ApplicationContext.get(), R.mipmap.ic_launcher);
            InputStream stream = ApplicationContext.get().getContentResolver().openInputStream(uri);

            // parse it..
            byte[] bytes = IOUtils.toByteArray(stream);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            stream.close();

            // position it..
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = createHelper.createClientAnchor();
            anchor.setRow1(1);
            anchor.setRow2(3);
            anchor.setCol1(1);
            anchor.setCol2(4);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            sheet.addMergedRegion(new CellRangeAddress(
                    1, //first row
                    2, //last row
                    1, //first column
                    3  //last column
            ));

            // adjust it..
            //pict.resize();

            Row rowNames = sheet.createRow((short) 3);
            rowNames.createCell(3).setCellValue("Nombre");
            rowNames.createCell(4).setCellValue(Utils.getUserName(ApplicationContext.get()));
            rowNames.createCell(5).setCellValue("Apellido");
            rowNames.createCell(6).setCellValue(Utils.getUserLastName(ApplicationContext.get()));

            // then add first row
            Row rowheader = sheet.createRow((short) 5);

            // with headers
            rowheader.createCell(1).setCellValue("Categoría");
            rowheader.createCell(2).setCellValue("Correctos");
            rowheader.createCell(3).setCellValue("Incorrectos");
            rowheader.createCell(4).setCellValue("Totales");
            rowheader.createCell(5).setCellValue("% Correctos ");
            rowheader.createCell(6).setCellValue("% Incorrectos");

            short rowCount = 5;
            for (ReportLine line : reportData) {

                rowCount++;

                // a row
                Row row = sheet.createRow(rowCount);

                // with columns
                row.createCell(1).setCellValue(line.category.getName());
                row.createCell(2).setCellValue(line.correctAns);
                row.createCell(3).setCellValue(line.wrongAns);
                row.createCell(4).setCellValue(line.totalTries);
                row.createCell(5).setCellValue(line.correctPerAns);
                row.createCell(6).setCellValue(line.wrongPerAns);
            }

            CellRangeAddress data = new CellRangeAddress(
                    5, //first row
                    rowCount, //last row
                    1, //first column
                    6  //last column
            );

            // border style
            RegionUtil.setBorderBottom( CellStyle.BORDER_THIN,
                    data, sheet, wb );
            RegionUtil.setBorderTop( CellStyle.BORDER_THIN,
                    data, sheet, wb );
            RegionUtil.setBorderLeft( CellStyle.BORDER_THIN,
                    data, sheet, wb );
            RegionUtil.setBorderRight( CellStyle.BORDER_THIN,
                    data, sheet, wb );
            RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), data, sheet, wb);
            RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), data, sheet, wb);
            RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), data, sheet, wb);
            RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), data, sheet, wb);

            Date dateVal = new Date();
            String filename = dateVal.toString();
            File file = null;

            File root = Environment.getExternalStorageDirectory();

            if (root.canWrite()) {

                File dir = new File(root.getAbsolutePath() + "/Report");
                dir.mkdirs();

                file = new File(dir, filename+".xls");

                FileOutputStream out = null;

                out = new FileOutputStream(file);
                wb.write(out);
                //out.write(fileContent.getBytes());
                out.close();

                return Uri.fromFile(file);

            } else {

                return null;
            }

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }
}
