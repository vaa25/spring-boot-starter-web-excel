package io.github.vaa25.excel;

import com.poiji.exception.PoijiExcelType;
import org.springframework.http.MediaType;

public enum ExcelFormat {

    XLSX(Constants.XLSX_MEDIA_TYPE_VALUE, PoijiExcelType.XLSX, "xlsx"),
    XLS(Constants.XLS_MEDIA_TYPE_VALUE, PoijiExcelType.XLS, "xls"),
    CSV(Constants.CSV_MEDIA_TYPE_VALUE, PoijiExcelType.CSV, "csv");

    private final String header;
    private final MediaType mediaType;
    private final PoijiExcelType excelType;
    private final String fileExtension;

    ExcelFormat(
        final String header,final PoijiExcelType excelType, final String fileExtension
    ) {
        this.header = header;
        this.mediaType = MediaType.valueOf(header);
        this.excelType = excelType;
        this.fileExtension = fileExtension;
    }

    public String getHeader() {
        return header;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public PoijiExcelType getExcelType() {
        return excelType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public static class Constants {
        public static final String CSV_MEDIA_TYPE_VALUE = "text/csv";
        public static final String XLS_MEDIA_TYPE_VALUE = "application/vnd.ms-excel";
        public static final String
            XLSX_MEDIA_TYPE_VALUE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }
}
