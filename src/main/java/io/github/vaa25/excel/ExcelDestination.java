package io.github.vaa25.excel;

import com.poiji.bind.FromExcel;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

enum ExcelDestination {

    STREAM(Stream.class, FromExcel::toStream), LIST(List.class, FromExcel::toList);

    private final Class type;
    private final Function<FromExcel, ?> save;

    ExcelDestination(final Class type, final Function<FromExcel, ?> save) {
        this.type = type;
        this.save = save;
    }

    public Class getType() {
        return type;
    }

    public Object get(final FromExcel fromExcel) {
        return save.apply(fromExcel);
    }
}
