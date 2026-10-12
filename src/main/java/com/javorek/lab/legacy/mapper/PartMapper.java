package com.javorek.lab.legacy.mapper;

import com.javorek.lab.legacy.model.Part;
import com.javorek.lab.legacy.model.PartFilter;

import java.util.List;

public interface PartMapper {
    List<Part> findParts(PartFilter filter);
}
