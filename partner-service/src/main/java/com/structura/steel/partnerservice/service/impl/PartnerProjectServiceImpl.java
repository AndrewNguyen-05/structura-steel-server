package com.structura.steel.partnerservice.service.impl;

import com.structura.steel.partnerservice.dto.request.PartnerProjectRequestDto;
import com.structura.steel.partnerservice.dto.response.PartnerProjectResponseDto;
import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.feign.ProductFeignClient;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.service.PartnerProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerProjectServiceImpl implements PartnerProjectService {

    private final PartnerProjectRepository partnerProjectRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final ProductFeignClient productFeignClient; // Để call sang product service

    @Override
    public PartnerProjectResponseDto createPartnerProject(PartnerProjectRequestDto dto) {
        // Kiểm tra Partner tồn tại
        Partner partner = partnerRepository.findById(dto.getPartnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + dto.getPartnerId()));

        // Map sang entity
        PartnerProject project = partnerMapper.toPartnerProject(dto);
        project.setPartner(partner);

        // Ở DB ta không thể lưu List<Product> vì Product ở service khác,
        // ta có thể lưu chuỗi productIds. Ví dụ:
        if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
            // Convert List<Long> -> "1,2,3" (đơn giản)
            String productIdsStr = dto.getProductIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            // Ta dùng tạm cột 'extraInfo' hoặc cột 'productIds' (tự thêm) để lưu trữ
            // Ở đây ví dụ thêm cột "productIds" ở PartnerProject:
            // project.setProductIds(productIdsStr);
        }

        PartnerProject saved = partnerProjectRepository.save(project);

        return partnerMapper.toPartnerProjectResponseDto(saved);
    }

    @Override
    public PartnerProjectResponseDto updatePartnerProject(Long id, PartnerProjectRequestDto dto) {
        PartnerProject existing = partnerProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + id));

        partnerMapper.updatePartnerProjectFromDto(dto, existing);
        // Lưu ý nếu dto.getPartnerId() thay đổi, cần set lại partner:
        if (dto.getPartnerId() != null) {
            Partner partner = partnerRepository.findById(dto.getPartnerId())
                    .orElseThrow(() -> new RuntimeException("Partner not found with id: " + dto.getPartnerId()));
            existing.setPartner(partner);
        }

        // Tương tự, xử lý productIds
        if (dto.getProductIds() != null) {
            // Convert sang chuỗi
            String productIdsStr = dto.getProductIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            // existing.setProductIds(productIdsStr);
        }

        PartnerProject updated = partnerProjectRepository.save(existing);
        return partnerMapper.toPartnerProjectResponseDto(updated);
    }

    @Override
    public PartnerProjectResponseDto getPartnerProject(Long id) {
        PartnerProject project = partnerProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + id));
        return partnerMapper.toPartnerProjectResponseDto(project);
    }

    @Override
    public void deletePartnerProject(Long id) {
        PartnerProject project = partnerProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerProject not found with id: " + id));
        partnerProjectRepository.delete(project);
    }

    @Override
    public List<PartnerProjectResponseDto> getAllPartnerProjects() {
        List<PartnerProject> projects = partnerProjectRepository.findAll();
        return partnerMapper.toPartnerProjectResponseDtoList(projects);
    }
}
