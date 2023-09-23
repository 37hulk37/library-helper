package com.hulk.library.dto;

import com.hulk.library.entity.Reader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReaderInfo {
    private Long id;
    private String name;

    public ReaderInfo(Reader reader) {
        this.id = reader.getId();
        this.name = reader.getName();
    }
}
