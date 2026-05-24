package com.aditya.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // e.g., WALLET_FREEZE, WALLET_UNFREEZE, HIGH_VALUE_TRANSFER, FREQUENT_TRANSFER, FAILED_TRANSFER

    private String performedBy; // email or 'SYSTEM'

    private String details; // additional info

    private LocalDateTime timestamp;
}
