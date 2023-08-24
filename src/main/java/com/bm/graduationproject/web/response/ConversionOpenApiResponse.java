package com.bm.graduationproject.web.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionOpenApiResponse {
    String result;
    String documentation;
    String terms_of_use;
    int time_last_update_unix;
    String time_last_update_utc;
    int time_next_update_unix;
    String time_next_update_utc;
    String base_code;
    String target_code;
    Double conversion_rate;
    Double conversion_result;
}
