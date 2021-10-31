package io.github.vaa25.excel;

import com.poiji.option.PoijiOptions;
import java.util.Map;
import org.springframework.web.server.ResponseStatusException;

import static io.github.vaa25.excel.RequestExcelFile.DEFAULT_EXCEL_OPTIONS_BEAN_NAME;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.util.StringUtils.hasText;

public final class ExcelOptionsBeans {

    private final Map<String, PoijiOptions> optionsBeans;

    public ExcelOptionsBeans(final Map<String, PoijiOptions> optionsBeans) {
        this.optionsBeans = optionsBeans;
    }

    public PoijiOptions getByBeanName(final String beanName){
        if (hasText(beanName)){
            return getOptions(beanName);
        } else {
            return getOptions(DEFAULT_EXCEL_OPTIONS_BEAN_NAME);
        }
    }

    private PoijiOptions getOptions(final String optionsBean) {
        if (optionsBeans.containsKey(optionsBean)){
            return optionsBeans.get(optionsBean);
        } else {
            final String reason = String.format("Excel options bean '%s' not found", optionsBean);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, reason);
        }
    }
}
