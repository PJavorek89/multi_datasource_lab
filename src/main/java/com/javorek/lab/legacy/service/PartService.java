package com.javorek.lab.legacy.service;

import com.javorek.lab.legacy.mapper.PartMapper;
import com.javorek.lab.legacy.model.Part;
import com.javorek.lab.legacy.model.PartFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PartService {

    private static final Set<String> ALLOWED_SORT = Set.of("part_no", "name", "price", "created_at");

    private final PartMapper partMapper;

    public PartService(PartMapper partMapper) {
        this.partMapper = partMapper;
    }

    @Transactional(transactionManager = "legacyTransactionManager", readOnly = true)
    public List<Part> findParts(PartFilter filter) {
        String sort = ALLOWED_SORT.contains(filter.sort()) ? filter.sort() : "part_no";  // ochrana pro ${}
        return partMapper.findParts(new PartFilter(
                filter.category(), filter.plant(), filter.minPrice(), filter.partNos(), sort));
    }
}