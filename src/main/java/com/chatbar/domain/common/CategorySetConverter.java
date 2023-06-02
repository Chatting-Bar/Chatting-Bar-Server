package com.chatbar.domain.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Converter
public class CategorySetConverter implements AttributeConverter<EnumSet<Category>, String> {

    private static final String DELIMITER = ",";

    //EnumSet -> String , Database에 저장하기 위함
    @Override
    public String convertToDatabaseColumn(EnumSet<Category> categorySet) {
        if (categorySet == null || categorySet.isEmpty()) {
            return null;
        }

        return categorySet.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    //String -> EnumSet , Database에서 조회할 때
    @Override
    public EnumSet<Category> convertToEntityAttribute(String categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }

        String[] categoryArray = categories.split(DELIMITER);
        EnumSet<Category> categorySet = EnumSet.noneOf(Category.class);

        for (String category : categoryArray) {
            try {
                Category enumCategory = Category.valueOf(category);
                categorySet.add(enumCategory);
            } catch (IllegalArgumentException e) {
                // 유효하지 않은 category 값일 경우, 해당 항목을 무시하고 다음으로 넘어감
                // 필요에 따라 로깅 등의 추가 작업을 수행할 수 있음
                System.err.println("Invalid category value: " + category);
            }
        }

        return categorySet;
    }
}