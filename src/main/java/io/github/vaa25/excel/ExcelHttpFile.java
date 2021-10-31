package io.github.vaa25.excel;

import com.poiji.bind.ToExcel;
import com.poiji.option.PoijiOptions;
import java.util.Collection;
import java.util.stream.Stream;

import static io.github.vaa25.excel.RequestExcelFile.DEFAULT_EXCEL_OPTIONS_BEAN_NAME;

public abstract class ExcelHttpFile<T>{

    private final String fileName;
    private final Class<T> type;
    private PoijiOptions options;
    private String optionsBean = DEFAULT_EXCEL_OPTIONS_BEAN_NAME;

    protected ExcelHttpFile(final String fileName, final Class<T> type) {
        this.fileName = fileName;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public Class<T> getType() {
        return type;
    }

    public String getOptionsBean() {
        return optionsBean;
    }

    public ExcelHttpFile<T> setOptionsBean(final String optionsBean) {
        this.optionsBean = optionsBean;
        return this;
    }

    public PoijiOptions getOptions() {
        return this.options;
    }

    public ExcelHttpFile<T> setOptions(final PoijiOptions options) {
        this.options = options;
        return this;
    }

    protected abstract ToExcel<T> setAsSourceIn(ToExcel<T> toExcel);

    public static <T> CollectionExcelHttpFile<T> of(final String fileName, final Collection<T> content, final Class<T> type){
        return new CollectionExcelHttpFile<>(fileName, content, type);
    }

    public static <T> StreamExcelHttpFile<T> of(final String fileName, final Stream<T> content, final Class<T> type){
        return new StreamExcelHttpFile<>(fileName, content, type);
    }

}
