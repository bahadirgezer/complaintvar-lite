package com.sikayetvar.complaint;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/complaint")
public class ComplaintController {

    @Autowired
    private ModelMapper modelMapper;
    private ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public List<ComplaintDTO> getAllComplaints() {
        return complaintService.getAllComplaints()
                .stream().map(complaint -> modelMapper.map(complaint, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDTO> getComplaintByPath(@PathVariable(name = "id") Long id) {
        Complaint complaint = complaintService.getComplaintByID(id);

        // convert entity to DTO
        ComplaintDTO complaintResponse = modelMapper.map(complaint, ComplaintDTO.class);

        return ResponseEntity.ok().body(complaintResponse);
    }

    @GetMapping(params = "id")
    public ResponseEntity<ComplaintDTO> getComplaintByParam(@RequestParam Long id) {
        Complaint complaint = complaintService.getComplaintByID(id);

        // convert entity to DTO
        ComplaintDTO complaintResponse = modelMapper.map(complaint, ComplaintDTO.class);
        return ResponseEntity.ok().body(complaintResponse);
    }

    @PostMapping
    public ResponseEntity<ComplaintDTO> createComplaint(@RequestBody ComplaintDTO complaintDTO) {

        // convert DTO to entity
        Complaint complaintRequest = modelMapper.map(complaintDTO, Complaint.class);

        Complaint complaint = complaintService.createComplaint(complaintRequest);

        // convert entity to DTO
        ComplaintDTO complaintResponse = modelMapper.map(complaint, ComplaintDTO.class);
        return new ResponseEntity<ComplaintDTO>(complaintResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDTO> updateComplaintByPath(@PathVariable(name = "id") Long id, @RequestBody ComplaintDTO complaintDTO) {

        //convert DTO to Entity
        Complaint complaintRequest = modelMapper.map(complaintDTO, Complaint.class);
        Complaint complaint = complaintService.updateComplaint(id, complaintRequest);

        //covert updated entity back to DTO for posting
        ComplaintDTO complaintResponse = modelMapper.map(complaint, ComplaintDTO.class);
        return ResponseEntity.ok().body(complaintResponse);
    }

    @PutMapping(params = "id")
    public ResponseEntity<ComplaintDTO> updateComplaintByParam(@RequestParam Long id, @RequestBody ComplaintDTO complaintDTO) {

        //convert DTO to Entity
        Complaint complaintRequest = modelMapper.map(complaintDTO, Complaint.class);
        Complaint complaint = complaintService.updateComplaint(id, complaintRequest);

        //covert updated entity back to DTO for posting
        ComplaintDTO complaintResponse = modelMapper.map(complaint, ComplaintDTO.class);
        return ResponseEntity.ok().body(complaintResponse);
    }

    //TODO: adding response to delete might be better
    @DeleteMapping
    public void deleteComplaint(@PathVariable(name = "id") Long id) {
        complaintService.deleteComplaint(id);
    }
}