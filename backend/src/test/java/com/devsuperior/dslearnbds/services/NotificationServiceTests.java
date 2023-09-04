package com.devsuperior.dslearnbds.services;

import com.devsuperior.dslearnbds.dto.NotificationDTO;
import com.devsuperior.dslearnbds.entities.Notification;
import com.devsuperior.dslearnbds.entities.User;
import com.devsuperior.dslearnbds.repositories.NotificationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class NotificationServiceTests {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private AuthService authService;

    @Mock
    private NotificationRepository notificationRepository;

    private User user;
    private Notification notification;
    private Pageable pageable;
    private PageImpl<Notification> page;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User();
        user.setId(1L);
        user.setName("João Vitor");
        user.setEmail("jvolima2004@gmail.com");
        user.setPassword("123456");
        pageable = PageRequest.of(1, 10);
        notification = new Notification();
        notification.setId(1L);
        notification.setText("Atividade 1 corrigida");
        notification.setMoment(Instant.now());
        notification.setRead(false);
        notification.setUser(user);
        notification.setRoute("/offers/1/resource/1/sections/1");
        page = new PageImpl<>(List.of(notification));

        Mockito.when(notificationRepository.findByUser(user, pageable)).thenReturn(page);

        Mockito.when(authService.authenticated()).thenReturn(user);
    }

    @Test
    public void notificationsForCurrentUserShouldReturnNotificationsDtoPage() {
        Page<NotificationDTO> pageDto = notificationService.notificationsForCurrentUser(pageable);

        Assertions.assertEquals(page.getSize(), pageDto.getSize());
        Assertions.assertEquals(notification.getText(), pageDto.getContent().get(0).getText());
        Mockito.verify(notificationRepository).findByUser(user, pageable);
    }
}
