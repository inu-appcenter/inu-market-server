package inu.market.major.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long id;

    private String name;

    public static Major createMajor(String name) {
        Major major = new Major();
        major.name = name;
        return major;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
