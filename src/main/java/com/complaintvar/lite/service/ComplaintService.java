package com.complaintvar.lite.service;

import com.complaintvar.lite.dto.ComplaintDTO;
import com.complaintvar.lite.exceptions.IllegalResourceFormatException;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.repository.CompanyRepository;
import com.complaintvar.lite.entity.Complaint;
import com.complaintvar.lite.repository.ComplaintRepository;
import com.complaintvar.lite.entity.User;
import com.complaintvar.lite.repository.UserRepository;
import com.complaintvar.lite.entity.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ComplaintDTO getComplaintByID(Long id) {
        log.info("Getting complaint by ID.");
        Complaint complaint;

        try {
            complaint = complaintRepository.findComplaintByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }
        log.debug(String.format("Fetched complaint with ID: %d", id));

        if (complaint == null) {
            log.debug(String.format("Complaint with ID: %d not found.", id));
            throw new ResourceNotFoundException(String.format("Complaint Not Found. ID: %d", id));
        }

        log.debug(String.format("Returning complaint DTO with ID: %d", id));
        return modelMapper.map(complaint, ComplaintDTO.class);
    }

    public ComplaintDTO createComplaint(ComplaintDTO complaintDTO, Long user, Long company) {
        log.info("Creating complaint.");
        log.debug("Converting complaint DTO to complaint entity.");
        Complaint complaint = modelMapper.map(complaintDTO, Complaint.class);

        if (complaint == null) {
            log.debug("Complaint creation failed. Null entity object.");
            throw new IllegalResourceFormatException("Complaint entity does not exit. Null value.");
        }

        if (complaint.getBody().isBlank()) {
            log.debug("Complaint body is blank. Illegal format");
            throw new IllegalResourceFormatException("Complaint body cannot be blank.");
        }

        if (complaint.getTitle().isBlank()) {
            log.debug("Complaint title is blank. Illegal format");
            throw new IllegalResourceFormatException("Complaint title cannot be blank.");
        }

        Company companyEntity;
        User userEntity;
        try {
            companyEntity = companyRepository.findCompanyByID(company);
            userEntity = userRepository.findUserByID(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        if (companyEntity == null) {
            log.debug("Complaint creation failed. Null company entity object.");
            throw new  IllegalResourceFormatException("Company entity does not exit. Invalid complaint creation.");
        }
        complaint.setCompany(companyEntity);

        if (userEntity == null) {
            log.debug("Complaint creation failed. Null user entity object.");
            throw new  IllegalResourceFormatException("User entity does not exit. Invalid complaint creation.");
        }
        complaint.setUser(userEntity);

        Complaint savedComplaint;
        try {
            savedComplaint = complaintRepository.save(complaint);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning complaint DTO with ID: %d", savedComplaint.getId()));
        return modelMapper.map(savedComplaint, ComplaintDTO.class);
    }

    public ComplaintDTO updateComplaint(Long id, ComplaintDTO complaintDTO) {
        log.info("Updating complaint");
        log.debug("Converting complaint DTO to complaint entity.");
        Complaint newComplaint = modelMapper.map(complaintDTO, Complaint.class);
        Complaint complaint;

        try {
            complaint = complaintRepository.findComplaintByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        if (complaint == null) {
            log.debug("Complaint update failed. Null entity object in the repository.");
            throw new IllegalResourceFormatException("Complaint entity does not exit. Null value.");
        }

        if (newComplaint == null) {
            log.debug("Complaint update failed. Null entity object from the parameter.");
            throw new IllegalResourceFormatException("Complaint entity does not exit. Null value.");
        }

        if (newComplaint.getTitle().isBlank()) { //Whitespaces count as empty.
            log.debug("Company title is blank. Illegal format");
            throw new IllegalResourceFormatException("Company title cannot be blank.");
        }
        complaint.setTitle(newComplaint.getTitle()); //set title if ok

        if (newComplaint.getBody().isBlank()) { //Whitespaces count as empty.
            log.debug("Company body is blank. Illegal format");
            throw new IllegalResourceFormatException("Company body cannot be blank.");
        }
        complaint.setBody(newComplaint.getBody()); //set body if ok

        if (newComplaint.getUser().getId() != complaint.getUser().getId()) { //user and company are immutable
            log.debug("Updated complaint does not have the same user.");
            throw new UnsupportedOperationException("Cannot update the user of a complaint.");
        }

        if (newComplaint.getCompany().getId() != complaint.getCompany().getId()) {
            log.debug("Updated complaint does not have the same company.");
            throw new UnsupportedOperationException("Cannot update the company of a complaint.");
        }

        Complaint savedComplaint;
        try {
            savedComplaint = complaintRepository.save(complaint);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning complaint DTO with ID: %d", savedComplaint.getId()));
        return modelMapper.map(savedComplaint, ComplaintDTO.class);
    }

    public Boolean deleteComplaint(long id) {
        log.info("Deleting complaint");
        Complaint complaint;
        log.debug("Fetching complaint to delete.");
        try {
            complaint = complaintRepository.findComplaintByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return false;
        }

        if (complaint == null) {
            log.debug("Complaint delete failed. Null entity object.");
            throw new ResourceNotFoundException("Complaint entity does not exit. Null value.");
        }

        log.debug(String.format("Deleting complaint with ID: %d", complaint.getId()));
        try {
            complaintRepository.delete(complaint);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return false;
        }

        return true;
    }
}
