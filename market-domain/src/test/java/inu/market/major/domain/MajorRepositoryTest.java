package inu.market.major.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MajorRepositoryTest {

    @Autowired
    private MajorRepository majorRepository;

    @BeforeEach
    void setUp() {
        majorRepository.deleteAll();
    }

    @Test
    @DisplayName("이름으로 학과를 조회한다.")
    void findByName() {
        // given
        Major major = Major.createMajor("정보기술대학", null);
        majorRepository.save(major);

        // when
        Major result = majorRepository.findByName("정보기술대학").get();

        // then
        assertThat(result).isEqualTo(major);
    }

    @Test
    @DisplayName("부모가 없는 학과(단과대학)를 조회한다.")
    void findByParentIsNull() {
        // given
        Major major = Major.createMajor("정보기술대학", null);
        majorRepository.save(major);

        // when
        List<Major> result = majorRepository.findByParentIsNull();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("자식 학과를 조회한다.")
    void findByParentId() {
        // given
        Major parent = Major.createMajor("정보기술대학", null);
        Major children = Major.createMajor("정보통신공학과", parent);
        majorRepository.save(parent);
        majorRepository.save(children);

        // when
        List<Major> result = majorRepository.findByParentId(parent.getId());

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}