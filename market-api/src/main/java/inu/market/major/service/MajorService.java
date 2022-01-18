package inu.market.major.service;

import inu.market.common.DuplicateException;
import inu.market.common.NotFoundException;
import inu.market.major.domain.Major;
import inu.market.major.domain.MajorRepository;
import inu.market.major.dto.MajorCreateRequest;
import inu.market.major.dto.MajorResponse;
import inu.market.major.dto.MajorUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional
    public Long createParent(MajorCreateRequest request) {

        if (majorRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(request.getName() + "는 이미 존재하는 학과입니다.");
        }

        Major major = Major.createMajor(request.getName(), null);
        return majorRepository.save(major).getId();
    }

    @Transactional
    public Long createChildren(Long majorId, MajorCreateRequest request) {
        Major findParent = majorRepository.findById(majorId)
                .orElseThrow(() -> new NotFoundException(majorId + "는 존재하지 않는 학과 ID 입니다."));

        if (majorRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(request.getName() + "는 이미 존재하는 학과입니다.");
        }

        Major major = Major.createMajor(request.getName(), findParent);
        return  majorRepository.save(major).getId();
    }

    @Transactional
    public void update(Long majorId, MajorUpdateRequest request) {
        Major findMajor = majorRepository.findById(majorId)
                .orElseThrow(() -> new NotFoundException(majorId + "는 존재하지 않는 학과 ID 입니다."));

        if (majorRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(request.getName() + "는 이미 존재하는 학과입니다.");
        }

        findMajor.changeName(request.getName());
    }

    @Transactional
    public void delete(Long majorId) {
        Major findMajor = majorRepository.findById(majorId)
                .orElseThrow(() -> new NotFoundException(majorId + "는 존재하지 않는 학과 ID 입니다."));

        majorRepository.delete(findMajor);
    }

    public List<MajorResponse> findParents() {
        List<Major> majors = majorRepository.findByParentIsNull();
        return majors.stream()
                .map(major -> MajorResponse.from(major))
                .collect(Collectors.toList());
    }

    public List<MajorResponse> findChildrenById(Long majorId) {
        List<Major> majors = majorRepository.findByParentId(majorId);
        return majors.stream()
                .map(major -> MajorResponse.from(major))
                .collect(Collectors.toList());
    }
}
