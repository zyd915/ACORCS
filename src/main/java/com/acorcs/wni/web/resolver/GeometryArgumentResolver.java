package com.acorcs.wni.web.resolver;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by dengc on 2017/1/2.
 */
public class GeometryArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.getParameterType().equals(Geometry.class)
                || methodParameter.getParameterType().isAssignableFrom(Geometry.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String parameterName = methodParameter.getParameterName();
        String parameter = nativeWebRequest.getParameter(parameterName);
        if(parameter == null){
            return null;
        }
        GeometryJSON geoJson = new GeometryJSON();
        Geometry geometry = null;
        try{
            Reader reader = new StringReader(parameter);
            geometry = geoJson.read(reader);
        }catch (IOException | NullPointerException e){
            throw new MethodArgumentTypeMismatchException(parameter,methodParameter.getParameterType(),methodParameter.getParameterName(),methodParameter,e);
        }

        geometry.setSRID(4326);
        return geometry;
    }
}
