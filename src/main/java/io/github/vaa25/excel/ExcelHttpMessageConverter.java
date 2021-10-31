package io.github.vaa25.excel;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import static java.util.Collections.singletonList;

public class ExcelHttpMessageConverter implements HttpMessageConverter<ExcelHttpFile> {

    private final ExcelFormat excelFormat;
    private final ExcelOptionsBeans optionsBeans;

    ExcelHttpMessageConverter(
        final ExcelFormat excelFormat, final ExcelOptionsBeans optionsBeans
    ) {
        this.excelFormat = excelFormat;
        this.optionsBeans = optionsBeans;
    }

    @Override
    public final boolean canRead(final Class clazz, final MediaType mediaType) {
        return false;
    }

    @Override
    public final ExcelHttpFile read(final Class clazz, final HttpInputMessage inputMessage)
        throws HttpMessageNotReadableException {
        return null;
    }

    @Override
    public boolean canWrite(final Class clazz, final MediaType mediaType) {
        return ExcelHttpFile.class.isAssignableFrom(clazz) && excelFormat.getMediaType().equalsTypeAndSubtype(mediaType);
    }

    @Override
    public void write(
        final ExcelHttpFile excelHttpFile, final MediaType contentType, final HttpOutputMessage outputMessage
    ) throws IOException, HttpMessageNotWritableException {
        fillHeaders(outputMessage, excelHttpFile.getFileName());
        excelHttpFile
            .setAsSourceIn(Poiji.toExcel())
            .withOptions(getOptions(excelHttpFile))
            .withDestination(outputMessage.getBody(), excelFormat.getExcelType())
            .save();
    }

    private PoijiOptions getOptions(final ExcelHttpFile excelHttpFile) {
        if (excelHttpFile.getOptions() != null){
            return excelHttpFile.getOptions();
        } else {
            return optionsBeans.getByBeanName(excelHttpFile.getOptionsBean());
        }
    }

    @Override
    public final List<MediaType> getSupportedMediaTypes() {
        return singletonList(excelFormat.getMediaType());
    }

    private void fillHeaders(final HttpOutputMessage outputMessage, final String fileName) {
        final HttpHeaders headers = outputMessage.getHeaders();
        final String filename = fileName + "." + excelFormat.getFileExtension();
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentType(excelFormat.getMediaType());
    }

}
