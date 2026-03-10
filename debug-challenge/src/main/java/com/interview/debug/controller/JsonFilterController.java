package com.interview.debug.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.interview.debug.model.FilteredUserDto;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/filter")
public class JsonFilterController {

    @GetMapping("/standard")
    public MappingJacksonValue getStandardUser() {
        FilteredUserDto user = new FilteredUserDto(1L, "johndoe", "john@example.com", "secret123", "USER");

        // 1. Define WHICH fields to include (ID, username, email)
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "username", "email");

        // 2. Wrap it in a FilterProvider using the name we defined in @JsonFilter
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        // 3. Wrap the DTO in MappingJacksonValue and set the filters
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/admin")
    public MappingJacksonValue getAdminUser() {
        FilteredUserDto user = new FilteredUserDto(1L, "johndoe", "john@example.com", "secret123", "USER");

        // 1. For Admin, we want everything EXCEPT password
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("password");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }
}
