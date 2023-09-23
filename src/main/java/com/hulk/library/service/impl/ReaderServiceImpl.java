package com.hulk.library.service.impl;

import com.hulk.library.dao.ReaderRepository;
import com.hulk.library.dto.ReaderInfo;
import com.hulk.library.entity.Reader;
import com.hulk.library.exception.BadRequestException;
import com.hulk.library.exception.NotFoundException;
import com.hulk.library.request.NewReaderRequest;
import com.hulk.library.service.ReaderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository repository;

    @Override
    @Transactional
    public ReaderInfo createReader(NewReaderRequest request) {
        if (repository.existsReaderByName(request.getName())) {
            throw new BadRequestException("Reader with name " + request.getName() + " already exists");
        }

        var reader = repository.save(new Reader(request.getName()));

        return new ReaderInfo(reader);
    }

    @Override
    @Transactional
    public ReaderInfo updateReader(Reader request) {
        if (repository.existsReaderByName(request.getName())) {
            throw new BadRequestException("Reader with name " + request.getName() + " already exists");
        }

        if (repository.existsById(request.getId())) {
            throw new NotFoundException("Reader with id " + request.getId() + " not fount");
        }

        var reader = repository.save(new Reader(request.getName()));

        return new ReaderInfo(reader);
    }

    @Override
    public ReaderInfo getBestReader(Date from, Date until) {
        var reader = repository.findBestReader(from, until)
                .orElseThrow(() -> new NotFoundException("No one read books from " + from + " until " + until));

        return new ReaderInfo(reader);
    }

    @Override
    public ReaderInfo getReader(Long id) {
        var reader = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reader with id " + id + "not found"));

        return new ReaderInfo(reader);
    }

    @Override
    @Transactional
    public void deleteReader(Long id) {
        if ( !repository.existsById(id)) {
            throw new NotFoundException("Reader with id " + id + "not exists");
        }

        repository.deleteById(id);
    }
}
