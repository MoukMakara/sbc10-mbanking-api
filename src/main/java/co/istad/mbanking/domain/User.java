package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(length = 10, nullable = false, unique = true)
    private String phoneNumber;

    @Column(length = 40, nullable = false, unique = true)
    private String email;

    @Column(length = 4, nullable = false)
    private String pin;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false, unique = true)
    private String nationalCardId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String gender;

    private String profileImage;

    @Column(length = 20, unique = true)
    private String studentCardId;

    private String street;
    private String village;
    private String sangkatOrCommune;
    private String khanOrDistrict;
    private String cityOrProvince;

    private String position;
    private BigDecimal monthlyIncomeRange;
    private String employeeType;
    private String companyName;
    private String mainSourceOfIncome;

    private Boolean isDeleted;
    private Boolean isBlocked;
    private Boolean isVerified;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

}