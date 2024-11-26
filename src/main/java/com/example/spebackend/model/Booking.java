package com.example.spebackend.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "rid")
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "cid")
    private Customer customer;
    @NonNull
    private LocalDate date;
    @NonNull
    private LocalTime time;
    private int tabletype;
}
