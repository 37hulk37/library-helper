package com.hulk.library.service;

import com.hulk.library.dto.ReaderInfo;
import com.hulk.library.entity.Reader;
import com.hulk.library.request.NewReaderRequest;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.Date;

public interface ReaderService {
    ReaderInfo createReader(NewReaderRequest request);

    ReaderInfo updateReader(Reader request);

    ReaderInfo getBestReader(Date from, Date until);

    ReaderInfo getReader(Long id);

    void deleteReader(Long id);
}
