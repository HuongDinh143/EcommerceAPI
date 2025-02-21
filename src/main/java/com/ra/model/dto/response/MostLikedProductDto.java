package com.ra.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MostLikedProductDto {
    private Long productId;
    private String productName;
    private long totalLikes;
}
