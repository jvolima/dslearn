package com.devsuperior.dslearnbds.services;

import com.devsuperior.dslearnbds.dto.DeliverRevisionDTO;
import com.devsuperior.dslearnbds.entities.Deliver;
import com.devsuperior.dslearnbds.entities.Enrollment;
import com.devsuperior.dslearnbds.entities.Task;
import com.devsuperior.dslearnbds.entities.enums.DeliverStatus;
import com.devsuperior.dslearnbds.repositories.DeliverRepository;
import com.devsuperior.dslearnbds.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;

@ExtendWith(SpringExtension.class)
public class DeliverServiceTests {

    @InjectMocks
    private DeliverService deliverService;

    @Mock
    private DeliverRepository deliverRepository;

    private Long existingId;
    private Long nonExistingId;
    private Deliver deliver;
    private DeliverRevisionDTO deliverRevisionDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        deliver = new Deliver();
        deliver.setId(1L);
        deliver.setMoment(Instant.now());
        deliver.setUri("/test/1");
        deliver.setEnrollment(new Enrollment());
        deliver.setLesson(new Task());
        deliver.setStatus(DeliverStatus.PENDING);
        deliverRevisionDTO = new DeliverRevisionDTO();
        deliverRevisionDTO.setFeedback("Parabéns pelo ótimo trabalho!");
        deliverRevisionDTO.setStatus(DeliverStatus.ACCEPTED);
        deliverRevisionDTO.setCorrectCount(5);

        Mockito.when(deliverRepository.getOne(existingId)).thenReturn(deliver);
        Mockito.when(deliverRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(deliverRepository.save(Mockito.any(Deliver.class))).thenReturn(deliver);
    }

    @Test
    public void saveRevisionShouldDoNothingWhenIdExist() {
        Assertions.assertDoesNotThrow(() -> {
            deliverService.saveRevision(existingId, deliverRevisionDTO);
        });

        Mockito.verify(deliverRepository).getOne(existingId);
        Mockito.verify(deliverRepository).save(deliver);
    }

    @Test
    public void saveRevisionShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            deliverService.saveRevision(nonExistingId, deliverRevisionDTO);
        });

        Mockito.verify(deliverRepository).getOne(nonExistingId);
    }
}
