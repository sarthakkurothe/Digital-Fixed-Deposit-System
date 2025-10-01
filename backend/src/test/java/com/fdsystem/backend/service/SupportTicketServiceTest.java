package com.fdsystem.backend.service;

import com.fdsystem.backend.dto.AdminSupportTicketDto;
import com.fdsystem.backend.dto.TicketRequestDTO;
import com.fdsystem.backend.dto.TicketResponseDTO;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.SupportTicket;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.SupportTicketRepository;
import com.fdsystem.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SupportTicketService using Mockito + JUnit5
 */
@ExtendWith(MockitoExtension.class)
class SupportTicketServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SupportTicketServiceTest.class);

    @Mock
    private FixedDepositRepository fixedDepositRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @InjectMocks
    private SupportTicketService supportTicketService;

    @BeforeEach
    void setup() {

    }

    // Helper builders
    private User sampleUser(long id, String name, String email) {
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setEmail(email);
        return u;
    }

    private FixedDeposit sampleFd(long id, User user) {
        FixedDeposit fd = new FixedDeposit();
        fd.setId(id);
        fd.setUser(user);
        return fd;
    }

    private SupportTicket sampleTicket(long id, User user, FixedDeposit fd, String subject, String description, SupportTicketStatus status) {
        SupportTicket t = new SupportTicket();
        t.setId(id);
        t.setUser(user);
        t.setFixedDeposit(fd);
        t.setSubject(subject);
        t.setDescription(description);
        t.setStatus(status);
        t.setCreatedDate(Date.valueOf(LocalDate.of(2024, 1, 1))); // deterministic date for tests
        return t;
    }

    @Test
    void testCreateTicket_Success() {
        long fdId = 10L;
        User user = sampleUser(2L, "Sarthak", "sarthak@example.com");
        FixedDeposit fd = sampleFd(fdId, user);

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setFdId(fdId);
        dto.setSubject("Problem with FD");
        dto.setDescription("Please help me with my FD.");

        when(fixedDepositRepository.findById(fdId)).thenReturn(Optional.of(fd));
        // Save should return the passed entity (common mock pattern)
        when(supportTicketRepository.save(any(SupportTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        supportTicketService.createTicket(dto);

        ArgumentCaptor<SupportTicket> captor = ArgumentCaptor.forClass(SupportTicket.class);
        verify(supportTicketRepository, times(1)).save(captor.capture());
        SupportTicket saved = captor.getValue();

        assertNotNull(saved);
        assertEquals(dto.getSubject(), saved.getSubject());
        assertEquals(dto.getDescription(), saved.getDescription());
        assertEquals(user, saved.getUser());
        assertEquals(fd, saved.getFixedDeposit());
        assertEquals(SupportTicketStatus.OPEN, saved.getStatus());
        assertNotNull(saved.getCreatedDate());
        log.info("createTicket saved ticket id? (not assigned by mock): subject={}, status={}", saved.getSubject(), saved.getStatus());
    }

    @Test
    void testCreateTicket_FdNotFound_Throws() {
        long missingFd = 99L;
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setFdId(missingFd);
        dto.setSubject("X");
        dto.setDescription("Y");

        when(fixedDepositRepository.findById(missingFd)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> supportTicketService.createTicket(dto));
        verify(supportTicketRepository, never()).save(any());
    }

    @Test
    void testGetTicketsByUserId_ReturnsMappedResponses() {
        long userId = 5L;
        User user = sampleUser(userId, "Test User", "test@example.com");
        FixedDeposit fd = sampleFd(20L, user);

        SupportTicket t1 = sampleTicket(101L, user, fd, "Subject A", "Desc A", SupportTicketStatus.OPEN);
        t1.setResponse("No response yet");
        SupportTicket t2 = sampleTicket(102L, user, fd, "Subject B", "Desc B", SupportTicketStatus.CLOSED);
        t2.setResponse("Resolved");

        List<SupportTicket> storedTickets = new ArrayList<>();
        storedTickets.add(t1);
        storedTickets.add(t2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(supportTicketRepository.findAllByUser(user)).thenReturn(storedTickets);

        List<TicketResponseDTO> responses = supportTicketService.getTicketsByUserId(userId);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        TicketResponseDTO r1 = responses.get(0);
        assertEquals(t1.getStatus().toString(), r1.getStatus());
        assertEquals(t1.getResponse(), r1.getResponse());
        assertEquals(t1.getSubject(), r1.getSubject());
        assertEquals(t1.getDescription(), r1.getDescription());
        assertEquals(t1.getId(), r1.getId());
        assertEquals(t1.getFixedDeposit().getId(), r1.getFdId());
        assertEquals(t1.getCreatedDate(), r1.getCreatedDate());
    }

    @Test
    void testGetAll_ReturnsAdminDtos() {
        User user1 = sampleUser(11L, "A", "a@example.com");
        FixedDeposit fd1 = sampleFd(201L, user1);
        SupportTicket ticket1 = sampleTicket(301L, user1, fd1, "S1", "D1", SupportTicketStatus.OPEN);

        User user2 = sampleUser(12L, "B", "b@example.com");
        FixedDeposit fd2 = sampleFd(202L, user2);
        SupportTicket ticket2 = sampleTicket(302L, user2, fd2, "S2", "D2", SupportTicketStatus.CLOSED);

        List<SupportTicket> tickets = List.of(ticket1, ticket2);

        when(supportTicketRepository.findAll()).thenReturn(tickets);

        List<AdminSupportTicketDto> dtos = supportTicketService.getAll();

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        AdminSupportTicketDto dto1 = dtos.get(0);
        assertEquals(ticket1.getUser().getName(), dto1.getName());
        assertEquals(ticket1.getDescription(), dto1.getDescription());
        assertEquals(ticket1.getFixedDeposit(), dto1.getFd());
        assertEquals(ticket1.getSubject(), dto1.getSubject());
        assertEquals(ticket1.getUser().getEmail(), dto1.getEmail());
        assertEquals(ticket1.getCreatedDate(), dto1.getCreatedDate());
        assertEquals(ticket1.getId(), dto1.getId());
        assertEquals(ticket1.getStatus().toString(), dto1.getStatus());
    }

    @Test
    void testSetTicketStatusById_UpdatesAndSaves() {
        long ticketId = 44L;
        User user = sampleUser(33L, "U", "u@example.com");
        FixedDeposit fd = sampleFd(77L, user);
        SupportTicket existing = sampleTicket(ticketId, user, fd, "S", "D", SupportTicketStatus.OPEN);

        when(supportTicketRepository.findById(ticketId)).thenReturn(Optional.of(existing));
        when(supportTicketRepository.save(any(SupportTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String newResponse = "Resolved by admin";
        supportTicketService.setTicketStatusById(ticketId, newResponse, SupportTicketStatus.CLOSED);

        ArgumentCaptor<SupportTicket> captor = ArgumentCaptor.forClass(SupportTicket.class);
        verify(supportTicketRepository, times(1)).save(captor.capture());
        SupportTicket saved = captor.getValue();

        assertEquals(SupportTicketStatus.CLOSED, saved.getStatus());
        assertEquals(newResponse, saved.getResponse());
    }

    @Test
    void testGetAllStatusTicketsCount_ReturnsCount() {
        String status = "OPEN";
        when(supportTicketRepository.getAllStatusTicketsCount(status)).thenReturn(7L);

        Long count = supportTicketService.getAllStatusTicketsCount(status);

        assertNotNull(count);
        assertEquals(7L, count.longValue());
        verify(supportTicketRepository, times(1)).getAllStatusTicketsCount(status);
    }
}
