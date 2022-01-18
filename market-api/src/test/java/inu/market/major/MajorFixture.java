package inu.market.major;

import inu.market.major.domain.Major;
import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.dto.MajorResponse;
import inu.market.major.dto.MajorUpdateRequest;

public class MajorFixture {

    public static final String TEST_MAJOR_NAME = " 정보통신공학과";

    public static final Major TEST_PARENT_MAJOR = new Major(1L, "정보기술대학", null);

    public static final Major TEST_MAJOR = new Major(1L, "정보통신공학과", TEST_PARENT_MAJOR);

    public static final MajorCreateRequest TEST_MAJOR_CREATE_REQUEST = new MajorCreateRequest(TEST_MAJOR_NAME);

    public static final MajorUpdateRequest TEST_MAJOR_UPDATE_REQUEST = new MajorUpdateRequest(TEST_MAJOR_NAME);

    public static final MajorResponse TEST_MAJOR_RESPONSE = MajorResponse.from(TEST_MAJOR);

}
