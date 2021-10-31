package io.github.vaa25.excel;

import com.poiji.bind.FromExcel;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ServerWebInputException;

public class ExcelArgumentResolver implements HandlerMethodArgumentResolver {

    private final ExcelDestination excelDestination;
    private final ExcelOptionsBeans optionsBeans;

    ExcelArgumentResolver(
        final ExcelDestination excelDestination, final ExcelOptionsBeans optionsBeans
    ) {
        this.excelDestination = excelDestination;
        this.optionsBeans = optionsBeans;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestExcelFile.class) &&
            excelDestination.getType().isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        final RequestExcelFile annotation = parameter.getParameterAnnotation(RequestExcelFile.class);
        final Optional content = Optional
            .ofNullable(webRequest.getNativeRequest(MultipartHttpServletRequest.class))
            .map(request -> request.getFile(annotation.name()))
            .map(multipartFile -> getContent(parameter, multipartFile));
        if (annotation.required() && !content.isPresent()) {
            throw new ServerWebInputException("Can't upload excel file", parameter);
        } else {
            return content.orElse(null);
        }
    }

    private Object getContent(final MethodParameter parameter, final MultipartFile file) {
        final Type actualTypeArgument = ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
        final Class classType = (Class) actualTypeArgument;
        try {
            final PoijiOptions options = getOptions(parameter);
            final InputStream inputStream = file.getInputStream();
            final PoijiExcelType excelType = PoijiExcelType.fromFileName(file.getOriginalFilename());
            final FromExcel<Object> fromExcel =
                Poiji.fromExcel().withJavaType(classType).withSource(inputStream, excelType).withOptions(options);
            return excelDestination.get(fromExcel);
        } catch (Exception e) {
            throw new ServerWebInputException("Can't upload excel file: " + e.getMessage(), parameter, e);
        }
    }

    private PoijiOptions getOptions(final MethodParameter parameter) {
        final String optionsBean = parameter.getParameterAnnotation(RequestExcelFile.class).optionsBean();
        return optionsBeans.getByBeanName(optionsBean);
    }

}