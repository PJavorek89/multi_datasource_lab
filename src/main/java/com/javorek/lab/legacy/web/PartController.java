package com.javorek.lab.legacy.web;

import com.javorek.lab.legacy.model.Part;
import com.javorek.lab.legacy.model.PartFilter;
import com.javorek.lab.legacy.service.PartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController                      // bean + návratová hodnota se automaticky serializuje do JSON
@RequestMapping("/parts")            // společná cesta pro všechny metody v controlleru
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {   // constructor injection
        this.partService = partService;
    }

    @GetMapping                      // GET /parts
    public List<Part> findParts(
            @RequestParam(required = false) String category,        // ?category=BRAKES
            @RequestParam(required = false) String plant,           // &plant=PL3
            @RequestParam(required = false) BigDecimal minPrice,    // &minPrice=4000
            @RequestParam(required = false) List<String> partNos,   // &partNos=P1,P2 nebo &partNos=P1&partNos=P2
            @RequestParam(defaultValue = "part_no") String sort) {  // výchozí řazení
        return partService.findParts(
                new PartFilter(category, plant, minPrice, partNos, sort));
    }
}
