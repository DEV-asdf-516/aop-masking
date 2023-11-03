package com.example.masking.masking;

import com.example.masking.model.MaskingDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@EnableAspectJAutoProxy
@Component
public class MaskingAspect {
    @Around("@annotation(applyMasking)")
    public Object applyMaskingAspect(ProceedingJoinPoint joinPoint, ApplyMasking applyMasking) throws Throwable {
       Object []args = joinPoint.getArgs();
       Object response = joinPoint.proceed();
       MaskingDto maskingOn = (MaskingDto) args[0];

       if(maskingOn.getDisableMaskingYn() == null && response != null) {
            // 필드 누락 시
           return applyMaskingUtil(applyMasking.typeValue(), applyMasking.genericTypeValue(),response);
       }
       if(maskingOn.getDisableMaskingYn().equals("Y")){
           // 원본 데이터
           return response;
       }
       else{
          // 마스킹 적용
           if(response!=null){
             return applyMaskingUtil(applyMasking.typeValue(), applyMasking.genericTypeValue(),response);
           }
       }
       return response;
    }

    private static <T> T applyMaskingUtil(Class<?> clazz, Class<?> klass, Object response)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(response instanceof List){
            return applyMaskingUtilForList(klass,response); // 마스킹 적용할 데이터가 List<?> 형태인 경우
        }
        else {
            return applyMaskingUtilForDto(clazz,response);
        }
    }

    private static <T> T applyMaskingUtilForDto(Class<?> clazz, Object response)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Object responseDto = clazz.getDeclaredConstructor().newInstance();
        Arrays.stream(fields).forEach(
                field -> {
                    field.setAccessible(true);
                    try{
                        Object fieldValue = field.get(response);
                        if(fieldValue instanceof String && field.isAnnotationPresent(Mask.class)){
                            Mask mask = field.getAnnotation(Mask.class); // Mask 어노테이션을 가져옴
                            MaskingType maskingType  = mask.type(); // 해당 어노테이션이 보유한 Enum 타입을 가져옴
                            String maskedValue = MaskingUtil.MaskingOf(maskingType, (String) fieldValue); // 마스킹 적용
                            field.set(responseDto,maskedValue);
                        }
                        else {
                            field.set(responseDto,fieldValue);
                        }
                    }catch (Exception e){}
                }
        );
        return (T) responseDto;
    }

    private static <T> T applyMaskingUtilForList(Class<?> klass, Object response)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> responseDtoList = new ArrayList<>();
        List<?> responseList = (List<?>) response;
        for(Object responseDto  : responseList){
            if(responseDto != null && responseDto.getClass().equals(klass)){
                Object maskedResponseDto = applyMaskingUtilForDto(klass,responseDto);
                responseDtoList.add(maskedResponseDto);
            }
            else {
                responseDtoList.add(responseDto);
            }
        }
        return (T) responseDtoList;
    }
}
