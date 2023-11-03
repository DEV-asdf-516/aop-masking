package com.example.masking.masking;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mask {
    MaskingType type(); // 클래스 멤버변수 필드에 사용할 마스킹 어노테이션
}
