package com.banking.ledger.application.usecase;

import com.banking.ledger.application.dto.JournalEntryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetMovementsUseCase {
    List<JournalEntryResponse> execute(UUID accountId, LocalDateTime from, LocalDateTime to);
}
