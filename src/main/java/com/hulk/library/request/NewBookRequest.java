package com.hulk.library.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBookRequest {
    private String name;
    private String author;
    private String topic;
    private String description;
}
