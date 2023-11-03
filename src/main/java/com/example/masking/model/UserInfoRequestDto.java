package com.example.masking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto implements MaskingDto{
    private String id;
    private String disableMaskingYn;
}
