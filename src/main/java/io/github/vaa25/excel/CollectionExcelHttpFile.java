package io.github.vaa25.excel;

import com.poiji.bind.ToExcel;
import java.util.Collection;

class CollectionExcelHttpFile<T> extends ExcelHttpFile<T> {

    private final Collection<T> content;

    CollectionExcelHttpFile(final String fileName, final Collection<T> content, final Class<T> type) {
        super(fileName, type);
        this.content = content;
    }

    @Override
    protected ToExcel<T> setAsSourceIn(final ToExcel<T> toExcel) {
        return toExcel.withSource(content).withJavaType(getType());
    }
}
