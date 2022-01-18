package inu.market.category.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private String iconUrl;

    public static Category createCategory(String name, String iconUrl) {
        Category category = new Category();
        category.name = name;
        category.iconUrl = iconUrl;
        return category;
    }

    public void changeNameAndIconUrl(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }
}
