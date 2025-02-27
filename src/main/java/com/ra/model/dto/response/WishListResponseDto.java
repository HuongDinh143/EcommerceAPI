package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishListResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long liked;
}
