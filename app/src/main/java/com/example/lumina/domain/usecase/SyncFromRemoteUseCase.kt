package com.example.lumina.domain.usecase

import com.example.lumina.domain.repository.JournalRepository
import javax.inject.Inject

class SyncFromRemoteUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.syncFromRemote()
}
