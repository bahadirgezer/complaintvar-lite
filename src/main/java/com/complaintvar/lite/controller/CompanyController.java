package com.complaintvar.lite.controller;

import com.complaintvar.lite.dto.CompanyDTO;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/company")
public class CompanyController {
    private final CompanyService companyService;

    //TODO: Pagination
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyByPath(@PathVariable Long id) {
        return getCompany(id);
    }

    @GetMapping(params = "id")
    public ResponseEntity<CompanyDTO> getCompanyByParam(@RequestParam Long id) {
        return getCompany(id);
    }

    private ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        log.info(String.format("Getting company with id: %d"), id);
        CompanyDTO companyDTO = companyService.getCompanyByID(id);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null.");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return ResponseEntity.ok().body(companyDTO);
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO newCompanyDTO) {
        log.info("Creating new company");
        CompanyDTO companyDTO = companyService.createCompany(newCompanyDTO);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null.");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return new ResponseEntity<CompanyDTO>(companyDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompanyByPath(@PathVariable(name = "id") Long id, @RequestBody CompanyDTO newCompanyDTO) {
        return updateCompany(id, newCompanyDTO);
    }

    @PutMapping(params = "id")
    public ResponseEntity<CompanyDTO> updateCompanyByParam(@RequestParam Long id, @RequestBody CompanyDTO newCompanyDTO) {
        return updateCompany(id, newCompanyDTO);
    }

    private ResponseEntity<CompanyDTO> updateCompany(@PathVariable(name = "id") Long id, @RequestBody CompanyDTO newCompanyDTO) {
        log.info(String.format("Updating company with id: %d"), id);
        CompanyDTO companyDTO = companyService.updateCompany(id, newCompanyDTO);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null.");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return new ResponseEntity<CompanyDTO>(companyDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteCompanyByPath(@PathVariable(name = "id") Long id) {
        return companyService.deleteCompany(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }

    @DeleteMapping(params = "id")
    public HttpStatus deleteCompanyByParam(@RequestParam Long id) {
        return companyService.deleteCompany(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }

    @PutMapping("/all")
    public HttpStatus updateEveryVerification(@RequestParam String verification) {
        if (verification.equalsIgnoreCase("true")) {
            companyService.updateEveryVerification(true);
        } else if (verification.equalsIgnoreCase("false")) {
            companyService.updateEveryVerification(false);
        } else {
            return HttpStatus.NOT_ACCEPTABLE;
        }
        return HttpStatus.ACCEPTED;
    }
}
