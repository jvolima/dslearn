package com.devsuperior.dslearnbds.services;

import com.devsuperior.dslearnbds.dto.DeliverRevisionDTO;
import com.devsuperior.dslearnbds.entities.Deliver;
import com.devsuperior.dslearnbds.repositories.DeliverRepository;
import com.devsuperior.dslearnbds.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class DeliverService {

    @Autowired
    private DeliverRepository repository;

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Transactional
    public void saveRevision(Long id, DeliverRevisionDTO dto) {
        try {
            Deliver deliver = repository.getOne(id);
            deliver.setStatus(dto.getStatus());
            deliver.setFeedback(dto.getFeedback());
            deliver.setCorrectCount(dto.getCorrectCount());
            repository.save(deliver);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }
}
