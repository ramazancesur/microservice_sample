package com.banking.ledger.application.usecase;

import com.banking.ledger.application.dto.JournalEntryResponse;
import com.banking.ledger.application.dto.PostJournalRequest;

public interface PostJournalUseCase {
    JournalEntryResponse execute(PostJournalRequest request);
}
