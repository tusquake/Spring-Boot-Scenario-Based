package com.springai.learning.dto;

/**
 * Data Transfer Object for Movie Information.
 * Used to demonstrate structured output parsing in Spring AI.
 */
public record MovieInfo(
    String title,
    int year,
    String genre,
    String summary
) {}
