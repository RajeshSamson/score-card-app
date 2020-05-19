package com.pipa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPositionResponse {

    private Long userId;
    private Long score;
    private Long position;

}
