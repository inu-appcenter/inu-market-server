package inu.market.major.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MajorTest {

    @Test
    @DisplayName("단과 대학을 생성한다.")
    void createParentMajor() {
        // given
        String name = "정보기술대학";

        // when
        Major result = Major.createMajor(name, null);

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getParent()).isNull();
    }

    @Test
    @DisplayName("학과를 생성한다.")
    void createChildrenMajor() {
        // given
        String name = "정보통신공학과";
        Major parent = Major.createMajor("정보기술대학", null);

        // when
        Major result = Major.createMajor(name, parent);

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getParent()).isEqualTo(parent);
    }

    @Test
    @DisplayName("학과의 이름을 변경한다.")
    void changeName() {
        // given
        Major major = Major.createMajor("정보통신공학과", null);

        // when
        major.changeName("컴퓨터공학과");

        // then
        assertThat(major.getName()).isEqualTo("컴퓨터공학과");
    }
}
