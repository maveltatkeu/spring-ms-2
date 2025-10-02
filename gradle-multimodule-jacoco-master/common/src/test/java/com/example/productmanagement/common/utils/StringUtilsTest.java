package com.example.productmanagement.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void truncate_WithNullInput_ShouldReturnNull() {
        // Act
        String result = StringUtils.truncate(null, 10);
        
        // Assert
        assertThat(result).isNull();
    }
    
    @Test
    void truncate_WithShortInput_ShouldReturnOriginalString() {
        // Arrange
        String input = "Short";
        int maxLength = 10;
        
        // Act
        String result = StringUtils.truncate(input, maxLength);
        
        // Assert
        assertThat(result).isEqualTo(input);
    }
    
    @Test
    void truncate_WithLongInput_ShouldTruncateAndAddEllipsis() {
        // Arrange
        String input = "This is a very long string that should be truncated";
        int maxLength = 20;
        
        // Act
        String result = StringUtils.truncate(input, maxLength);
        
        // Assert
        assertThat(result).isEqualTo("This is a very lo...");
        assertThat(result.length()).isEqualTo(maxLength);
    }
    
    @Test
    void randomString_WithPositiveLength_ShouldReturnStringWithCorrectLength() {
        // Arrange
        int length = 15;
        
        // Act
        String result = StringUtils.randomString(length);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.length()).isEqualTo(length);
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void isEmpty_WithNullOrEmptyOrBlankInput_ShouldReturnTrue(String input) {
        // Act
        boolean result = StringUtils.isEmpty(input);
        
        // Assert
        assertThat(result).isTrue();
    }
    
    @Test
    void isEmpty_WithNonEmptyInput_ShouldReturnFalse() {
        // Arrange
        String input = "Not empty";
        
        // Act
        boolean result = StringUtils.isEmpty(input);
        
        // Assert
        assertThat(result).isFalse();
    }
}
