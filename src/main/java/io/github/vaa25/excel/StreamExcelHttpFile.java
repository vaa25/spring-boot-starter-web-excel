package io.github.vaa25.excel;

import com.poiji.bind.ToExcel;
import java.util.stream.Stream;

class StreamExcelHttpFile<T> extends ExcelHttpFile<T> {

    private final Stream<T> content;

    StreamExcelHttpFile(final String fileName, final Stream<T> content, final Class<T> type) {
        super(fileName, type);
        this.content = content;
    }

    @Override
    protected ToExcel<T> setAsSourceIn(final ToExcel<T> toExcel) {
        return toExcel.withSource(content).withJavaType(getType());
    }
}
