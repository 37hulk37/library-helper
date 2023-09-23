package com.hulk.library.utils.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowBookRequest {
    private Long readerId;
    private Long bookId;
    private Integer days;
}
