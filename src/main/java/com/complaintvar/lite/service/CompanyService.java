package com.complaintvar.lite.service;

import com.complaintvar.lite.dto.CompanyDTO;
import com.complaintvar.lite.entity.Company;
import com.complaintvar.lite.exceptions.DuplicateEntityError;
import com.complaintvar.lite.exceptions.IllegalResourceFormatException;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper; //autowired yerine

    //TODO: Paginated get all and get some with sorting capabilities

    /**
     * Gets the company with the given ID.
     *
     * @param id ID of the company
     * @return company DTO of the fetched company
     */
    public CompanyDTO getCompanyByID(Long id) {
        log.info("Getting company by ID.");
        Company company;

        try {
            company = companyRepository.findCompanyByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }
        log.debug(String.format("Fetched company with ID: %d", id));

        if (company == null) {
            log.debug(String.format("Company with ID: %d not found.", id));
            throw new ResourceNotFoundException(String.format("Company Not Found. ID: %d", id));
        }

        log.debug(String.format("Returning company DTO with ID: %d", id));
        return new CompanyDTO(company.getId(),
                        company.getEmail(),
                        company.getName());
        //return modelMapper.map(company, CompanyDTO.class);
    }

    /**
     * Creates a new company from the companyDTO parameter.
     *
     * @param companyDTO company for creation
     * @return company DTO object saved to the database
     */
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        log.info("Creating company.");
        log.debug("Converting company DTO to company entity.");
        Company company = modelMapper.map(companyDTO, Company.class);

        if (company == null) {
            log.debug("Company creation failed. Null entity object.");
            throw new  IllegalResourceFormatException("Company entity does not exit. Null value.");
        }

        Company emailCheck;
        try {
            emailCheck = companyRepository.findCompanyByEmail(company.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }
        log.debug("Fetched company");

        if (emailCheck != null) {
            log.debug("Email in use by another company.");
            throw new DuplicateEntityError(String.format("Email \"%s\" already in use. Emails must be unique,", emailCheck.getEmail()));
        }

        Company savedCompany;
        try {
            savedCompany = companyRepository.save(company);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning company DTO with ID: %d", savedCompany.getId()));
        return modelMapper.map(savedCompany, CompanyDTO.class);
    }

    /**
     * Updates the email of the company.
     *
     * @param id ID of the company for the update
     * @param newEmail updated email
     * @return company DTO object saved to the database
     */
    public CompanyDTO updateEmail(Long id, String newEmail) {
        log.info("Updating company email.");
        log.debug("Converting company DTO to company entitiy.");
        Company company;

        try {
            company = companyRepository.findCompanyByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        if (newEmail.isBlank()) {
            log.debug("Company name is blank. Illegal format");
            throw new IllegalResourceFormatException("Company email cannot be blank.");
        }

        Company emailCheck;
        try {
            emailCheck = companyRepository.findCompanyByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }
        if (emailCheck != null) {
            if (emailCheck.getId() != company.getId()) {
                log.debug("Company update failed. Email in use.");
                throw new DuplicateEntityError("Email already in use, cannot update company.");
            }
        }
        company.setEmail(newEmail);

        Company savedCompany;
        try {
            savedCompany = companyRepository.save(company);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning company DTO with ID: %d", savedCompany.getId()));
        return modelMapper.map(savedCompany, CompanyDTO.class);
    }

    /**
     * Updates the name of the company.
     *
     * @param id ID of the company for the update
     * @param newName updated name
     * @return company DTO object saved to the database
     */
    public CompanyDTO updateName(Long id, String newName) {
        log.info("Updating company name.");
        log.debug("Converting company DTO to company entitiy.");
        Company company;

        try {
            company = companyRepository.findCompanyByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        if (newName.isBlank()) {
            log.debug("Company name is blank. Illegal format");
            throw new IllegalResourceFormatException("Company name cannot be blank.");
        }
        company.setName(newName);

        Company savedCompany;
        try {
            savedCompany = companyRepository.save(company);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning company DTO with ID: %d", savedCompany.getId()));
        return modelMapper.map(savedCompany, CompanyDTO.class);
    }

    /**
     * Updates the company -specified by the ID- with the new company DTO
     *
     * @param id ID of the company to be updated
     * @param companyDTO new company DTO
     * @return company DTO of the company saved to the database
     */
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        log.info("Updating company");
        log.debug("Converting company DTO to company entity.");
        Company newCompany = modelMapper.map(companyDTO, Company.class);
        Company company;

        try {
            company = companyRepository.findCompanyByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        if (company == null) {
            log.debug("Company update failed. Null entity object in the repository.");
            throw new IllegalResourceFormatException("Company entity does not exit. Null value.");
        }

        if (newCompany == null) {
            log.debug("Company update failed. Null entity object from the parameter.");
            throw new IllegalResourceFormatException("Company entity does not exit. Null value.");
        }


        if (newCompany.getEmail() != company.getEmail()) { //if the email is updated, check uniqueness.
            log.debug("Checking updated company email.");
            Company checkEmail;
            try {
                checkEmail = companyRepository.findCompanyByEmail(newCompany.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("Database query exception caught.");
                return null;
            }

            if (checkEmail != null) {
                if (checkEmail.getId() != company.getId()) {
                    log.debug("Company update failed. Email in use.");
                    throw new DuplicateEntityError("Email already in use, cannot update company.");
                }
            }
        }
        company.setEmail(newCompany.getEmail());

        if (newCompany.getName().isBlank()) { //Whitespaces count as empty.
            log.debug("Company name is blank. Illegal format");
            throw new IllegalResourceFormatException("Company name cannot be blank.");
        }
        company.setName(newCompany.getName());

        Company savedCompany;
        try {
            savedCompany = companyRepository.save(company);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning company DTO with ID: %d", savedCompany.getId()));
        return modelMapper.map(savedCompany, CompanyDTO.class);
    }

    public Boolean deleteCompany(Long id) {
        log.info("Deleting company");
        Company company;
        log.debug("Fetching company to delete.");
        try {
            company = companyRepository.findCompanyByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return false;
        }

        if (company == null) {
            log.debug("Company delete failed. Null entity object.");
            throw new ResourceNotFoundException("Company entity does not exit. Null value.");
        }

        log.debug(String.format("Deleting company with ID: %d", company.getId()));
        try {
            companyRepository.delete(company);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return false;
        }

        return true;
    }

    /**
     * Gets a list of companies which start with the give string.
     * @param id the
     */
    public List<CompanyDTO> getCompaniesWithIdLowerThan(Long id) {
        List<Company> companies = companyRepository.getIdLowerThan(id);
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException("No such company.");
        }
        return companies.stream()
                .map(company -> modelMapper.map(company, CompanyDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Gets a list of companies with pagination.
     *
     * @param page page number [0, maxPage] (Required)
     * @param large page size ["true"|"false"]
     * @param sortBy field name to sort by
     * @param order page order ["asc"|"desc"]
     * @return list of company DTOs in the requested page
     */
    public List<CompanyDTO> getPaginatedCompanies(Integer page, String large, String sortBy, String order) {
        Sort sort = null;
        if (!sortBy.isBlank()) {
            ;
            if (order.equalsIgnoreCase("asc")) {
                sort = Sort.by(sortBy).ascending();
            } else if (order.equalsIgnoreCase("desc")){
                sort = Sort.by(sortBy).descending();
            }
        }
        if (sort == null) {
            sort = Sort.by("id");
        }

        Pageable pageable = PageRequest.of(page,
                (large.equalsIgnoreCase("true")) ? 5 : 2,
                sort);
        Page<Company> pagedResult =  companyRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent().stream()
                    .map(company -> new CompanyDTO(company.getId(),
                            company.getEmail(),
                            company.getName()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<CompanyDTO>();
    }
}
