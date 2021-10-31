package io.github.vaa25.excel;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static io.github.vaa25.excel.RequestExcelFile.DEFAULT_EXCEL_OPTIONS_BEAN_NAME;

@ConditionalOnProperty(value = "spring-boot-starter-web-excel.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(Poiji.class)
@Configuration
public class ExcelWebAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private ExcelOptionsBeans excelOptionsBeans;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ExcelArgumentResolver(ExcelDestination.LIST, excelOptionsBeans));
        resolvers.add(new ExcelArgumentResolver(ExcelDestination.STREAM, excelOptionsBeans));
    }

    @Override
    public void extendMessageConverters(
        final List<HttpMessageConverter<?>> converters
    ) {
        converters.add(new ExcelHttpMessageConverter(ExcelFormat.CSV, excelOptionsBeans));
        converters.add(new ExcelHttpMessageConverter(ExcelFormat.XLS, excelOptionsBeans));
        converters.add(new ExcelHttpMessageConverter(ExcelFormat.XLSX, excelOptionsBeans));
    }

    @Bean
    public ExcelOptionsBeans excelOptionsBeans(final Map<String, PoijiOptions> poijiOptions){
        return new ExcelOptionsBeans(poijiOptions);
    }

    @Bean(DEFAULT_EXCEL_OPTIONS_BEAN_NAME)
    @ConditionalOnMissingBean
    public PoijiOptions defaultExcelOptions(){
        return PoijiOptions.PoijiOptionsBuilder.settings().build();
    }
}