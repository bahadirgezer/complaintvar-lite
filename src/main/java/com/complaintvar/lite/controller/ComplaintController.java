package com.complaintvar.lite.controller;

import com.complaintvar.lite.dto.CompanyDTO;
import com.complaintvar.lite.dto.ComplaintDTO;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.service.ComplaintService;
import com.complaintvar.lite.entity.Complaint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/complaint")
public class ComplaintController {

    @Autowired
    private ModelMapper modelMapper;
    private final ComplaintService complaintService;

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDTO> getComplaintByPath(@PathVariable Long id) {
        return getComplaint(id);
    }

    @GetMapping(params = "id")
    public ResponseEntity<ComplaintDTO> getComplaintByParam(@RequestParam Long id) {
        return getComplaint(id);
    }

    private ResponseEntity<ComplaintDTO> getComplaint(@PathVariable Long id) {
        log.info(String.format("Getting complaint with id: %d"), id);
        ComplaintDTO complaintDTO = complaintService.getComplaintByID(id);
        if (complaintDTO == null) {
            log.debug("ComplaintDTO object is null.");
            throw new ResourceNotFoundException("Cannot get complaint. Null return.");
        }
        return ResponseEntity.ok().body(complaintDTO);
    }

    @PostMapping(params = {"user", "company"})
    public ResponseEntity<ComplaintDTO> createComplaint(@RequestBody ComplaintDTO newComplaintDTO, @RequestParam Long user, @RequestParam Long company) {
        log.info("Creating new complaint");
        ComplaintDTO complaintDTO = complaintService.createComplaint(newComplaintDTO, user, company);
        if (complaintDTO == null) {
            log.debug("ComplaintDTO object is null.");
            throw new ResourceNotFoundException("Cannot get complaint. Null return.");
        }
        return new ResponseEntity<ComplaintDTO>(complaintDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDTO> updateComplaintByPath(@PathVariable(name = "id") Long id, @RequestBody ComplaintDTO newComplaintDTO) {
        return updateComplaint(id, newComplaintDTO);
    }

    @PutMapping(params = "id")
    public ResponseEntity<ComplaintDTO> updateComplaintByParam(@RequestParam Long id, @RequestBody ComplaintDTO newComplaintDTO) {
        return updateComplaint(id, newComplaintDTO);
    }

    private ResponseEntity<ComplaintDTO> updateComplaint(@PathVariable(name = "id") Long id, @RequestBody ComplaintDTO newComplaintDTO) {
        log.info(String.format("Updating complaint with id: %d"), id);
        ComplaintDTO complaintDTO = complaintService.updateComplaint(id, newComplaintDTO);
        if (complaintDTO == null) {
            log.debug("ComplaintDTO object is null.");
            throw new ResourceNotFoundException("Cannot get complaint. Null return.");
        }
        return new ResponseEntity<ComplaintDTO>(complaintDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteComplaintByPath(@PathVariable(name = "id") Long id) {
        return complaintService.deleteComplaint(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }

    @DeleteMapping(params = "id")
    public HttpStatus deleteComplaintByParam(@RequestParam Long id) {
        return complaintService.deleteComplaint(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }
}
