package org.kdb.inside.brains.view.console.export;

import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.ui.table.JBTable;
import icons.KdbIcons;
import kx.KxConnection;
import org.apache.poi.ss.usermodel.*;
import org.kdb.inside.brains.view.console.TableResultView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

public class ExcelExportAction extends AnExportAction {
    private final boolean saveOnDisk;

    public ExcelExportAction(String text, ExportingType type, TableResultView resultView, String description, boolean saveOnDisk) {
        this(text, type, resultView, description, saveOnDisk, KdbIcons.Console.ExportExcel);
    }

    public ExcelExportAction(String text, ExportingType type, TableResultView resultView, String description, boolean saveOnDisk, Icon icon) {
        super(text, type, resultView, description, icon);
        this.saveOnDisk = saveOnDisk;
    }

    @Override
    protected void performAction(Project project, TableResultView view, ExportingType type) throws Exception {
        if (saveOnDisk) {
            final FileSaverDescriptor fileSaverDescriptor = new FileSaverDescriptor("Export to Excel", "Exporting data into Excel file format", "xls");
            final FileSaverDialog saveFileDialog = FileChooserFactory.getInstance().createSaveFileDialog(fileSaverDescriptor, project);

            final VirtualFileWrapper file = saveFileDialog.save((VirtualFile) null, "Table Result");
            if (file != null) {
                exportData(file.getFile(), view, type);
            }
        } else {
            final File f = File.createTempFile("kdbinsidebrains_exel_export_", ".xls");
            exportData(f, view, type);
            Desktop.getDesktop().open(f);
        }
    }

    private void exportData(File file, TableResultView view, ExportingType type) throws Exception {
        final Workbook wb = WorkbookFactory.create(false);
        final Sheet sheet = wb.createSheet("KDB Exported Data");

        final JBTable table = view.getTable();
        final ExportingType.IndexIterator ri = type.rowsIterator(table);
        final ExportingType.IndexIterator ci = type.columnsIterator(table);

        int index = 0;
        if (type.withHeader()) {
            Row row = sheet.createRow(index++);

            int i = 0;
            for (int c = ci.reset(); c != -1; c = ci.next()) {
                final String columnName = table.getColumnName(c);
                row.createCell(i++, CellType.STRING).setCellValue(columnName);
            }
        }

        for (int r = ri.reset(); r != -1; r = ri.next()) {
            final Row row = sheet.createRow(index++);
            int i = 0;
            for (int c = ci.reset(); c != -1; c = ci.next()) {
                final Object value = table.getValueAt(r, c);
                if (value instanceof Boolean) {
                    row.createCell(i++, CellType.BOOLEAN).setCellValue((Boolean) value);
                } else if (value instanceof Number) {
                    if (KxConnection.isNull(value)) {
                        row.createCell(i++, CellType.NUMERIC).setCellValue(view.convertValue(value));
                    } else {
                        row.createCell(i++, CellType.NUMERIC).setCellValue(((Number) value).doubleValue());
                    }
                } else {
                    row.createCell(i++).setCellValue(view.convertValue(value));
                }
            }
        }

        try (final FileOutputStream stream = new FileOutputStream(file)) {
            wb.write(stream);
        }
        wb.close();
    }
}