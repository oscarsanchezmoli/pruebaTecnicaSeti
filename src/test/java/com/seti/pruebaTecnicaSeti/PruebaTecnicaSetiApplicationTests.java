package com.seti.pruebaTecnicaSeti;

import com.seti.pruebaTecnicaSeti.service.NotificationService;
import com.seti.pruebaTecnicaSeti.utils.SmsInfobip;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class PruebaTecnicaSetiApplicationTests {

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private SmsInfobip smsInfobip;

	@Test
	void contextLoads() {
	}

}
