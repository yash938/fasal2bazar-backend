package com.agriRoot.Utils;

import com.agriRoot.domain.PaegableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    private static final ModelMapper modelMapper = new ModelMapper(); // Reuse the instance

    public static <U, V> PaegableResponse<V> getPaegable(Page<U> page, Class<V> type) {

        // Convert entities to DTOs using ModelMapper
        List<V> collect = page.getContent()
                .stream()
                .map(object -> modelMapper.map(object, type))
                .collect(Collectors.toList());

        // Create a PaegableResponse object
        PaegableResponse<V> paegableResponse = new PaegableResponse<>();
        paegableResponse.setContent(collect);
        paegableResponse.setPageNumber(page.getNumber());
        paegableResponse.setPageSize(page.getSize());
        paegableResponse.setTotalElements(page.getTotalElements());
        paegableResponse.setLastPage(page.isLast());
        paegableResponse.setTotalPages(page.getTotalPages());

        return paegableResponse;
    }
}
