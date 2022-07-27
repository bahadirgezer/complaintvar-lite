package com.complaintvar.lite.controller;

import com.complaintvar.lite.dto.CompanyDTO;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/company")
public class CompanyController {
    private final CompanyService companyService;

    /**
     * C1: Paginates companies and returns a list of them.
     *
     * @param page page number [0, maxPage] (Required)
     * @param large page size ["true"|"false"]
     * @param sortBy field name to sort by
     * @param order page order ["asc"|"desc"]
     * @return list of company DTOs in the requested page
     */
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getPaginatedCompanies(
            @RequestParam Integer page,
            @RequestParam(required = false, defaultValue = "false") String large,
            @RequestParam(name = "sort", required = false, defaultValue = "") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String order) {
        List<CompanyDTO> companies = companyService.getPaginatedCompanies(page, large, sortBy, order);

        return ResponseEntity.ok().body(companies);
    }

    /**
     * C2: Get a single company with id as a path.
     *
     * @param id id of the company
     * @return the fetched company DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyByPath(@PathVariable Long id) {
        return getCompany(id);
    }

    /**
     * C3: Get a single company with id as a parameter.
     *
     * @param id id of the company
     * @return the fetched company DTO
     */
    @GetMapping(params = "id")
    public ResponseEntity<CompanyDTO> getCompanyByParam(@RequestParam Long id) {
        return getCompany(id);
    }

    /**
     * C4: Cover method for company get request.
     *
     * @param id id of the company
     * @return
     */
    private ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        log.info(String.format("Getting company with id: %d", id));
        CompanyDTO companyDTO = companyService.getCompanyByID(id);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null.");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return ResponseEntity.ok().body(companyDTO);
    }

    /**
     * C5: Creates and saves a company in the database.
     *
     * @param newCompanyDTO model for the new company
     * @return company DTO which is saved to the database
     */
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

    /**
     * C6: Update company specified by the id using a new company DTO object.
     *
     * @param id ID of the company to be updated
     * @param newCompanyDTO updated version of the company
     * @return company DTO which is saved to the database
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompanyByPath(@PathVariable(name = "id") Long id, @RequestBody CompanyDTO newCompanyDTO) {
        return updateCompany(id, newCompanyDTO);
    }

    /**
     * C7: Update company specified by the id using a new company DTO object.
     *
     * @param id ID of the company to be updated
     * @param newCompanyDTO updated version of the company
     * @return company DTO which is saved to the database
     */
    @PutMapping(params = "id")
    public ResponseEntity<CompanyDTO> updateCompanyByParam(@RequestParam Long id, @RequestBody CompanyDTO newCompanyDTO) {
        return updateCompany(id, newCompanyDTO);
    }

    /**
     * C8: Cover method for company put request.
     *
     * @param id ID of the company to be updated
     * @param newCompanyDTO updated version of the company
     * @return company DTO which is saved to the database
     */
    private ResponseEntity<CompanyDTO> updateCompany(@PathVariable(name = "id") Long id, @RequestBody CompanyDTO newCompanyDTO) {
        log.info(String.format("Updating company with id: %d", id));
        CompanyDTO companyDTO = companyService.updateCompany(id, newCompanyDTO);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null.");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return new ResponseEntity<CompanyDTO>(companyDTO, HttpStatus.ACCEPTED);
    }

    /**
     * C9: Updates the company email.
     *
     * @param id ID of the company to be updated
     * @param email new email for the company
     * @return company DTO which is saved to the database
     */
    @PutMapping(params = {"id", "email"})
    public ResponseEntity<CompanyDTO> updateCompanyEmail(@RequestParam Long id, @RequestParam String email) {
        log.info(String.format("Updating company email of id: %d", id));
        CompanyDTO companyDTO = companyService.updateEmail(id, email);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return new ResponseEntity<CompanyDTO>(companyDTO, HttpStatus.ACCEPTED);
    }

    /**
     * C10: Updates the company name.
     *
     * @param id ID of the company to be updated
     * @param name new name for the company
     * @return company DTO which is saved to the database
     */
    @PutMapping(params = {"id", "name"})
    public ResponseEntity<CompanyDTO> updateCompanyName(@RequestParam Long id, @RequestParam String name) {
        log.info(String.format("Updating company name of id: %d", id));
        CompanyDTO companyDTO = companyService.updateName(id, name);
        if (companyDTO == null) {
            log.debug("CompanyDTO object is null");
            throw new ResourceNotFoundException("Cannot get company. Null return.");
        }
        return new ResponseEntity<CompanyDTO>(companyDTO, HttpStatus.ACCEPTED);
    }

    /**
     * C11: Delete a company from the database with the given ID as a path variable.
     *
     * @param id ID of the company to be deleted
     * @return HTTP status based on the success of the operation
     */
    @DeleteMapping("/{id}")
    public HttpStatus deleteCompanyByPath(@PathVariable(name = "id") Long id) {
        return companyService.deleteCompany(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }

    /**
     * C12: Delete a company from the database with the given ID as parameter.
     *
     * @param id
     * @return HTTP status based on the success of the operation
     */
    @DeleteMapping(params = "id")
    public HttpStatus deleteCompanyByParam(@RequestParam Long id) {
        return companyService.deleteCompany(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }

    /**
     * C13: Gets companies with names starting with head.
     *
     * @param id starting string for the company names
     * @return a list of company DTOs
     */
    @GetMapping(path="/all")
    public ResponseEntity<List<CompanyDTO>> companiesWithIdLowerThan(@RequestParam Long id) {
        List<CompanyDTO> companies = companyService.getCompaniesWithIdLowerThan(id);
        return ResponseEntity.ok().body(companies);
    }
}
