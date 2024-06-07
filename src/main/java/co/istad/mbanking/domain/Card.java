package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 12, nullable = false, unique = true)
    private String number;

    @Column(length = 100, nullable = false)
    private String holder;

    @Column(nullable = false)
    private Integer cvv;

    @Column(nullable = false)
    private LocalDate issuedAt;

    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    private CardType cardType;

    @OneToOne(mappedBy = "card")
    private Account account;

}
